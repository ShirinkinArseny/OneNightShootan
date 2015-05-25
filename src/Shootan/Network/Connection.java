package Shootan.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static Shootan.Utils.ByteUtils.bytesToString;
import static Shootan.Utils.ByteUtils.stringToBytes;

public class Connection {

    private Consumer<ArrayList<Byte>> event;
    private Runnable close;
    AtomicBoolean isWorking = new AtomicBoolean(true);

    public boolean getIsWorking() {
        return isWorking.get();
    }

    public void setOnCloseEvent(Runnable r) {
        close = r;
    }

    public void setOnInputEvent(Consumer<ArrayList<Byte>> r) {
        event = r;
    }

    public void sendMessage(ArrayList<Byte> s) {
            //System.out.println("[Connection] Sending message: " + s);
        if (out!=null)
            out.println(bytesToString(s));
    }

    private PrintWriter out;

    public void startWorking(BufferedReader in, PrintWriter out) throws IOException {
        this.out=out;
        System.out.println("Start working, out: " + out);
        new Thread(() -> {
            try {
                String input;
                System.out.println("IN DA THREAD");
                while ((input = in.readLine()) != null) {
                    //System.out.println("[Connection] Gotcha msg, working...");
                    event.accept(stringToBytes(input));
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("[Connection] Error pt1 : " + e);
            }

            System.out.println("MID...");

            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("[Connection] Error pt 2: " + e);
            }
            System.out.println("Closing...");
            out.close();
            isWorking.set(false);
            if (close != null) close.run();
        }).start();
        System.out.println("Started, ya!");
    }
}