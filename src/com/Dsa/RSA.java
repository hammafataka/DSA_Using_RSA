package com.Dsa;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RSA extends JFrame {
    static Main mainpage ;
    int p = 0,
            q =0,
            totient = 0,
            numE = 0,
            d = 0,
            n=0;
    static int[] c;
    String plaintext = "";
    public static StringBuilder ciphertext;
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
    JButton GetPrivateKey;
    JButton GetPublicKey;
    JButton GenerateRandom;
    public static String TransferedData;


    public RSA() {


        TitledBorder topBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Encrypt and Decrypt Message");
        TitledBorder middleBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Enter RSA Values");
        TitledBorder bottomBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "RSA Values");



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
        GetPublicKey=new JButton("Load Public key");
        GetPrivateKey=new JButton("Load Private key");


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
        Top.setLayout(new GridLayout(2, 1, 10, 10));
        Top.setBorder(middleBorder);

        enterMessage = new JTextField();
        JLabel messageLabel = new JLabel("Enter a message in the next Text box ");
        encrypted = new JTextArea("Encrypted Message:");
        decrypted = new JTextArea("Decrypted Message:");
        encrypt = new JButton("Encrypt Message");
        decrypt = new JButton("Decrypt Message");
        GenerateRandom=new JButton("Generate random keys");




        Top.add(messageLabel);
        Top.add(enterMessage);
        Middle.add(encrypted);
        Middle.add(encrypt);
        Top.add(new JLabel("By Default Keys are 0. press Generate to Generate Randomly"));
        Top.add(GetPublicKey);
        Top.add(GetPrivateKey);
        Top.add(GenerateRandom);
        Middle.add(decrypted);
        Middle.add(decrypt);

        add(Middle, BorderLayout.CENTER);
        add(Top, BorderLayout.NORTH);
        add(bottom, BorderLayout.WEST);


        encrypt.addActionListener(e -> {
            plaintext = enterMessage.getText();
            int[] m = new int[plaintext.length()];
            for (int i = 0; i < plaintext.length(); i++) {
                m[i] = plaintext.charAt(i);
            }

            c = new int[plaintext.length()];

            BigInteger nBig = new BigInteger(n+"");

                ciphertext = new StringBuilder();
            for (int i = 0; i < m.length; i++) {
                BigInteger a = new BigInteger(m[i] + "");
                BigInteger b = a.pow(d);
                b = b.mod(nBig);
                c[i] = b.intValue();
                ciphertext.append(c[i]);
            }
            encrypted.setText("Encrypted Message: " + ciphertext);
            int result=JOptionPane.showConfirmDialog(null,"Do you want save the file?","Save",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(result==JOptionPane.YES_OPTION){
                mainpage.CreateFile(ciphertext.toString(),"file","sign");
                mainpage.CreateFile(TransferedData,"data","txt");
                int con=JOptionPane.showConfirmDialog(null,"Do you want to close here ?","Close",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(con==JOptionPane.YES_OPTION){
                    this.dispose();

                }

            }
        });


        decrypt.addActionListener(e -> {
            BigInteger nBig = new BigInteger(n + "");
            StringBuilder decryptedC = new StringBuilder();

            for (int j : c) {
                BigInteger a = new BigInteger(j + "");
                BigInteger b =a.pow(numE);
                b = b.mod(nBig);
                decryptedC.append((char) b.intValue());
            }
            decrypted.setText("Decrypted Message: " + decryptedC);
        });




        GetPublicKey.addActionListener(e -> {
            File Temp= mainpage.GetSelectedFile();
            String milad="";
            try {
                milad =readFileAsString(Temp.getAbsolutePath());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            milad = milad.replaceAll("[^-?0-9]+", " ");
            List<String> nums= Arrays.asList(milad.trim().split(" "));
            String[] itemsArray = new String[nums.size()];
            itemsArray = nums.toArray(itemsArray);
            int size = itemsArray.length;
            int [] arr = new int [size];
            for(int i=0; i<size; i++) {
                arr[i] = Integer.parseInt(itemsArray[i]);
            }
            numE=arr[0];
            n=arr[1];



        });
        GetPrivateKey.addActionListener(e -> {

            File Temp= mainpage.GetSelectedFile();
            String milad="";
            try {
                 milad =readFileAsString(Temp.getAbsolutePath());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            milad = milad.replaceAll("[^-?0-9]+", " ");
            List<String> nums= Arrays.asList(milad.trim().split(" "));
            String[] itemsArray = new String[nums.size()];
            itemsArray = nums.toArray(itemsArray);
            int size = itemsArray.length;
            int [] arr = new int [size];
            for(int i=0; i<size; i++) {
                arr[i] = Integer.parseInt(itemsArray[i]);
            }
            d=arr[0];
            n=arr[1];

        });
        GenerateRandom.addActionListener(e -> {
            p = GenerateRandom();
            q = GenerateRandom();
            getkeys(p,q);
        });


    }
    public static String readFileAsString(String fileName)throws Exception
    {
        var data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }
    private void getkeys(int p,int q){

        while (p==q || p < q) {

            q = GenerateRandom();
        }
        labelP.setText("p:    " + p);
        labelQ.setText("q:    " + q);
        if (p != 0 && q != 0) {
            n= p*q;

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
        int num = rand.nextInt(999-500)+000;
        while (isPrime(num)) {
            num = rand.nextInt(999-500)+500;
        }
        return num;


    }

    private static int GenerateRandomE(int totient) {
        Random rand = new Random();
        int num = rand.nextInt(799-500)+500;
        while (isPrime(num) || num % totient == 0) {
            num = rand.nextInt(799-500)+500;
        }
        return num;

    }



    public static void main(String[] args) {
        RSA frame = new RSA();
        frame.setTitle("RSA Encryption");
        frame.setSize(1100, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
