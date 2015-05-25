package Shootan.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ReServer {

    private ArrayList<PrintWriter> writers=new ArrayList<>();

    public void sendMessage(String msg) {

        for (PrintWriter p: writers)
            p.println(msg+"\n");

    }

    public ReServer(int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Welcome to Server side");

                ServerSocket servers = null;

                try {
                    servers = new ServerSocket(4444);
                } catch (IOException e) {
                    System.out.println("Couldn't listen to port 4444");
                    System.exit(-1);
                }

                System.out.print("Waiting for a client...");
                while (true) {
                    try {
                        Socket fromclient = servers.accept();
                        System.out.println("Client connected");
                        new Thread(() -> {

                            BufferedReader in=null;
                            PrintWriter out=null;
                            try {
                                in = new BufferedReader(new InputStreamReader(fromclient.getInputStream()));
                                out = new PrintWriter(fromclient.getOutputStream(), true);
                                writers.add(out);
                                String input, output;

                                System.out.println("Wait for messages");
                                while ((input = in.readLine()) != null) {
                                    if (input.equalsIgnoreCase("exit")) break;
                                    out.println("S ::: " + input);
                                    System.out.println(input);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (out!=null) out.close();
                                if (in!=null) try {
                                    in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    fromclient.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            }
        }).start();

    }

    public static void main(String[] args) {
        ReServer s=new ReServer(1234);

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            s.sendMessage("Yaytest "+System.currentTimeMillis());
        }
    }

}
