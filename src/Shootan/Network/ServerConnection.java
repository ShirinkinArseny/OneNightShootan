package Shootan.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection extends Connection {

    public void startWorking(Socket client) throws IOException{
        startWorking(new BufferedReader(new InputStreamReader(client.getInputStream())),
                new PrintWriter(client.getOutputStream(), true));
    }

    public ServerConnection()  {
    }
}
