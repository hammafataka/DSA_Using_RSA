package com.Dsa;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Random;

public class RSA extends JFrame {
    Main m=new Main();
    int p = m.publictemp,
            q = m.privatetemp,
            n = 0,
            totient = 0,
            numE = 0,
            d = 0;

    String plaintext = "";
    JTextField enterMessage;
    JLabel labelP;
    JLabel labelQ;
    JLabel labelTotient;
    JLabel labelE;
    JLabel labelN;
    JLabel labelD;
    JTextArea encrypted;
    JTextArea decrypted;
    JButton encrypt;
    JButton decrypt;

    public RSA() {


        TitledBorder topBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Enter RSA Values");
        TitledBorder middleBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Encrypt and Decrypt Message");
        TitledBorder bottomBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "RSA Values");

        //ValueActionListener valueListener = new ValueActionListener();
        MessageActionListener messageListener = new MessageActionListener();
        ButtonActionListener buttonListener = new ButtonActionListener();

        setLayout(new BorderLayout(20, 30));

        JPanel Middle = new JPanel();
        Middle.setLayout(new GridLayout(2, 3, 10, 10));
        Middle.setBorder(topBorder);


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

        JPanel Top = new JPanel();
        Top.setLayout(new GridLayout(2, 3, 10, 10));
        Top.setBorder(middleBorder);

        enterMessage = new JTextField();
        JLabel messageLabel = new JLabel("Enter a message");
        encrypted = new JTextArea("Encrypted Message:");
        decrypted = new JTextArea("Decrypted Message:");
        encrypt = new JButton("Encrypt Message");
        decrypt = new JButton("Decrypt Message");


        enterMessage.addActionListener(messageListener);
        encrypt.addActionListener(buttonListener);
        decrypt.addActionListener(buttonListener);

        Top.add(enterMessage);
        Middle.add(encrypted);
        Middle.add(encrypt);
        Top.add(messageLabel);
        Middle.add(decrypted);
        Middle.add(decrypt);

        add(Middle, BorderLayout.CENTER);
        add(Top, BorderLayout.NORTH);
        add(bottom, BorderLayout.WEST);
        p = GenerateRandom();
        q = GenerateRandom();
        while (p == q || p < q) {

            q = GenerateRandom();
        }
        labelP.setText("p:    " + p);
        labelQ.setText("q:    " + q);
        if (p != 0 && q != 0) {
            n = p * q;
            labelN.setText("n:    " + n);
            totient = (p - 1) * (q - 1);
            labelTotient.setText("totient:    " + totient);
        }
        numE = GenerateRandomE(totient);
        labelE.setText("e:    " + numE);
        calcD();


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

    }

    private static boolean isPrime(int inputNum) {
        if (inputNum <= 3 || inputNum % 2 == 0)
            return inputNum != 2 && inputNum != 3;
        int divisor = 3;
        while ((divisor <= Math.sqrt(inputNum)) && (inputNum % divisor != 0))
            divisor += 2;
        return inputNum % divisor == 0;
    }

    private static int GenerateRandom() {
        Random rand = new Random();
        int num = rand.nextInt(1000);
        while (isPrime(num) || num < 10) {
            num = rand.nextInt(1000);
        }
        return num;

    }

    private static int GenerateRandomE(int totient) {
        Random rand = new Random();
        int num = rand.nextInt(400);
        while (isPrime(num) || num < 10 || num % totient == 0) {
            num = rand.nextInt(400);
        }
        return num;

    }

    private class MessageActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            plaintext = enterMessage.getText();

        }
    }

    private class ButtonActionListener implements ActionListener {

        int[] c;

        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == encrypt) {
                plaintext = enterMessage.getText();
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
            } else {
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
        frame.setSize(800, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
