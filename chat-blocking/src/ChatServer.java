
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vinícius Moura
 */
public class ChatServer{
    public static final int PORT = 4000;
    private ServerSocket serverSocket;
    private final  List<ClientSocket> clients = new LinkedList<>();

    
    public void start() throws IOException{
        System.out.println("Servidor iniciado na porta: "+PORT);
        serverSocket = new ServerSocket(PORT);
        clientConnectionLoop();
    }
    
    private void clientConnectionLoop() throws IOException{
        while(true){
            //Fica aguadando uma conexão com o cliente
            //Socket clientSocket = serverSocket.accept();//bloqueantes
            ClientSocket clientSocket = new ClientSocket(serverSocket.accept());
            clients.add(clientSocket);
            
            //aguadando mensagem do cliente
            //BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //String msg = in.readLine(); //bloqueantes
            //expressão lambda e prog funcional
            new Thread(() -> clientMessageLoop(clientSocket)).start();
            
            
            
        }
    }
    
    private void sendMsgToAll(ClientSocket sender, String msg){
        Iterator<ClientSocket> iterator = clients.iterator();
        while(iterator.hasNext()){
            ClientSocket clientSocket = iterator.next();
            if(!sender.equals(clientSocket)){
                if(!clientSocket.sendMsg("Cliente "+sender.getRemoteSocketAddress()+": "+msg)){
                    iterator.remove();
                }
            }
        }
    }
    
    private void clientMessageLoop(ClientSocket clientSocket){
        String msg;
        try{
            while((msg = clientSocket.getMessage()) != null){
                if("sair".equalsIgnoreCase(msg))
                    return;
                System.out.printf("Msg recebida do cliente %s: %s\n",clientSocket.getRemoteSocketAddress(),msg);
                sendMsgToAll(clientSocket, msg);
            }
        } finally {
            clientSocket.close();
        }
        
    }
    
    public static void main(String[] args) {
        
        try {
            ChatServer server = new ChatServer();
            server.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar servidor"+ex.getMessage());
        }
        System.out.println("Servidor finalizado");
    }
}
