package Multithread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable{
    final Socket socket;
    final  Scanner scanner;
    String name;
    boolean isLoggedIn;
    private  DataInputStream input;
    private DataOutputStream output;


    public ClientHandler(Socket socket){
        this.socket = socket;
        scanner = new Scanner(System.in);
//        this.name = name;
        isLoggedIn = true;
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        }catch (IOException ex){
            log("ClientHandler:" + ex.getMessage());
        }
    }
    public void run() {
        String received;
//        write(output,"Your name:" +  name);
//name //name:msg
        while (true){
            try {
                received = input.readUTF();
                if(received.equals(Constants.LOGOUT)){
                    this.isLoggedIn = false;
//                    closeSocket();
//                    closeStreams();
                    break;
                }
                forwardToClient(received);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        closeStreams();
    }

    private void forwardToClient(String received){
        System.out.println(received);

        StringTokenizer tokenizer = new StringTokenizer(received, "#");
        if(tokenizer.countTokens()==2) {

            String recipient = tokenizer.nextToken().trim();
            String message = tokenizer.nextToken().trim();
            for (ClientHandler c : Multithread.Server.getClient()) {
                if (c.isLoggedIn && c.name.equals(recipient)) {
                    write(c.output, name + ": " + message);
                    log(name + " -->" + recipient + ": " + message);
                    break;
                }
            }
        }
        else{
            this.name = received;
            Multithread.Server.listname.add(this.name);
            for(int i = 0; i< Multithread.Server.getClient().size(); i++){
                if(Multithread.Server.getClient().get(i).isLoggedIn){
                    for(int j = 0; j< Multithread.Server.listname.size(); j++){
                        write(Multithread.Server.getClient().get(i).output, Multithread.Server.listname.get(j));
                    }
                }
            }
        }
    }

    private String read(){
        String line = "";
        try {
            line = input.readUTF();
        }catch (IOException e){
            log("read: " + e.getMessage());
        }
        return line;
    }

    private void write(DataOutputStream output, String message){
        try {
            output.writeUTF(message);
        }catch (IOException ex){
            log("write: " + ex.getMessage());
        }
    }

    private void closeStreams(){
        try {
            this.input.close();
            this.output.close();
        }catch (IOException ex){
            log("closeStreams: " + ex.getMessage());
        }
    }

    private  void closeSocket(){
        try {
            socket.close();
        }catch (IOException ex){
            log("closeSocket: " + ex.getMessage());
        }
    }

    private  void log(String msg){
        System.out.println(msg);
    }
}
