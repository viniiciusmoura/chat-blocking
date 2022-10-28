
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Vinícius Moura
 */
public class ClientSocket {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    
    
    public ClientSocket(Socket socket) throws IOException{
        this.socket= socket;
        System.out.println("Cliente "+socket.getRemoteSocketAddress()+" conectou");
        this.in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(),true);
    }
    
    
    public SocketAddress getRemoteSocketAddress(){
        return socket.getRemoteSocketAddress();
    }
    
    public void close(){
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar socket: "+ex.getMessage());
        }
        
    }
    
    public String getMessage(){
        try{
            return in.readLine();
        }catch(IOException e){
            return null;
        }
    }
    
    public boolean sendMsg(String msg){
        out.println(msg);
        return !out.checkError();
    }
    
    
}
