package models;

public class ActionType {
    private double time;
    private int angleValue;

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
    
    public void setAngleValue(int angle) {
    	this.angleValue = angle;
    }
    public void setTime(double time) {
    	this.time = time;
    }
}