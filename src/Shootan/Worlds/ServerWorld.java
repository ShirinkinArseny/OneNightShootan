package Shootan.Worlds;

import Shootan.GameEssences.Bullets.Bullet;
import Shootan.GameEssences.Units.Human;
import Shootan.GameEssences.Units.Unit;
import Shootan.ServerConfigs;
import Shootan.Utils.IndexWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static Shootan.Utils.ByteUtils.*;

public class ServerWorld extends StrangeWorld {

    private CopyOnWriteArrayList<String> messages=new CopyOnWriteArrayList<>();

    private int[] frags;
    private int[] deaths;

    public void onKilled(Unit killedUnit, Bullet killer) {

        frags[killer.getAuthor()]++;
        deaths[killedUnit.getId()]++;


        addMessage(killedUnit.getId() + " was pwned by " + killer.getAuthor() + " using " + killer.getName());
    }

    @Override
    public void additionalUpdate(float deltaTime) {
        /*units.stream().filter(u -> u.getAI() != null).forEach(u -> {
            updateVisibilityMap(u.getX(), u.getY());
            ArrayList<VisibleUnit> visibleUnits = new ArrayList<>(20);
            visibleUnits.addAll(
                    units
                            .stream()
                            .filter(k -> k.getId() != u.getId() && isVisible((int) k.getX(), (int) k.getY()))
                            .collect(Collectors.toList()));
            u.getAI().applyIntelligence(u, visibleUnits, this);
        });


        if (System.currentTimeMillis()-startTime>roundTime) {
            respawn();
            startTime=System.currentTimeMillis();
        }*/
    }


    private String getStatistic() {

        class FDUnit {
            private Unit unit;
            private int deaths;
            private int frags;
            private float rate;

            public FDUnit (Unit u) {
                unit=u;
                deaths=ServerWorld.this.deaths[u.getId()];
                frags=ServerWorld.this.frags[u.getId()];
                rate=-(frags-1.9f*deaths);
            }

            public int compare(FDUnit u) {
                return Float.compare(rate, u.rate);
            }

            public Unit getUnit() {
                return unit;
            }

            public float getRate() {
                return rate;
            }

            public int getFrags() {
                return frags;
            }

            public int getDeaths() {
                return deaths;
            }

        }

        ArrayList<FDUnit> sortedUnits=new ArrayList<>(units.size());
        for (Unit u: units)
            sortedUnits.add(new FDUnit(u));
        sortedUnits.sort(FDUnit::compare);

        StringBuilder stat=new StringBuilder();
        for (FDUnit u: sortedUnits) {
            stat
                    .append("ID: ")
                    .append(u.getUnit().getId())
                    .append(" RATE: ")
                    .append(u.getRate())
                    .append(" FRAGS: ")
                    .append(u.getFrags())
                    .append(" DEATHS: ")
                    .append(u.getDeaths())
                    .append('\n');
        }

        return stat.toString();
    }

    private void dropStat() {
        for (int i=0; i<units.size(); i++) {
            frags[i]=0;
            deaths[i]=0;
        }
    }

    private void respawn() {
        System.out.println(getStatistic());
        for (Unit u: units) {
            u.setX(10);
            u.setY(10);
            u.setHealth(1.0f);
        }
        dropStat();
    }

    public void addMessage(String msg) {
        System.out.println("Added message: "+msg);
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
            res.addAll(stringToBytes(message));
        } else {
            res.addAll(uIntToBytes(0));
        }
        return res;
    }

    public void acceptUnitChangedState(Long connectionId, ArrayList<Byte> data) {

        Player p=playerUnitMap.get(connectionId);
        if (p!=null) {
            for (Unit u: units) {
                if (u.getId()==p.getUnitId()) {
                    u.deserializeState(data);
                    break;
                }
            }
        }

    }

    public void discardConnection(Long connectionId) {

        playerUnitMap.remove(connectionId);


    }

    private HashMap<Long, Player> playerUnitMap=new HashMap<>(); //Contains connection between player id (network connection id) and [unit id, player name]

    public synchronized ArrayList<Byte> acceptHandShake(Long connectionId, ArrayList<Byte> data) {
        IndexWrapper index=new IndexWrapper();
        int gameVersion=twoBytesToUInt(data, index);
        if (gameVersion!= ServerConfigs.gameVersion) {
            ArrayList<Byte> response=new ArrayList<>();
            response.add(booleanToByte(false));
            response.addAll(stringToBytes("Game versions are not same. Server game version: "+ServerConfigs.gameVersion+", but your: "+gameVersion));
            return response;
        }
        String passwdHash=bytesToString(data, index);
        String userName=bytesToString(data, index);
        if (!passwdHash.equals(securityHash(ServerConfigs.serverPassword+userName))) {
            ArrayList<Byte> response=new ArrayList<>();
            response.add(booleanToByte(false));
            response.addAll(stringToBytes("You typed wrong server passwd"));
            return response;
        }

        int playersNumber=playerUnitMap.size();
        if (playersNumber==ServerConfigs.serverPlayersLimit) {
            ArrayList<Byte> response=new ArrayList<>();
            response.add(booleanToByte(false));
            response.addAll(stringToBytes("There are no free player slots on server. Used "+playersNumber+"/"+playersNumber));
            return response;
        }

        int foundFreeId=-1;

        boolean[] freeUnitIDs=new boolean[ServerConfigs.serverPlayersLimit];
        for (int i=0; i<ServerConfigs.serverPlayersLimit; i++) {
            freeUnitIDs[i]=true;
        }
        for (Player p: playerUnitMap.values()) {
            freeUnitIDs[p.getUnitId()]=false;
        }
        for (int i=0; i<ServerConfigs.serverPlayersLimit; i++) {
            if (freeUnitIDs[i]) {
                foundFreeId=i;
                break;
            }
        }

        if (foundFreeId==-1) {
            ArrayList<Byte> response=new ArrayList<>();
            response.add(booleanToByte(false));
            response.addAll(stringToBytes("Some strange shit happened. Seems like all slots are used, but not really. Likely it's a bug"));
            return response;
        }

        playerUnitMap.put(connectionId, new Player(userName, foundFreeId));
        ArrayList<Byte> response=new ArrayList<>();
        response.add(booleanToByte(true));
        response.addAll(uIntToBytes(playersNumber));

        for (long someConnectionId: playerUnitMap.keySet()) {
            Player p=playerUnitMap.get(someConnectionId);
            response.addAll(uIntToBytes(p.getUnitId()));
            response.addAll(stringToBytes(p.getName()));
            response.addAll(uIntToBytes(frags[p.getUnitId()]));
            response.addAll(uIntToBytes(deaths[p.getUnitId()]));
        }

        return response;
    }

    public ServerWorld() {
        super();


        for (int i=0; i<10; i++)
            units.add(new Human(10, 10));

        for (Unit u: units) {
            u.bindAI((me, visibleUnits, world) -> {

            });
        }
        frags=new int[units.size()];
        deaths=new int[units.size()];
        dropStat();
    }
}