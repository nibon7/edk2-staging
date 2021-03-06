// Copyright (c) 2019 Intel Corporation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

#![feature(alloc_layout_extra)]
#![feature(allocator_api)]
#![feature(core_panic_info)]

#![cfg_attr(not(test), no_std)]

#![allow(unused)]

mod mem;

use r_efi::efi;
use r_efi::efi::{Status};

extern {
  fn AllocatePool (Size: usize) -> *mut c_void;
  fn FreePool (Buffer: *mut c_void);
}

use core::panic::PanicInfo;
use core::ffi::c_void;

use core::mem::size_of;
use core::mem::transmute;

use core::slice;
use core::slice::from_raw_parts;
use core::slice::from_raw_parts_mut;

extern crate uefi_rust_panic_lib;
extern crate uefi_rust_allocation_lib;

extern crate alloc;

use alloc::vec::Vec;
use alloc::boxed::Box;
use alloc::{
    alloc::{handle_alloc_error, Alloc, Global, Layout},
};


#[no_mangle]
#[export_name = "TestIntegerOverflow"]
pub extern fn test_integer_overflow (
    buffer_size: usize,
    width : u32,
    height : u32,
    ) -> Status
{
    let data_size = width * height * 4;

    if data_size as usize > buffer_size {
      return Status::UNSUPPORTED;
    }

    Status::SUCCESS
}

#[no_mangle]
#[export_name = "TestIntegerCheckedOverflow"]
pub extern fn test_integer_checked_overflow (
    buffer_size: usize,
    width : u32,
    height : u32,
    ) -> Status
{

    let mut data_size: u32 = 0;

    match width.checked_mul(height) {
      Some(size) => {data_size = size},
      None => {return Status::INVALID_PARAMETER},
    }
    match data_size.checked_mul(4) {
      Some(size) => {data_size = size},
      None => {return Status::INVALID_PARAMETER},
    }

    if data_size as usize > buffer_size {
      return Status::UNSUPPORTED;
    }

    Status::SUCCESS
}

#[no_mangle]
#[export_name = "TestIntegerCast"]
pub extern fn test_integer_cast (
    buffer_size: u64,
    ) -> u32
{
    let data_size : u32 = buffer_size as u32;
    data_size
}

extern {
  fn ExternInit(Data: *mut usize);
}

#[no_mangle]
#[export_name = "TestUninitializedVariable"]
pub extern fn test_uninitializd_variable (
    index: usize,
    ) -> usize
{
    let mut data : usize = 1;

    if index > 10 {
      data = 0;
    }

    unsafe { ExternInit (&mut data ); }

    data = data + 1;

    data
}

#[no_mangle]
#[export_name = "TestArrayOutOfRange"]
pub extern fn test_array_out_of_range (
    index: usize,
    ) -> usize
{
    let mut data : [u8; 8] = [0; 8];

    data[index] = 1;

    data[index + 1] as usize
}

#[repr(C)]
#[derive(Copy, Clone, Debug)]
pub struct TestTable {
    pub r#type: u32,
    pub length: u32,
    pub value: [u8; 0],
}

#[no_mangle]
#[export_name = "TestBufferOverflow"]
pub extern fn test_buffer_overflow (
    buffer: &mut [u8; 0],
    buffer_size: usize,
    table: &TestTable,
    table_size: usize,
    )
{
    let mut dest = crate::mem::MemoryRegion::new(buffer as *mut [u8; 0] as usize as u64, buffer_size as u64);
    let mut source = crate::mem::MemoryRegion::new(&table.value as *const [u8; 0] as usize as u64, table_size as u64);

    for index in 0_u64 .. table.length as u64 {
      dest.write_u8(index, source.read_u8(index));
    }
}

#[repr(C)]
#[derive(Copy, Clone)]
pub struct TestTableFixed {
    pub r#type: u32,
    pub length: u32,
    pub value: [u8; 64],
}

#[no_mangle]
#[export_name = "TestBufferOverflowFixed"]
pub extern fn test_buffer_overflow_fixed (
    buffer: &mut [u8; 32],
    table: &TestTableFixed,
    )
{
    (*buffer)[0_usize..(table.length as usize)].copy_from_slice(
      &table.value[0_usize..(table.length as usize)]
      );
}

fn get_buffer<'a> () -> Option<&'a mut TestTableFixed>
{
    let ptr : *mut c_void = unsafe { AllocatePool (size_of::<TestTableFixed>()) };
    if ptr.is_null() {
      return None;
    }
    let buffer : &mut TestTableFixed = unsafe { core::mem::transmute::<*mut c_void, &mut TestTableFixed>(ptr) };
    Some(buffer)
}

fn release_buffer (test_table : &mut TestTableFixed)
{
  test_table.r#type = 0;
  unsafe { FreePool (test_table as *mut TestTableFixed as *mut c_void) ; }
}

#[no_mangle]
#[export_name = "TestBufferDrop"]
pub extern fn test_buffer_drop (
    
    )
{
    match get_buffer () {
      Some(buffer) => {
        buffer.r#type = 1;
        release_buffer(buffer);
        drop (buffer); // This is required.
        //buffer.r#type = 1; // error
      },
      None => {},
    }
}

#[no_mangle]
#[export_name = "TestBufferBorrow"]
pub extern fn test_buffer_borrow (
    test_table : &mut TestTableFixed
    )
{
    let test_table2 : &mut TestTableFixed = test_table;
    test_table2.r#type = 1;

    let test_table3 : &mut [u8; 64] = &mut test_table.value;
    test_table3[63] = 0;

    //test_table2.r#type = 2; // error
}

#[no_mangle]
#[export_name = "TestBufferAlloc"]
pub extern fn test_buffer_alloc (
    
    )
{
    let layout = unsafe { core::alloc::Layout::from_size_align_unchecked(32, 4) };
    unsafe {
      match Global.alloc (layout) {
        Ok(buffer) => {
          let mut box_buffer = Box::from_raw(from_raw_parts_mut(buffer.as_ptr(), layout.size()));
          box_buffer[0] = 1;
          Global.dealloc (buffer, layout);
          drop (buffer); // It is useless
          box_buffer[0] = 1; // cannot catch
        },
        Err(_) => handle_alloc_error (layout),
      }
    }

    let layout = core::alloc::Layout::new::<u32>();
    unsafe {
      match Global.alloc (layout) {
        Ok(buffer) => {
          Global.dealloc (buffer, layout);
        },
        Err(_) => handle_alloc_error (layout),
      }
    }
}

fn get_box (
    r#type: u32
    ) -> Box<TestTableFixed>
{
    let mut a = Box::new(TestTableFixed{
                       r#type: 0,
                       length: size_of::<TestTableFixed>() as u32,
                       value: [0; 64]
                       }); // it will call __rust_alloc().
    a.r#type = r#type;

    a
}

#[no_mangle]
#[export_name = "TestBoxAlloc"]
pub extern fn test_box_alloc (
    r#type: u32
    ) -> Box<TestTableFixed>
{
  let mut a = get_box(1);

  a.r#type = r#type;

  //test_box_free(a); // build fail.

  let b = a;
  b
}

#[no_mangle]
#[export_name = "TestBoxFree"]
pub extern fn test_box_free (
    buffer: Box<TestTableFixed>
    )
{
  // it will call __rust_dealloc()
}

#[no_mangle]
#[export_name = "TestBoxAllocFail"]
pub extern fn test_box_alloc_fail (
    size: u32
    ) -> Box<[u8; 0x800]>
{
    let mut a = Box::new([0_u8; 0x800]); // it will call __rust_alloc().

    a
}

#[no_mangle]
#[export_name = "TestBoxConvert"]
pub extern fn test_box_convert (
    size: usize
    ) -> *mut u8
{
    let layout = unsafe { core::alloc::Layout::from_size_align_unchecked(size, 4) };
    unsafe {
      match Global.alloc (layout) {
        Ok(buffer) => {
          let mut box_buffer = Box::<u8>::from_raw(from_raw_parts_mut(buffer.as_ptr(), layout.size()) as *mut [u8] as *mut u8 );
          Global.dealloc (buffer, layout);
          *box_buffer = 1;
          Box::<u8>::into_raw(box_buffer)
        },
        Err(_) => handle_alloc_error (layout),
      }
    }


}
