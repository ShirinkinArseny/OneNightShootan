package Shootan.GameEssences.Units;

import Shootan.AI.AI;
import Shootan.TimeFunctions.LinearAIMer;
import Shootan.Utils.IndexWrapper;
import Shootan.GameEssences.Weapon.*;

import java.util.ArrayList;
import java.util.List;

import static Shootan.Utils.ByteUtils.*;
import static Shootan.Utils.ByteUtils.twoBytesToAngle;

public abstract class Unit implements ControlledUnit {

    private float x;            //serializable
    private float y;            //serializable
    private float dx;
    private float dy;
    float motionAngle;  //serializable
    private float radiusQuad;
    private float radius;
    private int id;      //serializable
    private float speed;
    private float health;       //serializable
    private float damageCoef;

    private AI ai;

    private LinearAIMer aimerX;
    private LinearAIMer aimerY;

    public float getTimeApproxX() {
        return aimerX.getValue();
    }

    public float getTimeApproxY() {
        return aimerY.getValue();
    }

    public AI getAI() {
        return ai;
    }

    public void bindAI(AI ai) {
        this.ai=ai;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    private ArrayList<Weapon> weapons;
    byte currentWeapon;

    boolean isMoving=false;

    public ArrayList<Byte> fullSerialise() {
        ArrayList<Byte> res=new ArrayList<>(18+weapons.size()*10);
        res.addAll(uIntToBytes(id));
        res.addAll(uIntToBytes(getType()));
        res.addAll(coordToBytes(x));
        res.addAll(coordToBytes(y));
        res.addAll(normalisedFloatToBytes(health));

        res.add(currentWeapon);
        res.addAll(angleToBytes(this.viewAngle));
        res.add(booleanToByte(isMoving));
        res.addAll(angleToBytes(this.motionAngle));
        res.add(booleanToByte(wannaShot));

        res.add((byte) weapons.size());
        for (Weapon w: weapons) {
            res.addAll(w.serialize());
        }
        return res;
    }

    public void fullDeserialiseIgnoringOpinion(List<Byte> input, IndexWrapper index) {
        try {
            x = fourBytesToCoord(input, index);
            aimerX.aim(x);
            y = fourBytesToCoord(input, index);
            aimerY.aim(y);
            health = twoBytesToNormalisedFloat(input, index);

            {
                /*
                Ignored values
                 */
                byte currentWeapon = input.get(index.value++);
                float viewAngle = twoBytesToAngle(input, index);
                boolean isMoving = byteToBoolean(input, index);
                float motionAngle = twoBytesToAngle(input, index);
                boolean wannaShot = byteToBoolean(input, index);
            }

            int weaponsNumber = input.get(index.value++);
            for (int i = 0; i < weaponsNumber; i++) {
                int weaponType = twoBytesToUInt(input, index);
                weapons.get(i).deserialize(input, index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Som shit happend");
        }
    }

    public void fullDeserialise(List<Byte> input, IndexWrapper index) {
        try {
            /*
            IGNORING ID AND TYPE - THEY ARE ALREADY USED
             */
            x = fourBytesToCoord(input, index);
            aimerX.aim(x);
            y = fourBytesToCoord(input, index);
            aimerY.aim(y);
            health = twoBytesToNormalisedFloat(input, index);

            currentWeapon = input.get(index.value++);
            viewAngle = twoBytesToAngle(input, index);
            isMoving = byteToBoolean(input, index);
            setMotionAngle(twoBytesToAngle(input, index));
            wannaShot = byteToBoolean(input, index);

            int weaponsNumber = input.get(index.value++);
            if (weaponsNumber!=weapons.size()) {
                weapons.clear();

                for (int i = 0; i < weaponsNumber; i++) {
                    int weaponType = twoBytesToUInt(input, index);
                    weapons.add(Weapon.createDeserialized(weaponType, this, input, index));
                }

            } else {
                for (int i = 0; i < weaponsNumber; i++) {
                    int weaponType = twoBytesToUInt(input, index);
                    weapons.get(i).deserialize(input, index);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Som shit happend");
        }
    }

    public static Unit createDeserialized(int id, List<Byte> input, IndexWrapper index) {
        int type=twoBytesToUInt(input, index);
        Unit me=UnitFactory.create(type);
        if (me==null) {
            new Exception("Invalid unit type: "+type).printStackTrace();
        }
        me.id=id;
        me.fullDeserialise(input, index);
        return me;
    }


    public ArrayList<Byte> serializeState() {
        ArrayList<Byte> res=new ArrayList<>(9);
        res.addAll(uIntToBytes(this.id));
        res.add(currentWeapon);
        res.addAll(angleToBytes(this.viewAngle));
        res.add(booleanToByte(isMoving));
        res.addAll(angleToBytes(this.motionAngle));
        res.add(booleanToByte(wannaShot));
        return res;
    }

    public void deserializeState(List<Byte> input) {
        IndexWrapper index=new IndexWrapper(2);
        currentWeapon= (byte) byteToShort(input, index);
        viewAngle= twoBytesToAngle(input, index);
        isMoving=byteToBoolean(input, index);
        setMotionAngle(twoBytesToAngle(input, index));
        wannaShot=byteToBoolean(input, index);
    }


    public boolean isMoving() {
        return isMoving;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving=isMoving;
    }

    public int getId() {
        return id;
    }


    public void setMotionAngle(float motionAngle) {
        dx= (float) (Math.cos(motionAngle)*speed);
        dy= (float) (Math.sin(motionAngle)*speed);
        this.motionAngle=motionAngle;
    }

    public void setViewAngle(float angle) {
        angle%=2*Math.PI;
        while (angle<0) angle+=2*Math.PI;
        viewAngle=angle;
    }

    public void changeViewAngle(float deltaAngle) {
        float angle=this.viewAngle+deltaAngle;
        angle%=2*Math.PI;
        while (angle<0) angle+=2*Math.PI;
        viewAngle=angle;
    }

    float viewAngle;

    public float getMotionAngle() {
        return motionAngle;
    }

    public float getMotionSpeed() {
        return speed;
    }

    boolean wannaShot=false;

    public void setWannaShot(boolean yeah) {
        wannaShot=yeah;
    }

    public boolean getWannaShot() {
        return wannaShot;
    }

    private static int idCounter=0;

    public abstract int getType();

    public Unit(float x, float y, float radius, float speed, float damageCoef) {
        this.x=x;
        this.y=y;
        aimerX=new LinearAIMer(x, 0.05f);
        aimerY=new LinearAIMer(y, 0.05f);
        this.radius=radius;
        radiusQuad=radius*radius;
        this.speed=speed;
        this.damageCoef=damageCoef;
        this.id=idCounter;
        health=1;
        idCounter++;
        weapons=new ArrayList<>();
        weapons.add(new FireThrower(this));
        weapons.add(new MP40(this));
        weapons.add(new RockerLauncher(this));
        weapons.add(new SniperRifle(this));
        currentWeapon=1;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void move(float dt, boolean acceptDx, boolean acceptDy) {
        if (acceptDx) x += dt * dx;
        aimerX.aim(x);
        if (acceptDy) y += dt * dy;
        aimerY.aim(y);
    }

    public float getHealth() {
        return health;
    }

    public void damage(float power) {
        health-=power*damageCoef;
    }

    public float getRadius() {return radius;}

    public float getRadiusQuad() {return radiusQuad;}

    public Weapon getWeapon() {
        return weapons.get(currentWeapon);
    }

    public int getCurrentWeapon() {
        return currentWeapon;
    }

    public void selectPreviousWeapon() {
        currentWeapon--;
        if (currentWeapon<0)
            currentWeapon= (byte) (weapons.size()-1);
    }

    public void selectNextWeapon() {
        currentWeapon++;
        if (currentWeapon>=weapons.size())
            currentWeapon=0;
    }

    public float getViewAngle() {
        return viewAngle;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setX(int x) {
        this.x = x;
        aimerX.aim(x);
    }

    public void setY(int y) {
        this.y = y;
        aimerY.aim(y);
    }

    public void setHealth(float health) {
        this.health = health;
    }
}