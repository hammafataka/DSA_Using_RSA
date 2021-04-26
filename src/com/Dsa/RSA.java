package com.Dsa;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class RSA extends JFrame  {
    int p = 0,
            q = 0,
            n = 0,
            totient = 0,
            numE = 0,
            d = 0;

    String plaintext = "";
    JTextField enterP;
    JTextField enterQ;
    JTextField enterE;
    JTextField enterMessage;
    JLabel labelP;
    JLabel labelQ;
    JLabel labelTotient;
    JLabel labelE;
    JLabel labelN;
    JLabel labelD;
    JLabel enterLabelE;
    JLabel encrypted;
    JLabel decrypted;
    JButton encrypt;
    JButton decrypt;
    boolean[] primes = new boolean[100];

    public RSA() {


        int[] tempPrimes ={2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};
        for (int tempPrime : tempPrimes) {
            primes[tempPrime] = true;
        }
        TitledBorder topBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Enter RSA Values");
        TitledBorder middleBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Encypt and Decrypt Message");
        TitledBorder bottomBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "RSA Values");

        ValueActionListener valueListener = new ValueActionListener();
        MessageActionListener messageListener = new MessageActionListener();
        ButtonActionListener buttonListener = new ButtonActionListener();

        setLayout(new BorderLayout(20, 30));

        JPanel top = new JPanel();
        top.setLayout(new GridLayout(2, 3, 10, 10));
        top.setBorder(topBorder);

        JLabel enterLabelP = new JLabel("Enter a prime below 100");
        enterP = new JTextField();
        JLabel enterLabelQ = new JLabel("Enter a prime below 100");
        enterQ = new JTextField();
        enterLabelE = new JLabel("Enter a prime below 100. Not a divisor of " + totient);
        enterE = new JTextField();
        //disable text field for e at start
        enterE.setEnabled(false);

        enterP.addActionListener(valueListener);
        enterE.addActionListener(valueListener);
        enterQ.addActionListener(valueListener);

        top.add(enterP);
        top.add(enterQ);
        top.add(enterE);
        top.add(enterLabelP);
        top.add(enterLabelQ);
        top.add(enterLabelE);

        JPanel bottom = new JPanel();
        bottom.setLayout(new GridLayout(6, 2, 10, 10));
        bottom.setBorder(bottomBorder);

        labelP = new JLabel("p:    " + p);
        labelQ = new JLabel("q:    " + q);
        labelTotient = new JLabel("totient:    " + totient);
        JLabel publicKey = new JLabel("Public Key");
        labelE = new JLabel("e:    " + numE);
        labelN = new JLabel("n:    " + n);
        JLabel privateKey = new JLabel("Private Key");
        labelD = new JLabel("d:    " + d);
        JLabel spacer1 = new JLabel();
        JLabel spacer2 = new JLabel();
        JLabel spacer3 = new JLabel();

        bottom.add(labelP);
        bottom.add(labelQ);
        bottom.add(labelTotient);
        bottom.add(spacer1);
        bottom.add(publicKey);
        bottom.add(spacer2);
        bottom.add(labelE);
        bottom.add(labelN);
        bottom.add(privateKey);
        bottom.add(spacer3);
        bottom.add(labelD);

        JPanel middle = new JPanel();
        middle.setLayout(new GridLayout(2, 3, 10, 10));
        middle.setBorder(middleBorder);

        enterMessage = new JTextField();
        JLabel messageLabel = new JLabel("Enter a message");
        encrypted = new JLabel("Encrypted Message:");
        decrypted = new JLabel("Decrypted Message:");
        encrypt = new JButton("Encrypt Message");
        decrypt = new JButton("Decrypt Message");

        enterMessage.setEnabled(false);
        encrypt.setEnabled(false);
        decrypt.setEnabled(false);

        enterMessage.addActionListener(messageListener);
        encrypt.addActionListener(buttonListener);
        decrypt.addActionListener(buttonListener);

        middle.add(enterMessage);
        middle.add(encrypted);
        middle.add(encrypt);
        middle.add(messageLabel);
        middle.add(decrypted);
        middle.add(decrypt);

        add(top, BorderLayout.NORTH);
        add(middle, BorderLayout.CENTER);
        add(bottom, BorderLayout.WEST);

    }

    private class ValueActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int temp;
            if (event.getSource() == enterP) {
                temp = Integer.parseInt(enterP.getText());
                if (isPrime(temp)) {
                    p = temp;
                    labelP.setText("p:    " + p);
                }
                enterP.setText(null);

            }
            else if (event.getSource() == enterQ) {
                temp = Integer.parseInt(enterQ.getText());
                if (isPrime(temp)) {
                    q = temp;
                    labelQ.setText("q:    " + q);
                }
                enterQ.setText(null);

            }
            else {
                temp = Integer.parseInt(enterE.getText());
                if (!isPrime(temp)) {
                    temp = 0;
                }
                if (gcd(temp, totient) != 1) {
                    JOptionPane.showMessageDialog(null, "The number was a divisor of " + totient, "Error",
                            JOptionPane.PLAIN_MESSAGE);
                    temp = 0;
                }
                if (temp != 0) {
                    numE = temp;
                    labelE.setText("e:    " + numE);
                }
                enterE.setText(null);
                calcD();
            }
            if (p != 0 && q != 0) {
                enterE.setEnabled(true);
                n = p * q;
                labelN.setText("n:    " + n);
                totient = (p - 1) * (q - 1);
                labelTotient.setText("totient:    " + totient);
                enterLabelE.setText("Enter a prime below 100. Not a divisor of " + totient);
            }
        }

        private boolean isPrime(int prime) {
            for (int i = 0; i < 100; i++) {
                if (primes[i] && prime == i) {
                    return true;
                }
            }
            JOptionPane.showMessageDialog(null, "The number was not prime, or higher than 100", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        }

        private int gcd(int a, int b) {
            if (b == 0) {
                return a;
            }
            return gcd(b, a % b);
        }

        private void calcD() {
            int s = 0,
                    t = 1,
                    olds = 1,
                    oldt = 0,
                    r = numE,
                    oldr = totient;
            while (r != 1) {
                int quotient = oldr / r;
                int temp = r;
                r = oldr % r;
                oldr = temp;
                temp = s;
                s = olds - quotient * s;
                olds = temp;

                temp = t;
                t = oldt - quotient * t;
                oldt = temp;
            }

            if (t < 0) {
                d = totient + t;
            } else {
                d = t;
            }
            labelD.setText("d:    " + d);
            enterMessage.setEnabled(true);
        }
    }

    private class MessageActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            plaintext = enterMessage.getText();
            encrypt.setEnabled(true);

        }
    }

    private class ButtonActionListener implements ActionListener {

        int[] c;

        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == encrypt) {
                plaintext=enterMessage.getText();
                int[] m = new int[plaintext.length()];
                for (int i = 0; i < plaintext.length(); i++) {
                    m[i] = plaintext.charAt(i);
                }

                c = new int[plaintext.length()];

                BigInteger nBig = new BigInteger(n + "");

                StringBuilder ciphertext = new StringBuilder();
                for (int i = 0; i < m.length; i++) {
                    BigInteger a = new BigInteger(m[i] + "");
                    BigInteger b = a.pow(numE);
                    b = b.mod(nBig);
                    c[i] = b.intValue();
                    ciphertext.append(c[i]);
                }
                encrypted.setText("Encrypted Message: " + ciphertext);
                decrypt.setEnabled(true);
            }
            else {
                BigInteger nBig = new BigInteger(n + "");
                StringBuilder decryptedC = new StringBuilder();

                for (int j : c) {
                    BigInteger a = new BigInteger(j + "");
                    BigInteger b = a.pow(d);
                    b = b.mod(nBig);
                    decryptedC.append((char) b.intValue());
                }
                decrypted.setText("Decrypted Message: " + decryptedC);
            }
        }
    }
    public static void main(String[] args) {
        RSA frame = new RSA();
        frame.setTitle("RSA Encryption");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
