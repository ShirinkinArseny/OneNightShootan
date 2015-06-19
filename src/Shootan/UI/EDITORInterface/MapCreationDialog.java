package Shootan.UI.EDITORInterface;

import javax.swing.*;
import java.awt.event.*;

public class MapCreationDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField mapName;
    private JSpinner widthField;
    private JSpinner heightField;

    private boolean isSuccess=false;

    public String getName() {
        return mapName.getText();
    }

    public int getWidth() {
        return (int) widthField.getValue();
    }

    public int getHeight() {
        return (int) heightField.getValue();
    }

    public MapCreationDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void onOK() {
        isSuccess=true;
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
