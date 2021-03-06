# Rust for EDKII

**This project is an experiment and should not be used production workloads.**

### Background

There have been various instances of advocacy https://msrc-blog.microsoft.com/2019/11/07/using-rust-in-windows/ for building system level software in Rust https://www.rust-lang.org/.
Given the ability to migrate components to Rust incrementally, we are trying to add support for building EDKII components in Rust as the first step.

### Branch Description

The code base of development is **edk2-stable201911** tag.

This branch owner: Jiewen Yao <[jiewen.yao@intel.com](mailto:jiewen.yao@intel.com)>

### Preface

For rust, we use x86_64-unknown-uefi (https://github.com/rust-lang/rust/blob/master/src/librustc_target/spec/x86_64_unknown_uefi.rs) and i686-unknown-uefi (https://github.com/rust-lang/rust/blob/master/src/librustc_target/spec/i686_unknown_uefi.rs) target to generate PE library or .efi image in Windows or Linux.
The module entrypoint name is efi_main(). (https://github.com/rust-lang/rust/blob/master/src/librustc_target/spec/uefi_base.rs)

For C code, we use LLVM9 toolchain to generate PE binary in Windows or Linux.

For asm, we use nasm in Windows or Linux.

### How to use

#### Pre-Requisite

##### Rust Tool

1) Install rust using installer.

Goto https://www.rust-lang.org/tools/install for windows or linux

Rust version should be *nightly*. 

You can also set profile to minimal install.

Then you should use blow command to install rust-src component

```rustup component add rust-src```

**Or (install from source)**

You can install rust from source code **this is not recommend** 

2) Install cargo-xbuild

```
cargo install cargo-xbuild
```

3) Prebuild binary

goto RustPkg\External\r-efi

```
cargo xbuild --release --target x86_64-unknown-uefi
cargo xbuild --target x86_64-unknown-uefi
cargo xbuild --release --target i686-unknown-uefi
cargo xbuild --target i686-unknown-uefi
```

##### LLVM9

REF: https://github.com/tianocore/tianocore.github.io/wiki/CLANG9-Tools-Chain

1) Install LLVM9.0.0 from
  
http://releases.llvm.org/download.html#9.0.0

2) set Enviroment

```
set CLANG_HOST_BIN=n
set CLANG_BIN=C:\Program Files\LLVM\bin\
```

or

```
export CLANG_BIN=/home/your_llvm_bin_path
```

#### Build

Currently, we may use ways to build UEFI module with rust support.

1. Build the rust module with Cargo.

  go to rust folder, such as RustPkg\Test\TestRustLangApp, RustPkg\MdeModulePkg\Universal\CapsulePei

  ```
  cargo xbuild [--release] --target [x86_64-unknown-uefi|i686-unknown-uefi]
  ```

  the output is target/[x86_64-unknown-uefi|i686-unknown-uefi]/[debug|release]/test_rust_lang_app.efi

  This only works for UEFI application.

2. Build the rust module with EDKII tools.

  ```
  build -p RustPkg/RustPkg.dsc -t CLANGPDB -a IA32
  build -p RustPkg/RustPkg.dsc -t CLANGPDB -a X64
  ``` 

  This will also help to add section header, FFS header, FV header for the .efi image.
  It supports to generate PEIM/DXE driver/SMM driver in final FD image. See RustPkg/RustPkg.fdf.

### Supported Build combination

1. C source + Rust source mixed in INF (Library or Module)
  
  Rust source code is supported by EDKII build rule – Rust-To-Lib-File (.rs => .lib)
  
  **Limitation: Rust cannot have external dependency.**

2. Pure Rust Module only.
   
   A Cargo.toml file is added to INF file as source.

   Rust Module build is supported by EDKII build rule – Toml-File.RUST_MODULE (Toml => .efi)

   Limitation: Runtime might be a problem, not sure about virtual address translation for rust internal global variable.

3. Pure Rust Module + Pure Rust Library with Cargo Dependency.
   
  The cargo dependency means the rust lib dependency declared in Cargo.toml.

4. Pure Rust Module + C Library with EDKII Dependency.

  Rust Module build is supported by EDKII build rule – Toml-File (Toml => .lib)

  The EDKII dependency means the EDKII lib dependency declared in INF.

  If a rust module is built with C, the cargo must use staticlib. Or rlib should be used.

5.  C Module + Pure Rust Library with EDKII Dependency.

  Rust Lib build is supported by EDKII build rule – Toml-File. (Toml => .lib)

6. Pure Rust Module + Pure Rust Library with EDKII Dependency.
  
  Same as #4 + #5.

### uefi-rust interaction

#### allocator

Some rust data structures requires GlobalAlloc.
A UEFI version (uefi_rust_allocation_lib) is implemented at RustPkg\Library\UefiRustAllocationLib.

#### panic

The rust requires panic handler.
A default one - simple deadloop (uefi_rust_panic_lib) is implemented at RustPkg\Library\UefiRustPanicLib.
A platform may choose to implement it a different way, such as reporting information via debug message.

### future work

1. uefi-rust definition

* have common crate for the specification defined by UEFI.org. (We have number of efi-rust support in open source,
  such as r-efi (https://github.com/r-util/r-efi), uefi-rs (https://github.com/rust-osdev/uefi-rs), redox_uefi (https://gitlab.redox-os.org/redox-os?utf8=%E2%9C%93&filter=uefi).
  Each project defines its own data structures. There is no common definition for UEFI/PI specification so far.
* add more complete UEFI specification definition
* add more complete UEFI Platform Initialization (PI) specification definition
* add more industry standard definition, such as PE/COFF image, TCG, ACPI, SMBIOS, etc.

2. uefi-rust interaction

* add PEI and SMM version GlobalAlloc.
* add debug output in panic handler. (need standardize the debug output API)
* add OsString (https://doc.rust-lang.org/stable/std/ffi/) support. (the default string type in UEFI is 16bit Unicode, while default string type in rust is utf8.)
* add uefi-runtime consideration. (convert the module global address for UEFI runtime.)

3. uefi-rust modules

* support cross module include.
* add more rust modules.

### Appendix A: Build Rust from source

1. download the source code

https://github.com/rust-lang/rust

2. follow readme.md to generate config.toml.

NOTE:

    set lld = true to build rust-lld.
    set extended = true to build rust-lld.
    set docs = false to save build time.

Linux OS:

    set prefix, sysconfdir = in Linux OS.

Windows OS:

    set python = "python" in Windows OS.
    set buid, host, target = x86_64-pc-windows-msvc in Windows OS.
    set allow-old-toolchain = true , if visual studio < vs2019

3. follow readme.md to build the source.

./x.py build

4. Install rust and cargo.

a) For Linux OS:

Use below commend to install.

./x.py install
./x.py install cargo

    rustc is at /bin.
    rust-lld is at /lib/rustlib/x86_64-unknown-linux-gnu/bin.

export RUST_PREFIX=<rust install dir>
export PATH=$RUST_PREFIX/bin:$RUST_PREFIX/lib/rustlib/x86_64-unknown-linux-gnu/bin:$PATH
export RUST_SRC=<rust> # modify to the rust git.
export XARGO_RUST_SRC=$RUST_SRC/src

b) For Windows OS:

Set CARGO_HOME environment (default to ~/.cargo. windows example: c:\users<user>.cargo)

Add binary location to PATH (Assume RUST_SRC= @REM modify to the rust git.)

    rustc.exe toolchain is at %RUST_SRC%\build\x86_64-pc-windows-msvc\stage2\bin
    cargo.exe and tools is at %RUST_SRC%\build\x86_64-pc-windows-msvc\stage2-tools-bin

set RUST_SRC=<rust> @REM modify to the rust git.
set CARGO_HOME=c:\work\.cargo
set PATH=%CARGO_HOME%\bin;%RUST_SRC%\build\x86_64-pc-windows-msvc\stage2\bin;%RUST_SRC%\build\x86_64-pc-windows-msvc\stage2-tools-bin;%PATH%
set XARGO_RUST_SRC=%RUST_SRC%\src

Other way: Copy cargo.exe from %RUST_SRC%\build\x86_64-pc-windows-msvc\stage2-tools-bin to %RUST_SRC%\build\x86_64-pc-windows-msvc\stage2\bin

set RUST_SRC=<rust> @REM modify to the rust git.
rustup toolchain link rust-uefi %RUST_SRC%x\build\x86_64-pc-windows-msvc\stage2
rustup default rust-uefi
set XARGO_RUST_SRC=%RUST_SRC%\src

### Appendix B: Build LLVM from source

1. download the source.

Follow http://clang.llvm.org/get_started.html

2. configure

Linux OS:

cmake -DLLVM_ENABLE_PROJECTS="clang;lld" -G "Unix Makefiles" ../llvm

Windows OS: (assume using VS2017)

cmake -DLLVM_ENABLE_PROJECTS="clang;lld" -G "Visual Studio 15 2017" -A x64 -Thost=x64 ..\llvm

NOTE:

    use LLVM_ENABLE_PROJECTS=clang to build clang.
    use LLVM_ENABLE_PROJECTS=lld to build lld-link. (https://lld.llvm.org/)
    use CMAKE_BUILD_TYPE=Release to build release version. (https://llvm.org/docs/CMake.html)

3. build the source

Linux OS:

make

Windows OS: (please use release build, the debug build is very slow.)

devenv LLVM.sln /Build Release /Project ALL_BUILD

4. install

a) For Linux OS:

Add binary location to PATH (Assume LLVM_SRC= # modify to the llvm-project git.)

    clang and lld-link are at $LLVM_SRC/build/bin.

export LLVM_SRC=<llvm-project> # modify to the llvm-project git.
export PATH=$LLVM_SRC/build/bin;$PATH

b) For Windows OS:

Add binary location to PATH (Assume LLVM_SRC= @REM modify to the llvm-project git.)

    clang and lld-link are at %LLVM_SRC%\build\Release\bin.

set LLVM_SRC=<llvm-project> @REM modify to the llvm-project git.
set PATH=%LLVM_SRC%\build\Release\bin;%PATH%
