package model;

import java.time.LocalTime;

public class Pill {
    private String name;
    private int dosage;
    private int frequencyDay;
    private String noteString;

    public Pill(String name, int dosage, int freq) {
        this.name = name;
        this.dosage = dosage;
        frequencyDay = freq;
        noteString = "";
    }

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

}
