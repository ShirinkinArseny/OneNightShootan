package Shootan.Worlds;

import Shootan.GameEssences.Bullets.Bullet;
import Shootan.GameEssences.Units.Unit;
import Shootan.GameEssences.Units.VisibleUnit;
import Shootan.ServerConfigs;
import Shootan.Utils.IndexWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static Shootan.Utils.ByteUtils.*;

public class ServerWorld extends StrangeWorld implements GamePlayWorldAPI{

    private CopyOnWriteArrayList<String> messages=new CopyOnWriteArrayList<>();

    private int[] frags;
    private int[] deaths;
    private GamePlay gameplay;

    protected void onShot(Unit bullet, Bullet shoter) {
        gameplay.onShot(bullet, shoter);
    }

    protected void onHit(Unit killedUnit, Bullet killer) {
        gameplay.onHitAction(killedUnit, killer);
    }

    protected void onKilled(Unit killedUnit, Bullet killer) {
        frags[killer.getAuthor()]++;
        deaths[killedUnit.getId()]++;

        gameplay.onDeathAction(killedUnit, killer);
    }

    @Override
    protected void additionalUpdate(float deltaTime) {
        units.stream().filter(u -> u.getAI() != null).forEach(u -> {
            updateVisibilityMap(u.getX(), u.getY());
            ArrayList<VisibleUnit> visibleUnits = new ArrayList<>(20);
            visibleUnits.addAll(
                    units
                            .stream()
                            .filter(k -> k.getId() != u.getId() && isVisible((int) k.getX(), (int) k.getY()))
                            .collect(Collectors.toList()));
            u.getAI().applyIntelligence(u, visibleUnits, this);
        });

        gameplay.update(deltaTime);

    }

    public void dropStatistic() {
        for (int i=0; i<units.size(); i++) {
            frags[i]=0;
            deaths[i]=0;
        }
    }

    public void acceptMessage(String msg) {
        gameplay.onIncomingMessage(msg);
    }

    public void sendMessage(String msg) {
        System.out.println("Added message: "+msg);
        messages.add(msg);
    }

    @Override
    public int getFrags(Unit player) {
        return frags[player.getId()];
    }

    @Override
    public int getDeaths(Unit player) {
        return deaths[player.getId()];
    }

    @Override
    public String getUnitName(Unit u) {
        for (Player p: playerUnitMap.values()) {
            if (p.getUnit().getId()==u.getId()) {
                return p.getName();
            }
        }
        return u.toString();
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
            p.getUnit().deserializeState(data);
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

        Unit acceptedUnit=gameplay.acceptConnection();

        playerUnitMap.put(connectionId, new Player(userName, acceptedUnit));
        ArrayList<Byte> response=new ArrayList<>();
        response.add(booleanToByte(true));
        response.addAll(uIntToBytes(acceptedUnit.getId()));

        response.addAll(uIntToBytes(playerUnitMap.size()));

        for (long someConnectionId: playerUnitMap.keySet()) {
            Player p=playerUnitMap.get(someConnectionId);
            response.addAll(uIntToBytes(p.getUnit().getId()));
            response.addAll(stringToBytes(p.getName()));
            response.addAll(uIntToBytes(frags[p.getUnit().getId()]));
            response.addAll(uIntToBytes(deaths[p.getUnit().getId()]));
        }

        return response;
    }

    public void addUnit(Unit u) {
        units.add(u);

        int[] deaths=new int[units.size()];
        System.arraycopy(this.deaths, 0, deaths, 0, this.deaths.length);
        deaths[deaths.length-1]=0;
        this.deaths=deaths;

        int[] frags=new int[units.size()];
        System.arraycopy(this.frags, 0, deaths, 0, this.frags.length);
        frags[frags.length-1]=0;
        this.frags=frags;
    }

    public ServerWorld(GamePlay gameplay) {
        super();
        this.gameplay = gameplay;
        gameplay.setGamePlayAPI(this);
        frags=new int[units.size()];
        deaths=new int[units.size()];
        dropStatistic();
    }
}