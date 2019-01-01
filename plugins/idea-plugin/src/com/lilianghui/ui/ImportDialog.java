package com.lilianghui.ui;

import com.lilianghui.Context;
import com.lilianghui.action.Clicked;
import com.lilianghui.render.CellRender;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;

public class ImportDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList list1;
    private JTextField textField1;
    private JButton previousButton;
    private Clicked clicked;
    private Context context;

    public ImportDialog(Clicked clicked, Context context) {
        this.clicked = clicked;
        this.context = context;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list1.setCellRenderer(new CellRender());
        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (clicked.mouseClicked(ImportDialog.this, context, 2)) {
                        dispose();
                    }
                }
            }
        });
        textField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                clicked.refreshModel(context, ImportDialog.this, textField1.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                clicked.refreshModel(context, ImportDialog.this, textField1.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                clicked.refreshModel(context, ImportDialog.this, textField1.getText());
            }
        });
        previousButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked.mouseClicked(ImportDialog.this, context, 1);
            }
        });
    }

    private void onOK() {
        // add your code here
        if (clicked.mouseClicked(ImportDialog.this, context, 2)) {
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public JList getList1() {
        return list1;
    }

    public JButton getPreviousButton() {
        return previousButton;
    }

    public JTextField getTextField1() {
        return textField1;
    }
}
