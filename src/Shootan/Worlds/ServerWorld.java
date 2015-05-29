package Shootan.Worlds;

import Shootan.Bullets.Bullet;
import Shootan.Units.Unit;

import java.util.ArrayList;

import static Shootan.Utils.ByteUtils.twoBytesToUInt;
import static Shootan.Utils.ByteUtils.uIntToBytes;

public class ServerWorld extends StrangeWorld {

    public void onKilled(Unit killedUnit, Bullet killer) {}

    public ArrayList<Byte> createWorldDump() {
        ArrayList<Byte> res=new ArrayList<>();
        res.addAll(uIntToBytes(units.size()));
        for (Unit u: units)
            res.addAll(u.fullSerialise());
        res.addAll(uIntToBytes(bullets.size()));
        for (Bullet b: bullets)
            res.addAll(b.serialize());
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