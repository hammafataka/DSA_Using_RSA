package com.Dsa;



import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.logging.Logger;


public class  Main extends JFrame {

    private final Logger log = Logger.getLogger(Main.class.getName());

    JTextField filePath;
    String absolutePath;
    String Hashed;
    File pathFile;
    JLabel fileLbl;
    JButton importBtn;
    String toReturn;
    Integer pb;
    Integer pr;
    JLabel Name;
    JLabel TypeOfFile;
    JLabel Location;
    JLabel size;
    JLabel Created;
    JLabel Modified;
    public int publictemp;
    public int privatetemp;

    JLabel GenerateKeys;
    JButton Generate;
    JLabel PublicKey;
    JLabel PrivateKey;
    JTextField pField;
    JTextField qField;
    JButton pSave;
    JButton pLoad;
    JButton qSave;
    JButton qLoad;
    JButton signature;


    public Main() {
        TitledBorder FileDetailsBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Import File");
        TitledBorder FileDescriptionBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "File Details");
        TitledBorder KeyDetailsBorders = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Keys Details");
        setLayout(new BorderLayout(20, 30));


        JPanel FileDetails = new JPanel();
        FileDetails.setLayout(new GridLayout(1, 2, 10, 10));
        FileDetails.setBorder(FileDetailsBorder);

        filePath = new JTextField();
        fileLbl = new JLabel("Enter File Path");
        importBtn = new JButton("Browse");
        FileDetails.add(fileLbl);
        FileDetails.add(filePath);
        FileDetails.add(importBtn);

        importBtn.addActionListener(e -> {
                    try {
                        JFileChooser file = new JFileChooser();
                        file.setCurrentDirectory(new File(System.getProperty("user.home")));
                        FileNameExtensionFilter filter = new FileNameExtensionFilter("DSA", ".txt");
                        file.addChoosableFileFilter(filter);
                        int result = file.showSaveDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = file.getSelectedFile();
                            absolutePath = selectedFile.getAbsolutePath();
                            pathFile = new File(absolutePath);
                            filePath.setText(absolutePath);
                            String fileName = pathFile.getName();

                            toReturn = readFileAsString(absolutePath);
                            Name.setText("Name: "+fileName);
                            TypeOfFile.setText("Type Of File: .txt");
                            Location.setText("Location: "+absolutePath);
                            double dsize=(double) pathFile.length()   ;
                            DecimalFormat df=new DecimalFormat();
                            df.setMaximumFractionDigits(2);
                            try{
                               Path p=pathFile.toPath();
                                FileTime createdtime=(FileTime) Files.getAttribute(p,"creationTime");

                                Created.setText("Created: "+createdtime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                            }
                            catch (IOException ignored) {

                            }
                            String filesize=df.format(dsize)+" Bytes";
                            size.setText("Size: "+filesize);
                            Hashed=hashing(toReturn);
                            DateFormat sdf=new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
                            Modified.setText("Modified: "+sdf.format(pathFile.lastModified()));
                           JOptionPane.showMessageDialog(null, Hashed);

                        } else if (result == JFileChooser.CANCEL_OPTION) {
                            JOptionPane.showMessageDialog(null, "No File Selected", "Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception exception) {
                        log.info(exception.getMessage());
                    }


                });

        JPanel FileDescriptions = new JPanel();
        FileDescriptions.setLayout(new GridLayout(7, 2, 10, 10));
        FileDescriptions.setBorder(FileDescriptionBorder);
        Name = new JLabel("Name:");
        TypeOfFile = new JLabel("Type Of File:");
        Location = new JLabel("Location:");
        size = new JLabel("Size:");
        Created = new JLabel("Created:");
        Modified = new JLabel("Modified");

        FileDescriptions.add(Name);
        FileDescriptions.add(TypeOfFile);
        FileDescriptions.add(Location);
        FileDescriptions.add(size);
        FileDescriptions.add(Created);
        FileDescriptions.add(Modified);

        JPanel KeyDetails = new JPanel();
        KeyDetails.setLayout(new GridLayout(6, 2, 10, 10));
        KeyDetails.setBorder(KeyDetailsBorders);

        GenerateKeys = new JLabel("Generate a Public/Private Key Pair");
        Generate = new JButton("Generate");
        Generate.addActionListener(e -> {
            publictemp=GenerateRandom();
            privatetemp=GenerateRandom();
            while (publictemp==privatetemp||publictemp<privatetemp){

                privatetemp=GenerateRandom();


            }

            File tempfile=selectfile();
            GenerateFile(tempfile,publictemp);


            RSA r=new RSA();
            r.labelP.setText(Integer.toString(publictemp));
            r.labelQ.setText(Integer.toString(privatetemp));
            r.setSize(800, 350);
            r.setVisible(rootPaneCheckingEnabled);
            r.enterMessage.setText(Hashed);


        });
        PublicKey = new JLabel("Public Key");
        PrivateKey = new JLabel("Private Key");
        pField = new JTextField();
        qField = new JTextField();
        pSave = new JButton("Save");
        pLoad = new JButton("Load");
        qSave = new JButton("Save");
        qLoad = new JButton("Load");
        signature = new JButton("Create Signature");

        KeyDetails.add(GenerateKeys);
        KeyDetails.add(Generate);
        KeyDetails.add(PublicKey);
        KeyDetails.add(PrivateKey);
        KeyDetails.add(pField);
        KeyDetails.add(qField);
        KeyDetails.add(pSave);
        KeyDetails.add(qSave);

        KeyDetails.add(pLoad);
        KeyDetails.add(qLoad);

        add(FileDetails, BorderLayout.NORTH);
        add(FileDescriptions, BorderLayout.CENTER);
        add(KeyDetails, BorderLayout.SOUTH);


    }
    private static void GenerateFile(File file,int toWrite){

        try{
            BufferedWriter bd=new BufferedWriter(new FileWriter(file));
            bd.write(String.format("%d",toWrite));
            bd.close();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }

    }
    private static File  selectfile(){
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("DSA", ".txt");
        file.addChoosableFileFilter(filter);
        file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = 0;//file.showSaveDialog(null);
        File tempfile=new File("");
        if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile=file.getSelectedFile();
            String stringpath=selectedFile.getAbsolutePath();
            Path path=Paths.get(stringpath);
             tempfile=new File(String.format("%s//public.txt",path));





        } else if (result == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "No File Selected", "Failed", JOptionPane.ERROR_MESSAGE);
        }
        return tempfile;
    }
    private static int GenerateRandom(){
        Random rand=new Random();
        int num=rand.nextInt(400);
        while (!isPrime(num) ||num<10){
            num=rand.nextInt(400);
        }
        return num;

    }

    public static String hashing  (String input ) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA3-512");
        final byte[] hashbytes = digest.digest(
                input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashbytes);
    }
    public static String readFileAsString(String fileName)throws Exception
    {
        var data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    private static boolean isPrime(int inputNum){
        if (inputNum <= 3 || inputNum % 2 == 0)
            return inputNum == 2 || inputNum == 3;
        int divisor = 3;
        while ((divisor <= Math.sqrt(inputNum)) && (inputNum % divisor != 0))
            divisor += 2;
        return inputNum % divisor != 0;
    }

    public static void main(String[] args){
        Main frame = new Main();
        frame.setTitle("DSA");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();


    }

}

