package Shootan.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection extends Connection{

    private PrintWriter out;
    private BufferedReader in;

    public ClientConnection(String ip, int port) {
        try {
            Socket clientSocket;
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("[ClientConnection] failed to init");
        }
    }

    public void start() {
        try {
            startWorking(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}