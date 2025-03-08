package model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Plan {
    protected String planName;
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
    public Pill getPillInfo(String pillName) {
       for (Pill pill : mapPill.keySet()) {
        if (pill.getName() == pillName) {
            return pill;
        }
       }
       return null;
    }

    //return ArrayList<LocalTime> of pills given Pill
    public ArrayList<LocalTime> getPillTime(Pill pill){
        if (mapPill.containsKey(pill)){
        return mapPill.get(pill);
        }else{
            return null;
        }
    }

    public LocalDate getNextIntakeDate(Pill pill){
        int freq = pill.getFreq();
        int dayStart = startDate.getDayOfYear();
        int today = LocalDate.now().getDayOfYear();
        int calc = today-dayStart;
        int day;
        if (calc % freq ==0) {
            return LocalDate.now();
        } else {
            day = (calc % freq) + today;
        }
        return LocalDate.ofYearDay(2025, day);

    }



    public String getPlanName(){
        return planName;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public Map<Pill, ArrayList<LocalTime>> getMapPill() {
        return mapPill;
    }
}
