/** @file
 
 The file is used to create, update Hii Packages section of the MSA file
 
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
import org.tianocore.frameworkwizard.module.Identifications.HiiPackages.HiiPackagesIdentification;

/**
 * The class is used to create, update Hii Packages of the MSA file
 * 
 * It extends IDialog
 * 
 */
public class HiiPackagesDlg extends IDialog {

    // /
    // / Define class Serial Version UID
    // /
    private static final long serialVersionUID = -6851574146786158116L;

    //
    // Define class members
    //
    private JPanel jContentPane = null;

    private JLabel jLabelName = null;

    private JTextField jTextFieldName = null;

    private JLabel jLabelUsage = null;

    private JComboBox jComboBoxUsage = null;

    private StarLabel jStarLabel1 = null;

    private StarLabel jStarLabel2 = null;

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
    private HiiPackagesIdentification id = null;

    private EnumerationData ed = new EnumerationData();

    /**
     * 
     * This method initializes jTextFieldName
     * 
     * @return javax.swing.JTextField jTextFieldName
     * 
     */
    private JTextField getJTextFieldName() {
        if (jTextFieldName == null) {
            jTextFieldName = new JTextField();
            jTextFieldName.setBounds(new java.awt.Rectangle(168, 12, 320, 20));
            jTextFieldName.setPreferredSize(new java.awt.Dimension(320, 20));
            jTextFieldName.setToolTipText("Enter the C Name of the HII Package");
        }
        return jTextFieldName;
    }

    /**
     * 
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
                                          + "<tr><td>ALWAYS_PRODUCED</td><td>Hii is always registered</td></tr>"
                                          + "<tr><td>SOMETIMES_PRODUCED</td><td>Some executions paths will require the Hii to be registered</td></tr>"
                                          + "</table></html>");
        }
        return jComboBoxUsage;
    }

    /**
     * 
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
     * 
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     * 
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJContentPane());
        }
        return jScrollPane;
    }

    /**
     * 
     * This method initializes jTextAreaHelpText
     * 
     * @return javax.swing.JTextArea jTextAreaHelpText
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
     * 
     * This method initializes jScrollPaneHelpText
     * 
     * @return javax.swing.JScrollPane
     * 
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
     * 
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
     *
     * This method initializes jButtonCancel
     * 
     * @return javax.swing.JButton
     * 
     **/
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
     * 
     * This method initializes this
     * 
     **/
    private void init() {
        this.setSize(505, 235);
        this.setContentPane(getJScrollPane());
        this.setTitle("Hii Packages");
        initFrame();
        this.setViewMode(false);
        this.centerWindow();
    }

    /**
     * This method initializes this Fill values to all fields if these values are
     * not empty
     * 
     * @param inHiiPackagesId
     * 
     **/
    private void init(HiiPackagesIdentification inHiiPackagesId) {
        init();
        this.id = inHiiPackagesId;

        if (this.id != null) {
            this.jTextFieldName.setText(id.getName());
            this.jComboBoxUsage.setSelectedItem(id.getUsage());
            this.jTextAreaHelpText.setText(id.getHelp());
            this.jTextFieldFeatureFlag.setText(id.getFeatureFlag());
            this.jArchCheckBox.setSelectedItems(id.getSupArchList());
        }
    }

    /**
     * This is the override edit constructor
     * 
     * @param inHiiPackagesIdentification
     * @param iFrame
     * 
     **/
    public HiiPackagesDlg(HiiPackagesIdentification inHiiPackagesIdentification, IFrame iFrame) {
        super(iFrame, true);
        init(inHiiPackagesIdentification);
    }

    /**
     * 
     * Disable all components when the mode is view
     * 
     * @param isView
     *          true - The view mode; false - The non-view mode
     * 
     **/
    public void setViewMode(boolean isView) {
        if (isView) {
            this.jTextFieldName.setEnabled(!isView);
            this.jComboBoxUsage.setEnabled(!isView);
        }
    }

    /**
     * 
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel jContentPane
     * 
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jStarLabel1 = new StarLabel();
            jStarLabel1.setLocation(new java.awt.Point(2, 12));
            jLabelName = new JLabel();
            jLabelName.setText("Hii Package C Name");
            jLabelName.setBounds(new java.awt.Rectangle(12, 12, 155, 20));
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
            jLabelArch.setBounds(new java.awt.Rectangle(12, 132, 155, 20));
            jLabelArch.setText("Supported Architectures");
            jArchCheckBox = new ArchCheckBox();
            jArchCheckBox.setBounds(new java.awt.Rectangle(168, 132, 320, 20));
            jArchCheckBox.setPreferredSize(new java.awt.Dimension(320, 20));

            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.setPreferredSize(new java.awt.Dimension(480, 180));

            jContentPane.add(jStarLabel1, null);
            jContentPane.add(jLabelName, null);
            jContentPane.add(getJTextFieldName(), null);

            jContentPane.add(jStarLabel2, null);
            jContentPane.add(jLabelUsage, null);
            jContentPane.add(getJComboBoxUsage(), null);

            jContentPane.add(jLabelHelpText, null);
            jContentPane.add(getJScrollPaneHelpText(), null);

            jContentPane.add(jLabelFeatureFlag, null);
            jContentPane.add(getJTextFieldFeatureFlag(), null);

            jContentPane.add(jLabelArch, null);
            jContentPane.add(jArchCheckBox, null);

            jContentPane.add(getJButtonOk(), null);
            jContentPane.add(getJButtonCancel(), null);
        }
        return jContentPane;
    }

    /**
     * 
     * This method initializes Usage type
     * 
     */
    private void initFrame() {
        Tools.generateComboBoxByVector(jComboBoxUsage, ed.getVHiiPackageUsage());
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
                getCurrentHiiPackages();
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
     * 
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
        // Check Hii Package Name
        //
        if (isEmpty(this.jTextFieldName.getText())) {
            Log.wrn("Update Hii Packages", "Hii Package Name Record must not be empty");
            return false;
        }

        if (!isEmpty(this.jTextFieldName.getText())) {
            if (!DataValidation.isC_NameType(this.jTextFieldName.getText())) {
                Log.wrn("Update Hii Packages", "Incorrect data type for Hii Package Name");
                return false;
            }
        }

        //
        // Check FeatureFlag
        //
        if (!isEmpty(this.jTextFieldFeatureFlag.getText())) {
            if (!DataValidation.isFeatureFlag(this.jTextFieldFeatureFlag.getText())) {
                Log.wrn("Update Hii Packages", "Incorrect data type for Feature Flag");
                return false;
            }
        }

        return true;
    }

    private HiiPackagesIdentification getCurrentHiiPackages() {
        String arg0 = this.jTextFieldName.getText();
        String arg1 = this.jComboBoxUsage.getSelectedItem().toString();

        String arg2 = this.jTextFieldFeatureFlag.getText();
        Vector<String> arg3 = this.jArchCheckBox.getSelectedItemsVector();
        String arg4 = this.jTextAreaHelpText.getText();

        id = new HiiPackagesIdentification(arg0, arg1, arg2, arg3, arg4);
        return id;
    }

    public HiiPackagesIdentification getId() {
        return id;
    }

    public void setId(HiiPackagesIdentification id) {
        this.id = id;
    }
}
