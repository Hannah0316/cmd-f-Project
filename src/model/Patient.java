package model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Patient {
    protected String patientName;
    ArrayList<Pill> Pills;

    public Patient(String name, LocalDate startDate, LocalDate endDate){
        this.patientName = name;
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


    public String getPatientName(){
        return patientName;
    }
    public Map<Pill, ArrayList<LocalTime>> getMapPill() {
        return mapPill;
    }
}
