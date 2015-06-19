package Shootan.TimeFunctions;

public class LinearAIMer {

    private float aimValue;
    private float startValue;
    private float aimTime;

    private long aimStartTime;

    public LinearAIMer(float startValue, float aimTime) {
        this.startValue = startValue;
        this.aimTime = aimTime;
        aimStartTime=System.currentTimeMillis();
    }

    public float getValue() {

        long ctm=System.currentTimeMillis();

        if (ctm-aimStartTime>aimTime*1000) {
            return aimValue;
        } else {
            return startValue+(aimValue-startValue)*(ctm-aimStartTime)/(1000f*aimTime);
        }

    }

    public void setValue(float newValue) {
        startValue=newValue;
        aimValue=newValue;
    }

    public void aim(float newValue) {
        startValue=getValue();
        aimValue=newValue;
        aimStartTime=System.currentTimeMillis();
    }

}
