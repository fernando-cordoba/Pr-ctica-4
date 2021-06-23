
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ryukai
 */
public class Server {
    
    public static void main(String[] args) throws IOException{
    
        ServerSocket servidor = null;
        Socket socket = null;
        DataInputStream input;
        DataOutputStream output;
        
        
        
        File carpeta = new File("//../home/ryukai/Documentos/Escritorio/elieth unix/SocketTCP/src/main/java/Images");
        
        File[] listado = carpeta.listFiles();
        
        servidor = new ServerSocket(5000);
        JOptionPane.showMessageDialog(null, "Server is working");
        
        while(true){
            
            socket = servidor.accept();
            
        
            input  = new DataInputStream(socket.getInputStream());
            output  = new DataOutputStream(socket.getOutputStream());
            
            String filename = input.readUTF();
            
            filename = carpeta + "/" + filename;
            
            if(listado == null || listado.length==0){
            
                JOptionPane.showMessageDialog(null, "Directorio vacío");
                
            }else{
                
                
                
                for(int i=0 ; i< listado.length ; i++){
                if(filename.contains(listado[i].toString())){
                    
                    output.writeUTF("Archivo encontrado");
                    
                    FileInputStream fileinput = new FileInputStream(listado[i].getAbsolutePath());
                    
                    byte[] filecontentbytes = new byte[(int) listado[i].length()];
                    fileinput.read(filecontentbytes);
                    
                    String filen = listado[i].getName();
                    byte[] filenamebytes = filen.getBytes();
                    
                    output.writeInt(filenamebytes.length);
                    output.write(filenamebytes);
                    
                    output.writeInt(filecontentbytes.length);
                    output.write(filecontentbytes);
                    
                    break;
                
                }else if((i==listado.length-1) && !(filename.contains(listado[i].toString()))){
                
                    output.writeUTF("Archivo no encontrado");
                    
                }               
                }
            }
            
            
            socket.close();
            
        }
        
    
        
    }
    
}
