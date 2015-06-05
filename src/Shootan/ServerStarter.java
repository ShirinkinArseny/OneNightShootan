package Shootan;

import Shootan.Network.Server;
import Shootan.Worlds.ServerWorld;

import java.io.*;

public class ServerStarter {


    public static void main(String[] args) throws IOException {



        ServerWorld w=new ServerWorld();

        Server server=new Server(ClientConfigs.serverPort);
        server.setOnInputEvent(w::acceptUnitChangedState);
        server.start();

        new Thread(() -> {
            while (true) {
                server.sendMessage(w.createWorldDump());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {

            long lastTimeNanos=System.nanoTime();

            while (true) {
                long currentTimeNanos = System.nanoTime();
                float sec = (currentTimeNanos - lastTimeNanos) / 1000000000.0f;
                lastTimeNanos = currentTimeNanos;
                w.update(sec);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();





    }
}