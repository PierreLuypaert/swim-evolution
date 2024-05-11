package models;

public class ActionType {
    private final double time;
    private final int angleValue;

    public ActionType(double time,int angleValue) {
    	this.time = time;
        this.angleValue = angleValue;
    }
        
    public double getTime() {
    	return this.time;
    }

    public int getAngleValue() {
        return angleValue;
    }
}