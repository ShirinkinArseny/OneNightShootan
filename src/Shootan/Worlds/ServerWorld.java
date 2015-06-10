package Shootan.Worlds;

import Shootan.Bullets.Bullet;
import Shootan.Units.Unit;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static Shootan.Utils.ByteUtils.twoBytesToUInt;
import static Shootan.Utils.ByteUtils.uIntToBytes;

public class ServerWorld extends StrangeWorld {

    private CopyOnWriteArrayList<String> messages=new CopyOnWriteArrayList<>();

    public void onKilled(Unit killedUnit, Bullet killer) {
        addMessage(killedUnit.getId() + " was pwned by " + killer.getAuthor() + " using " + killer.getName());
    }

    public void addMessage(String msg) {
        messages.add(msg);
    }

    public ArrayList<Byte> createWorldDump() {

        ArrayList<Byte> res=new ArrayList<>();
        res.addAll(uIntToBytes(units.size()));
        for (Unit u: units)
            res.addAll(u.fullSerialise());
        res.addAll(uIntToBytes(bullets.size()));
        for (Bullet b: bullets)
            res.addAll(b.serialize());
        if (messages.size()!=0) {
            String message=messages.get(0);
            messages.remove(0);
            res.addAll(uIntToBytes(message.length()));
            for (byte b: message.getBytes()) {
                res.add(b);
            }
        } else {
            res.addAll(uIntToBytes(0));
        }
        return res;
    }

    public void acceptUnitChangedState(ArrayList<Byte> data) {
        int id=twoBytesToUInt(data.get(0), data.get(1));
        for (Unit u: units) {
            if (u.getId()==id) {
                u.deserializeState(data);
                break;
            }
        }
    }

    public ServerWorld() {
        super();
    }

}