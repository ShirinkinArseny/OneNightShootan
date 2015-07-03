package Shootan;

import Shootan.GameEssences.Bullets.Bullet;
import Shootan.GameEssences.Units.Human;
import Shootan.GameEssences.Units.Unit;
import Shootan.Network.Server;
import Shootan.Worlds.GamePlay;
import Shootan.Worlds.GamePlayWorldAPI;
import Shootan.Worlds.ServerWorld;

import java.io.*;

public class ServerStarter {


    public static void main(String[] args) throws IOException {




        ServerWorld w=new ServerWorld(new GamePlay() {

            private GamePlayWorldAPI api;

            @Override
            public void setGamePlayAPI(GamePlayWorldAPI api) {
                this.api=api;
            }

            @Override
            public void onDeathAction(Unit deathUnit, Bullet killer) {
                deathUnit.setX(10);
                deathUnit.setY(10);
            }

            @Override
            public void update(float sec) {

            }

            @Override
            public void onIncomingMessage(String msg) {

            }

            @Override
            public void onHitAction(Unit hitted, Bullet hitter) {

            }

            @Override
            public void onShot(Unit bullet, Bullet shoter) {

            }

            @Override
            public Unit acceptConnection() {
                Unit u=new Human(10, 10);
                api.addUnit(u);
                return u;
            }
        });

        Server server=new Server(ServerConfigs.serverPort);
        server.setOnHandShakeEvent(w::acceptHandShake);
        server.setOnInputEvent(w::acceptUnitChangedState);
        server.setOnDisconnectedEvent(w::discardConnection);
        server.start();

        new Thread(() -> {

            long lastTimeNanos=System.nanoTime();

            while (true) {
                server.sendMessage(w.createWorldDump());

                long currentTimeNanos = System.nanoTime();
                float sec = (currentTimeNanos - lastTimeNanos) / 1000000000.0f;
                lastTimeNanos = currentTimeNanos;
                w.update(sec);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();





    }
}