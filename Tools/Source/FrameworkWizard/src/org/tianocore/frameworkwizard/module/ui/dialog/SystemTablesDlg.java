/** @file
 <<The file is used to create, update SystemTable in the MSA file>>
 
 <<This dialog is used to add or edit a System Table entry in the MSA file.>>
 
 Copyright (c) 2006, Intel Corporation
 All rights reserved. This program and the accompanying materials
 are licensed and made available under the terms and conditions of the BSD License
 which accompanies this distribution.  The full text of the license may be found at
 http://opensource.org/licenses/bsd-license.php
 
 THE PROGRAM IS DISTRIBUTED UNDER THE BSD LICENSE ON AN "AS IS" BASIS,
 WITHOUT WARRANTIES OR REPRESENTATIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED.
 
 **/
package org.tianocore.frameworkwizard.module.ui.dialog;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import org.tianocore.frameworkwizard.common.DataType;
import org.tianocore.frameworkwizard.common.DataValidation;
import org.tianocore.frameworkwizard.common.EnumerationData;
import org.tianocore.frameworkwizard.common.Log;
import org.tianocore.frameworkwizard.common.Tools;
import org.tianocore.frameworkwizard.common.ui.ArchCheckBox;
import org.tianocore.frameworkwizard.common.ui.IDialog;
import org.tianocore.frameworkwizard.common.ui.IFrame;
import org.tianocore.frameworkwizard.common.ui.StarLabel;
import org.tianocore.frameworkwizard.module.Identifications.SystemTables.SystemTablesIdentification;
import org.tianocore.frameworkwizard.workspace.WorkspaceTools;

/**
 * The class is used to create, update SystemTables section of the MSA file
 * 
 * It extends IDialog
 * 
 */
public class SystemTablesDlg extends IDialog {

    // /
    // / Define class Serial Version UID
    // /
    private static final long serialVersionUID = 7488769180379442276L;

    //
    // Define class members
    //
    private JPanel jContentPane = null;

    private JLabel jLabelEntry = null;

    private JLabel jLabelUsage = null;

    private JComboBox jComboBoxUsage = null;

    private StarLabel jStarLabel1 = null;

    private StarLabel jStarLabel2 = null;

    private JComboBox jComboBoxGuidC_Name = null;

    private JLabel jLabelFeatureFlag = null;

    private JTextField jTextFieldFeatureFlag = null;

    private JLabel jLabelArch = null;

    private JScrollPane jScrollPane = null;

    private JLabel jLabelHelpText = null;

    private JTextArea jTextAreaHelpText = null;

    private JScrollPane jScrollPaneHelpText = null;

    private ArchCheckBox jArchCheckBox = null;

    private JButton jButtonOk = null;

    private JButton jButtonCancel = null;

    //
    // Not used by UI
    //
    private SystemTablesIdentification id = null;

    private EnumerationData ed = new EnumerationData();

    private WorkspaceTools wt = new WorkspaceTools();

    /**
     * This method initializes jComboBoxGuidC_Name
     * 
     * @return javax.swing.JComboBox jComboBoxGuidC_Name
     * 
     */
    private JComboBox getJComboBoxGuidC_Name() {
        if (jComboBoxGuidC_Name == null) {
            jComboBoxGuidC_Name = new JComboBox();
            jComboBoxGuidC_Name.setBounds(new java.awt.Rectangle(168, 12, 320, 20));
            jComboBoxGuidC_Name.setPreferredSize(new java.awt.Dimension(320, 20));
            jComboBoxGuidC_Name.setToolTipText("Select the GUID C Name of the System Table");
        }
        return jComboBoxGuidC_Name;
    }

    /**
     * This method initializes jComboBoxUsage
     * 
     * @return javax.swing.JComboBox jComboBoxUsage
     * 
     */
    private JComboBox getJComboBoxUsage() {
        if (jComboBoxUsage == null) {
            jComboBoxUsage = new JComboBox();
            jComboBoxUsage.setBounds(new java.awt.Rectangle(168, 37, 320, 20));
            jComboBoxUsage.setPreferredSize(new java.awt.Dimension(320, 20));
            jComboBoxUsage
                          .setToolTipText("<html><table>"
                                          + "<tr><td>ALWAYS_CONSUMED</td><td>Module requires a GUIDed entry in the system table</td></tr>"
                                          + "<tr><td>SOMETIMES_CONSUMED</td><td>Module consumes a GUIDed entry in the system<br>table if it is present</td>"
                                          + "</tr><tr><td>ALWAYS_PRODUCED</td><td>Module always produces a GUIDed entry in the system table</td></tr>"
                                          + "<tr><td>SOMETIMES_PRODUCED</td><td>Module produces a GUIDed entry in the system table<br>for some of its execution flows.</td></tr>"
                                          + "</table></html>");
        }
        return jComboBoxUsage;
    }

    /**
     * This method initializes jTextFieldFeatureFlag
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldFeatureFlag() {
        if (jTextFieldFeatureFlag == null) {
            jTextFieldFeatureFlag = new JTextField();
            jTextFieldFeatureFlag.setBounds(new java.awt.Rectangle(168, 107, 320, 20));
            jTextFieldFeatureFlag.setPreferredSize(new java.awt.Dimension(320, 20));
            jTextFieldFeatureFlag.setToolTipText("Postfix expression that must evaluate to TRUE or FALSE");
        }
        return jTextFieldFeatureFlag;
    }

    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJContentPane());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jTextAreaHelpText
     * 
     * @return javax.swing.JTextArea
     * 
     */
    private JTextArea getJTextAreaHelpText() {
        if (jTextAreaHelpText == null) {
            jTextAreaHelpText = new JTextArea();
            jTextAreaHelpText.setLineWrap(true);
            jTextAreaHelpText.setWrapStyleWord(true);
        }
        return jTextAreaHelpText;
    }

    /**
     * This method initializes jScrollPaneHelpText
     * 
     * @returns javax.swing.JScrollPane jScrollPaneHelpText
     */
    private JScrollPane getJScrollPaneHelpText() {
        if (jScrollPaneHelpText == null) {
            jScrollPaneHelpText = new JScrollPane();
            jScrollPaneHelpText.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPaneHelpText.setSize(new java.awt.Dimension(320, 40));
            jScrollPaneHelpText.setPreferredSize(new java.awt.Dimension(320, 40));
            jScrollPaneHelpText.setLocation(new java.awt.Point(168, 62));
            jScrollPaneHelpText.setViewportView(getJTextAreaHelpText());
        }
        return jScrollPaneHelpText;
    }

    /**
     * This method initializes jButtonOk
     * 
     * @return javax.swing.JButton
     * 
     */
    private JButton getJButtonOk() {
        if (jButtonOk == null) {
            jButtonOk = new JButton();
            jButtonOk.setBounds(new java.awt.Rectangle(290, 162, 90, 20));
            jButtonOk.setText("Ok");
            jButtonOk.addActionListener(this);
        }
        return jButtonOk;
    }

    /**
     * This method initializes jButtonCancel
     * 
     * @return javax.swing.JButton
     * 
     */
    private JButton getJButtonCancel() {
        if (jButtonCancel == null) {
            jButtonCancel = new JButton();
            jButtonCancel.setBounds(new java.awt.Rectangle(390, 162, 90, 20));
            jButtonCancel.setText("Cancel");
            jButtonCancel.addActionListener(this);
        }
        return jButtonCancel;
    }

    public static void main(String[] args) {

    }

    /**
     * This method initializes this
     * 
     */
    private void init() {
        this.setSize(505, 235);
        this.setContentPane(getJScrollPane());
        this.setTitle("System Tables");
        initFrame();
        this.setViewMode(false);
        this.centerWindow();
    }

    /**
     * This method initializes this Fill values to all fields if these values are
     * not empty
     * 
     * @param inSystemTablesId
     * 
     */
    private void init(SystemTablesIdentification inSystemTablesId) {
        init();
        this.id = inSystemTablesId;

        if (this.id != null) {
            this.jComboBoxGuidC_Name.setSelectedItem(id.getName());
            this.jComboBoxUsage.setSelectedItem(id.getUsage());
            this.jTextAreaHelpText.setText(id.getHelp());
            this.jTextFieldFeatureFlag.setText(id.getFeatureFlag());
            this.jArchCheckBox.setSelectedItems(id.getSupArchList());
        }
    }

    /**
     * This is the override edit constructor
     * 
     * @param inBootModesIdentification
     * @param iFrame
     * 
     */
    public SystemTablesDlg(SystemTablesIdentification inSystemTablesIdentification, IFrame iFrame) {
        super(iFrame, true);
        init(inSystemTablesIdentification);
    }

    /**
     * Disable all components when the mode is view
     * 
     * @param isView
     *          true - The view mode; false - The non-view mode
     * 
     */
    public void setViewMode(boolean isView) {
        if (isView) {
            this.jComboBoxUsage.setEnabled(!isView);
        }
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel jContentPane
     * 
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jStarLabel1 = new StarLabel();
            jStarLabel1.setLocation(new java.awt.Point(2, 12));
            jLabelEntry = new JLabel();
            jLabelEntry.setText("Table's GUID C Name");
            jLabelEntry.setBounds(new java.awt.Rectangle(12, 12, 155, 20));
            jStarLabel2 = new StarLabel();
            jStarLabel2.setLocation(new java.awt.Point(2, 37));
            jLabelUsage = new JLabel();
            jLabelUsage.setText("Usage");
            jLabelUsage.setBounds(new java.awt.Rectangle(12, 37, 155, 20));
            jLabelHelpText = new JLabel();
            jLabelHelpText.setBounds(new java.awt.Rectangle(12, 62, 155, 20));
            jLabelHelpText.setText("Help Text");
            jLabelFeatureFlag = new JLabel();
            jLabelFeatureFlag.setBounds(new java.awt.Rectangle(12, 107, 155, 20));
            jLabelFeatureFlag.setText("Feature Flag Expression");
            jLabelArch = new JLabel();
            jLabelArch.setBounds(new java.awt.Rectangle(12, 130, 155, 20));
            jLabelArch.setText("Supported Architectures");
            jArchCheckBox = new ArchCheckBox();
            jArchCheckBox.setBounds(new java.awt.Rectangle(168, 130, 320, 20));
            jArchCheckBox.setPreferredSize(new java.awt.Dimension(320, 20));

            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.setPreferredSize(new java.awt.Dimension(480, 180));

            jContentPane.add(jLabelEntry, null);
            jContentPane.add(jLabelFeatureFlag, null);
            jContentPane.add(getJTextFieldFeatureFlag(), null);
            jContentPane.add(getJComboBoxGuidC_Name(), null);
            jContentPane.add(jLabelArch, null);
            jContentPane.add(jLabelUsage, null);
            jContentPane.add(getJComboBoxUsage(), null);

            jContentPane.add(jStarLabel1, null);
            jContentPane.add(jStarLabel2, null);

            jContentPane.add(jLabelHelpText, null);
            jContentPane.add(getJScrollPaneHelpText(), null);
            jContentPane.add(jArchCheckBox, null);
            jContentPane.add(getJButtonOk(), null);
            jContentPane.add(getJButtonCancel(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes Usage type
     * 
     */
    private void initFrame() {
        Tools.generateComboBoxByVector(jComboBoxUsage, ed.getVSystemTableUsage());
        Tools.generateComboBoxByVector(jComboBoxGuidC_Name, wt.getAllGuidDeclarationsFromWorkspace());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     * 
     * Override actionPerformed to listen all actions
     * 
     */
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == jButtonOk) {
            if (checkAdd()) {
                getCurrentSystemTables();
                this.returnType = DataType.RETURN_TYPE_OK;
                this.setVisible(false);
            }
        }

        if (arg0.getSource() == jButtonCancel) {
            this.returnType = DataType.RETURN_TYPE_CANCEL;
            this.setVisible(false);
        }
    }

    /**
     * Data validation for all fields
     * 
     * @retval true - All datas are valid
     * @retval false - At least one data is invalid
     * 
     */
    public boolean checkAdd() {
        //
        // Check if all fields have correct data types
        //

        //
        // Check FeatureFlag
        //
        if (!isEmpty(this.jTextFieldFeatureFlag.getText())) {
            if (!DataValidation.isFeatureFlag(this.jTextFieldFeatureFlag.getText())) {
                Log.wrn("Update System Tables", "Incorrect data type for Feature Flag");
                return false;
            }
        }

        return true;
    }

    private SystemTablesIdentification getCurrentSystemTables() {
        String arg0 = this.jComboBoxGuidC_Name.getSelectedItem().toString();
        String arg1 = this.jComboBoxUsage.getSelectedItem().toString();

        String arg2 = this.jTextFieldFeatureFlag.getText();
        Vector<String> arg3 = this.jArchCheckBox.getSelectedItemsVector();
        String arg4 = this.jTextAreaHelpText.getText();

        id = new SystemTablesIdentification(arg0, arg1, arg2, arg3, arg4);
        return id;
    }

    public SystemTablesIdentification getId() {
        return id;
    }

    public void setId(SystemTablesIdentification id) {
        this.id = id;
    }
}
