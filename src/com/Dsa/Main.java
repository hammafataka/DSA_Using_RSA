package com.Dsa;



import javax.swing.*;
import javax.swing.border.TitledBorder;
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


public class  Main extends JFrame {

    Receiver re;
    RSA r;
    int p = 0,
            q =0,
            totient = 0,
            numE = 0,
            d = 0,
            n=0;
    static int keynum;
    static JTextField filePath;
    static String Hashed;
    File SelectedFile;
    JLabel fileLbl;
    JButton importBtn;
    static String toReturn;
    static JLabel Name;
    static JLabel TypeOfFile;
    static JLabel Location;
    static JLabel size;
    static JLabel Created;
    static JLabel Modified;
    public Integer publictemp;
    public Integer privatetemp;

    JLabel GenerateKeys;
    JButton Generate;
    JLabel PublicKey;
    JLabel PrivateKey;
    JTextField pField;
    JTextField qField;
    JButton Hash;
    JButton pLoad;
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
        Generate = new JButton("Generate random keys and sign file");
        PublicKey = new JLabel("Public Key");
        PrivateKey = new JLabel("Private Key");
        pField = new JTextField();
        qField = new JTextField();
        Hash = new JButton("Sign File");
        pLoad = new JButton("Save Entered key");
        qLoad = new JButton("Save Entered key");
        signature = new JButton("Create Signature");

        KeyDetails.add(GenerateKeys);
        KeyDetails.add(Generate);
        KeyDetails.add(PublicKey);
        KeyDetails.add(PrivateKey);
        KeyDetails.add(pField);
        KeyDetails.add(qField);
        KeyDetails.add(Hash);
        KeyDetails.add(pLoad);
        KeyDetails.add(qLoad);

        add(FileDetails, BorderLayout.NORTH);
        add(FileDescriptions, BorderLayout.CENTER);
        add(KeyDetails, BorderLayout.SOUTH);


        re=new Receiver();
        re.setSize(800, 350);
        re.setVisible(rootPaneCheckingEnabled);
        Generate.addActionListener(e -> {
            p = GenerateRandom();
            q = GenerateRandom();
            getkeys(p,q);
            String hamma =numE+","+n;
            CreateFile(hamma,"private","priv");
            String pu=d+","+n;
            boolean confirm=CreateFile(pu,"public","publ");


        });

        importBtn.addActionListener(e -> {
            SelectedFile = GetSelectedFile();
            setFileDetails(SelectedFile);
        });
        pLoad.addActionListener(e -> {
            CreateFile(pField.getText(),"public","publ");

        });
        qLoad.addActionListener(e -> {
            CreateFile(qField.getText(),"private","priv");

        });
        Hash.addActionListener(e -> {
            if(keynum>=2){
                int con=JOptionPane.showConfirmDialog(null,"Do you want to close here?","Close",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(con==JOptionPane.YES_OPTION){
                    this.dispose();
                }
                r=new RSA();
                r.setSize(800, 350);
                r.setVisible(rootPaneCheckingEnabled);
                r.enterMessage.setText(Hashed);
                r.TransferedData=Hashed;
            }
            else {
                JOptionPane.showMessageDialog(null,"Please first save key or generate randomly","Error",JOptionPane.ERROR_MESSAGE);
            }

        });
    }


    public static File GetSelectedFile(){
        JFileChooser file=new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result=file.showSaveDialog(null);
        File selectedFile=new File("");
        if(result==JFileChooser.APPROVE_OPTION){
            selectedFile=file.getSelectedFile();

        }
        else if (result == JFileChooser.CANCEL_OPTION){
            JOptionPane.showMessageDialog(null, "No File Selected", "Failed", JOptionPane.ERROR_MESSAGE);
        }
        return selectedFile;

    }
    private void getkeys(int p,int q){

        while (p==q || p < q) {

            q = GenerateRandom();
        }
        if (p != 0 && q != 0) {
            n= p*q;
            totient = (p - 1) * (q - 1);
        }
        numE = GenerateRandomE(totient);
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


    }
    public static String setFileDetails(File file){
        try{
            filePath.setText(file.getAbsolutePath());
            toReturn = readFileAsString(file.getAbsolutePath());
            Name.setText("Name: "+file.getName());
            TypeOfFile.setText("Type Of File: .txt");
            Location.setText("Location: "+file.getAbsolutePath());
            double dsize=(double) file.length()   ;
            DecimalFormat df=new DecimalFormat();
            df.setMaximumFractionDigits(2);
            try{
                FileTime createdtime=(FileTime)Files.getAttribute(file.toPath(),"creationTime");
                 Created.setText("Created: "+createdtime.toInstant().atZone(ZoneId.systemDefault()).
                         format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            }
            catch (IOException e) {
                return "";
            }
            String filesize=df.format(dsize)+" Bytes";
            size.setText("Size: "+filesize);
            Hashed=hashing(toReturn);
            DateFormat sdf=new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
            Modified.setText("Modified: "+sdf.format(file.lastModified()));
            //JOptionPane.showMessageDialog(null, Hashed);
            return toReturn;
        }
        catch (Exception ignored){

        }

        return  toReturn;
    }
    public static boolean CreateFile(String toWrite,String filename,String extension){
        keynum++;
        boolean success=false;
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));
        file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File tempfile;
        if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile=file.getSelectedFile();
            Path path=Paths.get(selectedFile.getAbsolutePath());
             tempfile=new File(String.format("%s//%s.%s",path,filename,extension));
            try{
                BufferedWriter bd=new BufferedWriter(new FileWriter(tempfile));
                bd.write(String.format("%s",toWrite));
                bd.close();
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(null,ex.getMessage());
            }
            return success=true;

        }
        else{
            JOptionPane.showMessageDialog(null, "No Directory Selected", "Failed", JOptionPane.ERROR_MESSAGE);
            return success=false;
        }
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

    public static void main(String[] args){
        Main frame = new Main();
        frame.setTitle("DSA");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();


    }

}

