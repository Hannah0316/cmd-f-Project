package model;

import java.time.LocalTime;

public class Pill extends Med {
    Med med;
    int dosage;
    LocalTime time;
    String note;
    Frequency freq;

    public Pill(String name, int dosage, int freq, LocalTime time, String note) {
        super(name);
        this.dosage = dosage;
        this.freq = Frequency(freq);
        this.time = time;
        this.note = note;
    }

    public SetTime(int time)

}
