
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ryukai
 */
public class Client {

    public static void main(String[] args){

        final int Port = 5000;

        JFrame jframe = new JFrame("Client Downloader");
        jframe.setSize(600, 450);
        jframe.setLayout(new BoxLayout(jframe.getContentPane(), BoxLayout.Y_AXIS));
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel paneltext = new JPanel(new FlowLayout(SwingConstants.LEADING, 50, 10));
        JPanel panelbutton = new JPanel(new FlowLayout(SwingConstants.LEADING, 195, 10));

        JLabel jtitle = new JLabel("File Downloader");
        jtitle.setFont(new Font("Arial", Font.BOLD, 25));
        jtitle.setBorder(new EmptyBorder(5, 0, 40, 0));
        jtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jFile = new JLabel("Type the file you want to download");
        jFile.setFont(new Font("Arial", Font.BOLD, 18));
        jFile.setBorder(new EmptyBorder(20, 0, 30, 0));
        jFile.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField field = new JTextField();
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.setPreferredSize(new Dimension(500, 40));
        field.setFont(new Font("Arial", Font.BOLD, 14));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        paneltext.add(field);

        JButton jbutton = new JButton("Download File");
        jbutton.setPreferredSize(new Dimension(200, 40));
        jbutton.setFont(new Font("Arial", Font.BOLD, 18));
        jbutton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelbutton.add(jbutton);

        jbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0)  {

                String filename = field.getText();
                if (field.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(null, "Write the name of the file first");

                } else {

                    String Ip = JOptionPane.showInputDialog("Ingrese la ip del servidor");
                    try {
                        Socket socket = new Socket(Ip, Port);
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                        DataInputStream input  = new DataInputStream(socket.getInputStream());

                        output.writeUTF(filename);
                        
                        String result = input.readUTF();
                        
                        if(result.equals("Archivo encontrado")){
                            
                            int filenamelength = input.readInt();
                            
                            if(filenamelength > 0){

                                byte[] filenamebytes = new byte[filenamelength];
                                input.readFully(filenamebytes, 0, filenamebytes.length);
                                
                                String filenam = new String(filenamebytes);

                                int filecontentlength = input.readInt();
                        
                                if(filecontentlength > 0){

                                    byte[] filecontentbytes = new byte[filecontentlength];
                                    input.readFully(filecontentbytes, 0, filecontentlength);

                                    createframe(filenam,filecontentbytes);
                                   

                                }

                            }
                        
                            
                        
                        }else{
                        
                            JOptionPane.showMessageDialog(null, result);
                        }
                        
                        socket.close();
                        jframe.dispose();

                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        });

        jframe.add(jtitle);
        jframe.add(jFile);
        jframe.add(paneltext);
        jframe.add(panelbutton);
        jframe.setVisible(true);

    }
    
    public static void createframe(String filenam,byte[] filedata){
        
    
        JFrame jframe = new JFrame("Client Downloader");
        jframe.setSize(600, 450);
        jframe.setLayout(new BoxLayout(jframe.getContentPane(), BoxLayout.Y_AXIS));
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel paneldownloader = new JPanel(new FlowLayout(SwingConstants.LEADING, 100, 10));
        
        JLabel jtitle = new JLabel("File Downloader");
        jtitle.setFont(new Font("Arial", Font.BOLD, 25));
        jtitle.setBorder(new EmptyBorder(5, 0, 40, 0));
        jtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel jprompt = new JLabel("Are you sure you want to download " + filenam + "?");
        jprompt.setFont(new Font("Arial", Font.BOLD, 18));
        jprompt.setBorder(new EmptyBorder(20, 0, 30, 0));
        jprompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        JButton jbuttonyes = new JButton("Yes");
        jbuttonyes.setPreferredSize(new Dimension(100, 80));
        jbuttonyes.setFont(new Font("Arial", Font.BOLD, 18));
        jbuttonyes.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton jbuttonno = new JButton("No");
        jbuttonno.setPreferredSize(new Dimension(100, 80));
        jbuttonno.setFont(new Font("Arial", Font.BOLD, 18));
        jbuttonno.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        paneldownloader.add(jbuttonyes);
        paneldownloader.add(jbuttonno);
        
        
        jbuttonyes.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                
                
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Guardar en: ");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                
                chooser.setAcceptAllFileFilterUsed(false);
                
                if(chooser.showSaveDialog(null)== JFileChooser.APPROVE_OPTION){
                    
                    FileOutputStream file = null;
                    try {
                        File filetodownload = new File(chooser.getSelectedFile().getAbsolutePath()+ "/" + filenam);
                        file = new FileOutputStream(filetodownload); 
                        file.write(filedata);
                        file.close();
                        jframe.dispose();
                        JOptionPane.showMessageDialog(null, "Descarga finalizada con éxito");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }catch(IOException e ){
                    
                    } finally {
                        try {
                            file.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    
                
                }
                
                
            }
        
        
        });
        
        jbuttonno.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                jframe.dispose();
            }
        
        
        });
        
        jframe.add(jtitle);
        jframe.add(jprompt);
        jframe.add(paneldownloader);
        
        jframe.setVisible(true);
        

        
    }

}
