package Shootan.Worlds;

import Shootan.Bullets.Bullet;
import Shootan.Units.Human;
import Shootan.Units.Unit;
import Shootan.Utils.IndexWrapper;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

import static Shootan.Utils.ByteUtils.twoBytesToUInt;

public class ClientWorld extends StrangeWorld {

    private BiConsumer<Unit, Bullet> onKilling;

    public void onKilled(Unit killedUnit, Bullet killer) {
        onKilling.accept(killedUnit, killer);
    }

    public ArrayList<Byte> createUnitChangedState() {
        return getMe().serializeState();
    }

    private CopyOnWriteArrayList<ArrayList<Byte>> dumps=new CopyOnWriteArrayList<>();

    private void applyLastDump() {

        if (dumps.size()>0) {
            ArrayList<Byte> data = dumps.get(dumps.size() - 1);
            dumps.clear();

            IndexWrapper index = new IndexWrapper();
            int usersNumber = twoBytesToUInt(data.get(index.value++), data.get(index.value++));
            for (int i = 0; i < usersNumber; i++) {

                boolean found = false;
                int id = twoBytesToUInt(data.get(index.value), data.get(index.value + 1));
                for (Unit u : units) {
                    if (u.getId() == id) {
                        found = true;
                        if (id == this.myId) {
                            u.fullDeserialiseIgnoringOpinion(data, index);
                            me = u;
                        } else {
                            u.fullDeserialise(data, index);
                        }
                        break;
                    }
                }

                if (!found) {
                    units.add(Unit.createDeserialized(data, index));
                }

            }
            int bulletsNumber = twoBytesToUInt(data.get(index.value++), data.get(index.value++));
            bullets.clear();
            for (int i = 0; i < bulletsNumber; i++) {
                bullets.add(Bullet.createDeserialized(data, index));
            }
        }
    }

    public void acceptWorldDump(ArrayList<Byte> data) {
        if (dumps.size()>0) {
            dumps.clear();
        }
        dumps.add(data);
    }


    public int isVisible(int x, int y) {
        if (!(x >= 0 && x < SIZE && y >= 0 && y < SIZE)) return 0;
        return visibility[y][x].get();
    }

    private boolean getHasHardBlocksIgnoringStart(float fromX, float fromY, float toX, float toY) {
        float idx=toX-fromX;
        float idy=toY-fromY;

        float length= (float) Math.sqrt(idx*idx+idy*idy);

        float dx=idx/length;
        float dy=idy/length;

        float x=fromX;
        float y=fromY;

        for (int i=0; i<length; i++) {

            if (!((int)y==(int)fromY && (int)x==(int)fromX)) {
                if (blocks[(int)y][(int)x].getIsHard())
                    return true;
            }

            x+=dx;
            y+=dy;
        }
        return false;
    }

    private int updateBlockVisibility(float cameraBlockX, float cameraBlockY, int blockX, int blockY) {


        boolean isVisible1=!getHasHardBlocksIgnoringStart(blockX, blockY, cameraBlockX, cameraBlockY);

        boolean isVisible2=!getHasHardBlocksIgnoringStart(blockX+1, blockY, cameraBlockX, cameraBlockY);

        boolean isVisible3=!getHasHardBlocksIgnoringStart(blockX, blockY+1, cameraBlockX, cameraBlockY);

        boolean isVisible4=!getHasHardBlocksIgnoringStart(blockX+1, blockY+1, cameraBlockX, cameraBlockY);


        int res=
                (isVisible1?1:0)+
                (isVisible2?1:0)+
                (isVisible3?1:0)+
                (isVisible4?1:0);


        /*if (res!=0) {

            float myAngle = getMe().getViewAngle();
            if (myAngle < 0) myAngle += 2 * Math.PI;

            float blockAngle = (float) Math.atan2(blockY - cameraBlockY, blockX - cameraBlockX);
            if (blockAngle < 0) blockAngle += 2 * Math.PI;

            float angle = Math.abs(myAngle - blockAngle);
            angle = (float) Math.min(angle, 2 * Math.PI - angle);

            int res2;
            if (angle < Math.PI / 3) res2 = 4;
            else if (angle < Math.PI * 2 / 5) res2 = 3;
            else if (angle < Math.PI * 3 / 7) res2 = 2;
            else if (angle < Math.PI * 1 / 2) res2 = 1;
            else
                res2 = 0;

            if (res2 == 0) {
                res = 0;
            } else {
                res=res2*res/4;
            }
        }*/

        return res;
    }

    private void updateVisibilityMap(float cameraX, float cameraY) {

        int cameraXI= (int) cameraX;
        int cameraYI= (int) cameraY;

        new Thread(() -> {
            for (int i=Math.max(0, cameraXI-getPotentialViewDistance); i<Math.min(SIZE, cameraXI + getPotentialViewDistance); i++) {
                for (int j=Math.max(0, cameraYI-getPotentialViewDistance); j < Math.min(SIZE, cameraYI + getPotentialViewDistance); j++) {
                    visibility[j][i].set(updateBlockVisibility(cameraX, cameraY, i, j));
                }
            }
        }).start();


    }

    private int myId =new Random().nextInt(10);
    private Unit me=null;
    public Unit getMe() {
        return me;
    }



    protected void checkForNewShotings(float dt) {
        for (Unit u : units) {
            boolean nowWannaShot=me.getWannaShot();
            Bullet b = u.getWeapon().getNewBullet(dt);
            if (b != null) {
                bullets.add(b);
                u.setWannaShot(nowWannaShot);
            }
        }
    }


    protected void updateAlifeUnits() {
        super.updateAlifeUnits();
        for (Unit u: units) {
            if (u.getId()==myId) {
                me=u;
                break;
            }
        }
    }

    public void update(float dt) {
        applyLastDump();
        super.update(dt);
    }

    public ClientWorld(BiConsumer<Unit, Bullet> onKilling) {
        super();
        for (Unit u: units) {
            if (u.getId()==myId) {
                me=u;
                break;
            }
        }

        for (int i=0; i<SIZE; i++) {
            for (int j=0; j <SIZE; j++) {
                visibility[j][i].set(4);
            }
        }

        this.onKilling = onKilling;
        /*new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updateVisibilityMap(getMe().getX(), getMe().getY());
            }
        }, 20, 20);*/
    }

}