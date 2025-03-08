package model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Plan {
    String planName;
    LocalDate startDate;
    LocalDate endDate;
    Map<Pill, ArrayList<LocalTime>> mapPill;

    public Plan(String name, LocalDate startDate, LocalDate endDate){
        this.planName = name;
        this. startDate = startDate;
        this.endDate = endDate;
        mapPill = new HashMap<>();
    }

    public void addPill(Pill pill, ArrayList<LocalTime> timeArray) {
        mapPill.put(pill, timeArray);
    }
    
    public void removePill(Pill pill) {
        if (mapPill.containsKey(pill)){
            mapPill.remove(pill);
        }
    }
    //return Pill given pill name
    public Pill getPillInfo(String pill) {
       for (String key : mapPill.keySet()) {
        System.out.println("Key = " + key);
       }
    }

    //return ArrayList<LocalTime> of pills given Pill
    public ArrayList<LocalTime> getPillTime(Pill pill){
        if (mapPill.containsKey(pill)){
        return mapPill.get(pill);
        }else{
            return null;
        }
    }



    protected String getPlanName() {
        return planName;
    }
    protected LocalDate getStartDate() {
        return startDate;
    }
    protected LocalDate getEndDate() {
        return endDate;
    }
    protected Map<Pill, ArrayList<LocalTime>> getMapPill() {
        return mapPill;
    }
}
