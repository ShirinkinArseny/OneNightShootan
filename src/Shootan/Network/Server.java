package Shootan.Network;

import java.io.IOException;
import java.net.*;

import java.util.ArrayList;
import java.util.function.Consumer;


public class Server {

    private ArrayList<ServerConnection> clients;
    private Consumer<ArrayList<Byte>> event;
    private Consumer<String> onConnected;
    private int port;

    public void sendMessage(ArrayList<Byte> s) {
        for (ServerConnection c: clients)
            c.sendMessage(s);
    }

    public void setOnConnectedEvent(Consumer<String> onConnected) {
        this.onConnected=onConnected;
    }

    public void setOnInputEvent(Consumer<ArrayList<Byte>> r) {
        event=r;
    }

    public Server(int port){
        this.port = port;
        clients=new ArrayList<>();
    }

    public void start() {
        new Thread(() -> {
            ServerSocket server;
            try {
                server = new ServerSocket(port);
                while (true) {
                    Socket client = server.accept();

                    new Thread(() -> {
                        ServerConnection cl = new ServerConnection();

                        cl.setOnInputEvent(event::accept);

                        cl.setOnCloseEvent(() -> {
                            System.out.println("[Server] Disconnected client: " + client.getInetAddress().toString());
                            onConnected.accept(client.getInetAddress()+" disconnected");
                            clients.remove(cl);
                        });

                        clients.add(cl);
                        try {
                            cl.startWorking(client);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("[Server] Got client: " + client.getInetAddress());
                        onConnected.accept(client.getInetAddress()+" connected");
                    }).start();


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}