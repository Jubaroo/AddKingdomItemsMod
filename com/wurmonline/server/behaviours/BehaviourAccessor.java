package com.wurmonline.server.behaviours;

/**
 * Created by neko on 11/08/16.
 */
public class BehaviourAccessor {

    public static Seat getSeat(byte type) {
        return new Seat(type);
    }
    public static void setMaxSpeed (Vehicle v, float speed) {
        v.setMaxSpeed(speed);
    }
    public static void setMaxDepth(Vehicle v, float depth) {
        v.setMaxDepth(depth);
    }
    //public static void setMaxHeight(Vehicle v, float height) {
    //    v.setMaxHeight(height);
    //}
    //public static void setSkillNeeded(Vehicle v, float skill) {
    //    v.setSkillNeeded(skill);
    //}
    public static void setMaxHeightDiff(Vehicle v, float heightdiff){
        v.setMaxHeightDiff(heightdiff);
    }
    public static void setEmbarkString(Vehicle v, String s) {
        v.setEmbarkString(s);
    }
    public static void setPilotName(Vehicle v, String s) {
        v.setPilotName(s);
    }
}
