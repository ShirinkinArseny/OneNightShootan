package Shootan.Worlds;

import Shootan.GameEssences.Bullets.Bullet;
import Shootan.GameEssences.Units.Unit;
import Shootan.ServerConfigs;
import Shootan.Utils.IndexWrapper;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import static Shootan.Utils.ByteUtils.*;

public class ClientWorld extends StrangeWorld {

    private boolean acceptedDump=false;

    public ArrayList<Byte> createUnitChangedState() {
        return getMe().serializeState();
    }

    private CopyOnWriteArrayList<ArrayList<Byte>> dumps=new CopyOnWriteArrayList<>();

    private Consumer<String> onInputMessage;

    public void applyLastDump() {

        try {

            if (dumps.size() > 0) {
                ArrayList<Byte> data = dumps.get(dumps.size() - 1);
                dumps.clear();

                IndexWrapper index = new IndexWrapper();
                int usersNumber = twoBytesToUInt(data, index);
                for (int i = 0; i < usersNumber; i++) {


                    boolean found = false;
                    int id = twoBytesToUInt(data, index);
                    for (Unit u : units) {
                        if (u.getId() == id) {
                            found = true;

                            int type = twoBytesToUInt(data, index);//ignored
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
                        Unit newUnit=Unit.createDeserialized(id, data, index);
                        units.add(newUnit);
                        if (id == this.myId) {
                            me = newUnit;
                        }
                    }

                }
                int bulletsNumber = twoBytesToUInt(data, index);

                ArrayList<Bullet> alifeBullets = new ArrayList<>(bullets.size());

                for (int i = 0; i < bulletsNumber; i++) {
                    int id = twoBytesToUInt(data, index);
                    int type = twoBytesToUInt(data, index);
                    boolean found = false;
                    for (Bullet b : bullets) {
                        if (b.getID() == id) {
                            if (b.getType()==type) {
                                b.deserialize(data, index);
                                alifeBullets.add(b);
                                found = true;
                            }
                            break;
                        }
                    }

                    if (!found) {
                        index.value-=2;//откатываем type
                        alifeBullets.add(Bullet.createDeserialized(data, index, id));
                    }

                }
                bullets.clear();
                bullets.addAll(alifeBullets);
                alifeBullets.clear();

                int messageBytesNumber = twoBytesToUInt(data, index);
                if (messageBytesNumber != 0) {
                    byte[] messageBytes = new byte[messageBytesNumber];
                    for (int i = 0; i < messageBytesNumber; i++) {
                        messageBytes[i] = data.get(index.value++);
                    }
                    String message = new String(messageBytes);

                    onInputMessage.accept(message);
                }

                if (!acceptedDump) {
                    acceptedDump = true;
                }

                if (me==null) {
                    new Exception("ME is still null; me.id=="+myId).printStackTrace();
                }
            }


        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void acceptWorldDump(Long connectionId, ArrayList<Byte> data) {
        if (dumps.size()>0) {
            dumps.clear();
        }
        dumps.add(data);
    }



    private int myId=-1;
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
        for (Unit u: units) {
            if (u.getId()==myId) {
                me=u;
                break;
            }
        }
    }

    private long lastUpdation=0;

    @Override
    public void additionalUpdate(float deltaTime) {
        if (System.nanoTime()-lastUpdation>100000000) {
            updateVisibilityMap(getMe().getX(), getMe().getY());
            lastUpdation=System.nanoTime();
        }
    }

    public void update(float dt) {
        applyLastDump();
        super.update(dt);
    }

    public void setOnInputMessage(Consumer<String> onInputMessage) {

        this.onInputMessage=onInputMessage;
    }

    public ClientWorld() {
        super();
        for (Unit u: units) {
            if (u.getId()==myId) {
                me=u;
                break;
            }
        }
    }

    public ArrayList<Byte> generateHandShake() {
        ArrayList<Byte> res=new ArrayList<>();
        res.addAll(uIntToBytes(ServerConfigs.gameVersion));
        res.addAll(stringToBytes(securityHash(ServerConfigs.serverPassword+ServerConfigs.playerName)));
        res.addAll(stringToBytes(ServerConfigs.playerName));
        return res;
    }

    private int[] frags;
    private int[] deaths;
    private HashMap<Integer, String> unitNameMap=new HashMap<>(); //Contains connection between player id (network connection id) and [unit id, player name]

    public boolean acceptHandShake(Long id, ArrayList<Byte> bytes) {
        IndexWrapper index=new IndexWrapper();
        boolean success=byteToBoolean(bytes, index);
        if (success) {
            myId=twoBytesToUInt(bytes, index);

            int playersNumber=twoBytesToUInt(bytes, index);
            frags=new int[playersNumber];
            deaths=new int[playersNumber];
            for (int i=0; i<playersNumber; i++) {
                int unitId=twoBytesToUInt(bytes, index);
                String name=bytesToString(bytes, index);
                unitNameMap.put(unitId, name);
                frags[unitId]=twoBytesToUInt(bytes, index);
                deaths[unitId]=twoBytesToUInt(bytes, index);
            }
            return true;

        } else {
            String errorMessage=bytesToString(bytes, index);
            new Exception("CANNOT PLAY WITH THIS SERVER CUZ: "+errorMessage).printStackTrace();
            return false;
        }
    }

    public boolean getAcceptedDump() {
        return acceptedDump;
    }
}