
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
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
public class ChatClient implements Runnable{
    private final String SERVER_ADDRESS = "127.0.0.1";
    private ClientSocket clientSocket;
    private Scanner scanner;
    private PrintWriter out;
    
    public ChatClient(){
        scanner = new Scanner(System.in);
    }
    
    public void start() throws IOException{
        try{
            clientSocket = new ClientSocket(new Socket(SERVER_ADDRESS,ChatServer.PORT));
            //this.out =new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            System.out.println("Cliente conectado ao servidor em "+SERVER_ADDRESS+":"+ChatServer.PORT);
            new Thread(this).start();
            messageLoop();
        } finally {
            clientSocket.close();
        }
    }
    
    @Override
    public void run(){
        String msg;
        while((msg=clientSocket.getMessage())!=null)
       
        System.out.printf("\nMsg recebida do servidor: %s\n",msg);
    }
    
    private void messageLoop() throws IOException{
        String msg;
        do{
            System.out.print("Digite uma mensagem (sair para finalizar): ");
            msg = scanner.nextLine();
        
            //out.write(msg);
            //System.out.println("==>"+msg);
            clientSocket.sendMsg(msg);
            
            //out.newLine();
            //out.flush();
        }while(!msg.equalsIgnoreCase("sair"));
    }
    
    public static void main(String[] args) {
        
        try {
            ChatClient client = new ChatClient();
            client.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar cliente"+ex.getMessage());
        }
        System.out.println("Cliente finalizado");
    }


}
