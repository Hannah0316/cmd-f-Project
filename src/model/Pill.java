package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Pill {
    private String name;
    private int dosage;
    private int frequencyDay;
    private String noteString;
    LocalDate startDate;
    LocalDate endDate;
    ArrayList<LocalTime> timeList;

    public Pill(String name, int dosage, int freq, LocalDate start, LocalDate end, ArrayList<LocalTime> times) {
        this.name = name;
        this.dosage = dosage;
        frequencyDay = freq;
        noteString = "";
        startDate = start;
        endDate = end;
        timeList = times;
    }

    public LocalDate getNextIntakeDate(){
        int freq = getFreq();
        int dayStart = startDate.getDayOfYear();
        int today = LocalDate.now().getDayOfYear();
        int calc = today - dayStart;
        int day;
        if (calc % freq == 0) {
            return LocalDate.now();
        } else {
            day = (calc % freq) + today;
        }
        return LocalDate.ofYearDay(2025, day);
    }

    // public LocalTime 

    public void addNote(String note) {
        noteString = note;
    }

    public String getName() {
        return name;
    }

    public int getDosage() {
        return dosage;
    }

    public int getFreq() {
        return frequencyDay;
    }

    public String getNote() {
        return noteString;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ArrayList<LocalTime> getTimes() {
        return timeList;
    }

}
