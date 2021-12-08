package snmp2;

import javax.swing.*;

public class FenSNMP4 {
    private SNMP4 snmp;

    public FenSNMP4() {
        initComp();
        snmp = new SNMP4(TFcommunity.getText(), Integer.parseInt(TFtimeout.getText()), Integer.parseInt(TFretries.getText()), TFip.getText());
    }

    private void initComp() {
        Bset.addActionListener(e -> BsetPressed());
        Bget.addActionListener(e -> BgetPressed());
        BgetNext.addActionListener(e -> BgetNextPressed());
        CBAsynchrone.addActionListener(e -> CBAsynchronePressed());
        Bconnexion.addActionListener(e -> BconnexionPressed());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new FenSNMP4().mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void BgetPressed() {
        snmp.setMib(TFmib.getText());
        snmp.get();
    }
    private void BsetPressed() {
        if (!TFvaleur.getText().isEmpty()) {
            snmp.setMib(TFmib.getText());
            snmp.set(TFvaleur.getText());
        }
        else {
            System.out.println("Veuillez entrez une nouvelle valeur !");
        }
    }
    private void BgetNextPressed() {
        if ( snmp.getMib() == null) {
            snmp.setMib(TFmib.getText());
        }
        snmp.getNext();
    }
    private void CBAsynchronePressed() {
        snmp.setAsynchrone(CBAsynchrone.isSelected());
    }
    private void BconnexionPressed() {
        if (ping.main(TFip.getText()) == 1) {
            snmp.setConnect(true);
        }
    }





    //<editor-fold defaultstate="collapsed" desc="Declaration Boutons">
    private JPanel mainPanel;
    private JCheckBox CBAsynchrone;
    private JTextField TFmib;
    private JTextField TFcommunity;
    private JButton Bget;
    private JButton BgetNext;
    private JButton Bset;
    private JLabel Label1;
    private JLabel Label2;
    private JTextField TFtimeout;
    private JTextField TFretries;
    private JLabel Label4;
    private JLabel Label5;
    private JTextField TFvaleur;
    private JLabel Label6;
    private JButton Bconnexion;
    private JTextField TFip;
    private JLabel Label7;
    //</editor-fold>
}
