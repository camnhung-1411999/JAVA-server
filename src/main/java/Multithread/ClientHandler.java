package Multithread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {
    final Socket socket;
    final Scanner scanner;
    String name;
    boolean isLoggedIn;
    private DataInputStream input;
    private DataOutputStream output;


    public ClientHandler(Socket socket) {
        this.socket = socket;
        scanner = new Scanner(System.in);
//        this.name = name;
        isLoggedIn = true;
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        String received;
        while (true) {
            try {
                received = input.readUTF();
                if (received.equals(Constants.LOGOUT)) {
                    for (int i = 0; i < Server.listname.size(); i++) {
                        if (Server.listname.get(i).equals(this.name)) {
                            Server.listname.remove(i);
                            break;
                        }
                    }

                    Server.clients.remove(this);
                    this.isLoggedIn = false;
                    for (int i = 0; i < Server.getClient().size(); i++) {
                        if (Server.getClient().get(i).isLoggedIn) {
                            for (int j = 0; j < Server.listname.size(); j++) {
                                write(Server.getClient().get(i).output, Server.listname.get(j));
                            }
                        }
                    }
                    break;
                }

                forwardToClient(received);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        closeStreams();
    }

    private void forwardToClient(String received) {

        StringTokenizer tokenizer = new StringTokenizer(received, "#");
        //System.out.println(tokenizer.toString());
        String msg = received.substring(received.indexOf("#") + 1, received.length());
        int index = Integer.parseInt(tokenizer.nextToken());
        System.out.println(index);
        switch (index) {
            case 1: { // client sent string type : "1#username" ->login/signup with username
                this.name = tokenizer.nextToken();
                Server.listname.add(this.name);
                for (int i = 0; i < Server.getClient().size(); i++) {
                    if (Server.getClient().get(i).isLoggedIn) {
                        if (Server.getClient().get(i).name.equals(this.name)) {
                            for (int j = 0; j < Server.listname.size() - 1; j++) {
                                write(Server.getClient().get(i).output, Server.listname.get(j));
                            }
                        } else {
                            write(Server.getClient().get(i).output, this.name);
                        }
                    }
                }
            }
            break;
            case 2: {// client sent string : "2#recipient#msg -> this client send msg to receiver
                String recipient = tokenizer.nextToken().trim();
                String message = tokenizer.nextToken().trim();
                System.out.println(received);
                System.out.println(message);
                for (ClientHandler c : Multithread.Server.getClient()) {
                    if (c.isLoggedIn && c.name.equals(recipient)) {
                        write(c.output, msg );
                        System.out.println(name + " -->" + recipient + ": " + message);
                        break;
                    }
                }
            }
            break;

        }
    }

    //user
    private String read() {
        String line = "";
        try {
            line = input.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    private void write(DataOutputStream output, String message) {
        try {
            output.writeUTF(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

//    private void closeStreams() {
//        try {
//            this.input.close();
//            this.output.close();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private void closeSocket() {
//        try {
//            socket.close();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
}
