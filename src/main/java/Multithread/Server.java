package Multithread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    static List<ClientHandler> clients;
    ServerSocket serverSocket;
    static int numOfUsers = 0;
    Socket socket;
    public static List<String> listname;
    public Server(){
        listname = new ArrayList<String>();
        clients = new ArrayList<ClientHandler>();
        try {
            serverSocket = new ServerSocket(Constants.PORT);
        }catch (IOException e){
            log("Server:" + e.getMessage());
        }
    }

    private void waitConnection(){
        log("Server running....");
        while (true){
            try {
                socket = serverSocket.accept();
            }catch (IOException e){
                log("waitConnection:" +e.getMessage());
            }
            log("Client accepted: " + socket.getInetAddress());
            numOfUsers++;
            ClientHandler handler = new ClientHandler(socket);
            Runnable target;
            Thread thread = new Thread(handler);
            addClient(handler);
            thread.start();
        }
    }

    public static List<ClientHandler> getClient(){
        return clients;
    }

    private void addClient(ClientHandler client){
        clients.add(client);
    }

    private  void log(String message){
        System.out.println(message);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.waitConnection();
    }

}
