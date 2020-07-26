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
<<<<<<<Updated upstream
=======
                System.out.println(received);
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
>>>>>>>Stashed changes
                forwardToClient(received);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void forwardToClient(String received) {

        StringTokenizer tokenizer = new StringTokenizer(received, "#");
        String msg = received.substring(received.indexOf("#") + 1, received.length());
        int index = Integer.parseInt(tokenizer.nextToken());
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
                break;
            }
            case 2: {// client sent string : "2#recipient#msg -> this client send msg to receiver
                String recipient = tokenizer.nextToken().trim();
                String message = tokenizer.nextToken().trim();
                String[] ex = msg.split("#");/// mesage có cấu trúc là (người gửi)#(list người nhận)#(tin nhắn): trường hợp dành cho chatlist

                if (ex.length > 2) {
                    for (ClientHandler c : Multithread.Server.getClient()) {
                        for (int i = 0; i < ex.length - 1; i++) {
                            if (c.isLoggedIn && c.name.equals(ex[i])) {
                                write(c.output, name + "#" + msg);
                                System.out.println(name + " -->" + recipient + ": " + message);
                                break;
                            }
                        }

                    }
                } else {
                    for (ClientHandler c : Multithread.Server.getClient()) {
                        if (c.isLoggedIn && c.name.equals(recipient)) {
                            write(c.output, name + "#" + msg);
                            System.out.println(name + " -->" + recipient + ": " + message);
                            break;
                        }
                    }
                }
                break;
            }
            case 4: {//logout :  4#nameLogout
                String nameLogout = tokenizer.nextToken();
                for (int i = 0; i < Server.listname.size(); i++) {
                    if (Server.listname.get(i).equals(this.name)) {
                        Server.listname.remove(i);
                        break;
                    }
                }
                for (int i = 0; i < Server.clients.size(); i++) {
                    if (Server.clients.get(i).name.equals(nameLogout)) {
                        Server.clients.remove(i);
                        break;
                    }
                }
                for (int i = 0; i < Server.getClient().size(); i++) {
                    if (Server.getClient().get(i).isLoggedIn) {
                        write(Server.getClient().get(i).output, "logout#" + nameLogout);
                    }
                }
            }
            case 3: {
                String recipient = tokenizer.nextToken().trim();
                String message = tokenizer.nextToken().trim();
                String[] ex = msg.split("#");/// mesage có cấu trúc là (người gửi)#(list người nhận)#(tin nhắn)#filesize: trường hợp dành cho chatlist

                if (ex.length >= 3) {
                    byte bytes[] = new byte[Integer.parseInt(ex[ex.length - 1])];
                    boolean flag = true;
                    for (ClientHandler c : Multithread.Server.getClient()) {
                        for (int i = 0; i < ex.length - 1; i++) {
                            if (c.isLoggedIn && c.name.equals(ex[i])) {
                                write(c.output, "3File3#" + name + "#" + msg);

                                try {
                                    if (flag) {
                                        input.read(bytes, 0, bytes.length);
                                        flag = !flag;
                                    }
                                    c.output.write(bytes, 0, bytes.length);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(name + " -->" + recipient + ": " + message);
                                break;
                            }
                        }
                    }
                }
                break;

            }
        }
    }

    private void write(DataOutputStream output, String message) {
        try {
            output.writeUTF(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
