package Shootan.Network;

import java.io.IOException;
import java.net.*;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public class Server {

    private ArrayList<ServerConnection> clients;
    private BiConsumer<Long, ArrayList<Byte>> onInput;
    private BiFunction<Long, ArrayList<Byte>,  ArrayList<Byte>> onHandShake;
    private Consumer<Long> onDisconnected;
    private int port;

    public void sendMessage(ArrayList<Byte> s) {
        for (ServerConnection c: clients)
            c.sendMessage(s);
    }

    public void setOnDisconnectedEvent(Consumer<Long> onDisconnected) {
        this.onDisconnected=onDisconnected;
    }

    public void setOnHandShakeEvent(BiFunction<Long, ArrayList<Byte>,  ArrayList<Byte>> r) {
        onHandShake =r;
    }

    public void setOnInputEvent(BiConsumer<Long, ArrayList<Byte>> r) {
        onInput =r;
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

                        final boolean[] isFirstUsage = {true};
                        cl.setOnInputEvent((id, bytes) -> {
                            if (isFirstUsage[0]) {
                                ArrayList<Byte> response=onHandShake.apply(id, bytes);
                                cl.sendMessage(response);
                                isFirstUsage[0] =false;

                                byte[] sBytes=new byte[response.size()];
                                for (int i=0; i<response.size(); i++) {
                                    sBytes[i]=response.get(i);
                                }

                                System.out.println("Send response: "+new String(sBytes));
                                clients.add(cl);
                            } else {
                                onInput.accept(id, bytes);
                            }
                        });

                        cl.setOnCloseEvent(() -> {
                            System.out.println("[Server] Disconnected client: " + client.getInetAddress().toString());
                            onDisconnected.accept(cl.getConnectionID());
                            clients.remove(cl);
                        });

                        try {
                            cl.startWorking(client);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("[Server] Got client: " + client.getInetAddress());
                    }).start();


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}