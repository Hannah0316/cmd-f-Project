package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Patient {
    protected String patientName;
    ArrayList<Pill> pills;

    public Patient(String name) {
        this.patientName = name;
        pills = new ArrayList<>();
    }

    public void addPill(String name, int dosage, int freq, LocalDate start, LocalDate end, ArrayList<LocalTime> times, String note) {
        Pill pill = new Pill(name, dosage, freq, start, end, times, note);
        pills.add(pill);
    }

    public void removePill(String pillName) {
        for (Pill p : pills) {
            if (p.getName().equals(pillName)) {
                pills.remove(p);
            }
        }
    }

    public void removePill(Pill pill) {
        for (Pill p : pills) {
            if (p.equals(pill)) {
                pills.remove(p);
            }
        }
    }

    // return Pill given pill name
    public Pill getPill(String pillName) {
        for (Pill pill : pills) {
            if (pill.getName().equals(pillName)) {
                return pill;
            }
        }
        return null;
    }

    public Pill getPill(Pill pill) {
        for (Pill p : pills) {
            if (p.equals(pill)) {
                return p;
            }
        }
        return null;
    }


    public Pill getPillIndex(int i) {
        return pills.get(i);
    }

    public String getPatientName() {
        return patientName;
    }

    public ArrayList<Pill> getPills() {
        return pills;
    }

}
