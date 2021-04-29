package com.Dsa;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Receiver extends JFrame {

static Main m;
static RSA r;
static JTextField filepath;
JButton browse;
JButton Decrypt;
static JLabel Name;
static JLabel TypeOfFile;
static JLabel Location;
static JLabel size;
static JLabel Created;
static JLabel Modified;
static File s;
static String toReturn;
static JTextArea message;
static JButton getFile;


    public Receiver(){
        TitledBorder buttonBorders=BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Tools");
        TitledBorder fileBorders=BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"File");
        TitledBorder detailsBorder=BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Path");
        TitledBorder messageDetails=BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"File Inputs");
        setLayout(new BorderLayout(50,30));

        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new GridLayout(3,0,30,10));
        buttonPanel.setBorder(buttonBorders);

        JPanel filePanel=new JPanel();
        filePanel.setLayout(new GridLayout(7,0,10,20));
        filePanel.setBorder(fileBorders);

        JPanel detailPanel=new JPanel();
        detailPanel.setBorder(detailsBorder);
        detailPanel.setLayout(new GridLayout(1,0,10,20));

        JPanel messagePanel=new JPanel();
        messagePanel.setLayout(new GridLayout(1,0));
        messagePanel.setBorder(messageDetails);

        Name = new JLabel("Name:");
        TypeOfFile = new JLabel("Type Of File:            ");
        Location = new JLabel("Location:");
        size = new JLabel("Size:");
        Created = new JLabel("Created:");
        Modified = new JLabel("Modified");
        message=new JTextArea();
        getFile=new JButton("Get File From path");

        filepath=new JTextField();
        browse=new JButton("Browse");
        Decrypt=new JButton("Decrypt");

        detailPanel.add(filepath);



        buttonPanel.add(browse);
        buttonPanel.add(Decrypt);
        buttonPanel.add(getFile);

        filePanel.add(Name);
        filePanel.add(TypeOfFile);
        filePanel.add(Location);
        filePanel.add(size);
        filePanel.add(Created);
        filePanel.add(Modified);

        messagePanel.add(message);

        add(detailPanel,BorderLayout.NORTH);
        add(buttonPanel,BorderLayout.WEST);
        add(filePanel,BorderLayout.EAST);
        add(messagePanel,BorderLayout.CENTER);


        browse.addActionListener(e -> {

             s=m.GetSelectedFile();
            setFileDetails(s);
            message.setText(toReturn);
        });

        Decrypt.addActionListener(e -> {
            r=new RSA();
            r.setSize(800, 350);
            r.setVisible(rootPaneCheckingEnabled);
            r.enterMessage.setText(toReturn);
        });
        getFile.addActionListener(e -> {
            String path=filepath.getText();
            File file=new File(String.format("%s",path));
            setFileDetails(file);
            message.setText(toReturn);
        });

    }
    public static void setFileDetails(File file){
        try{
            filepath.setText(file.getAbsolutePath());
            toReturn = m.readFileAsString(file.getAbsolutePath());
            Name.setText("Name: "+file.getName());
            TypeOfFile.setText("Type Of File: .txt");
            Location.setText("Location: "+file.getAbsolutePath());
            double dsize=(double) file.length()   ;
            DecimalFormat df=new DecimalFormat();
            df.setMaximumFractionDigits(2);
            try{
                FileTime createdtime=(FileTime) Files.getAttribute(file.toPath(),"creationTime");
                Created.setText("Created: "+createdtime.toInstant().atZone(ZoneId.systemDefault()).
                        format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            }
            catch (IOException e) {
                return;
            }
            String filesize=df.format(dsize)+" Bytes";
            size.setText("Size: "+filesize);
            //Hashed=hashing(toReturn);
            DateFormat sdf=new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
            Modified.setText("Modified: "+sdf.format(file.lastModified()));
            //JOptionPane.showMessageDialog(null, Hashed);
        }
        catch (Exception ignored){

        }


    }
    public static void main(String[] args) {
        Receiver frame = new Receiver();
        frame.setTitle("Receiver Form");
        frame.setSize(800, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
