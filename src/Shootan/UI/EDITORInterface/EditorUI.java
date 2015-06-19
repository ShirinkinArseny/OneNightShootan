package Shootan.UI.EDITORInterface;

import javax.swing.*;

public class EditorUI extends JFrame {
    private JPanel contentPane;

    private UICanvas uic;

    public EditorUI() {

        try {
            UIManager.setLookAndFeel(com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel.class.getCanonicalName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setContentPane(contentPane);

        uic = new UICanvas();

        contentPane.add(uic);


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1100, 700);

        setResizable(true);

        setVisible(true);


        uic.start();
    }


    public static void main(String[] args) {
        new EditorUI();
    }
}
