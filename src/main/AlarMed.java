package main;

import java.awt.Component;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale.Category;
import java.util.Scanner;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.*;

public class AlarMed {

    private Patient patient;
    private Scanner scanner;
    private boolean isProgramRunning;
    private Pill pill;
    private LocalDate currentDay;
    private LocalTime currentTime;

    public AlarMed() {
        initiate();
        addNewPatient();
        addNewPill();
        // Run user interface on a separate thread
        new Thread(this::runUserInterface).start();

        // Run machine on a separate thread
        runMachine();
    }

    public void runMachine() {
        while (!patient.getPills().isEmpty()) {
            try {
                System.out.println("Waiting for a minute...");
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            update();
            System.out.println();
        }
        System.out.println("No more pills to take!");
    }

    public void runUserInterface() {
        while (this.isProgramRunning) {
            this.handleMenu();
        }
    }

    /*
     * EFFECTS: runMachine when the patient should eat a pill
     */
    public void update() {
        System.out.println("Checking time...");
        currentDay = LocalDate.now();
        ArrayList<Pill> pills = patient.getPills();
        for (Pill p : pills) {
            if (p.getNextIntakeDate().equals(currentDay)) {
                currentTime = LocalTime.now();
                int currentHour = currentTime.getHour();
                int currentMinute = currentTime.getMinute();
                ArrayList<LocalTime> times = p.getTimes();
                for (LocalTime time : times) {
                    int hour = time.getHour();
                    int minute = time.getMinute();
                    if (currentHour == hour && currentMinute == minute) {
                        int count = 0;
                        while (count != p.getDosage()) {
                            releasePill();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            System.out.println("Dropped a " + p.getName() + " at " + currentHour + ":" + currentMinute);
                            count++;
                        }
                    }
                }
            }
            checkAndRemovePill(p, currentDay);
        }
    }

    /*
     * EFFECT: remove pill from patient if today is the endDate of the pill
     */
    public void checkAndRemovePill(Pill pill, LocalDate date) {
        int endDay = pill.getEndDate().getDayOfYear();
        int today = date.getDayOfYear();
        if (endDay == today) {
            patient.removePill(pill);
            String pillName = pill.getName();
            System.out.println("Finished " + pillName + "!");
        }
    }

    public void releasePill() {
        System.out.println("Running machine!");
    }

    public void initiate() {

        this.scanner = new Scanner(System.in);
        patient = new Patient("");
        this.isProgramRunning = true;
    }

    public void quit() {
        System.out.println("Goodbye");
        this.isProgramRunning = false;
    }

    public void handleMenu() {
        this.displayMenu();
        String input = this.scanner.nextLine();
        this.processMenuCommands(input);
    }

    public void addNewPatient() {
        System.out.println("Please enter the patient name:");
        String name = this.scanner.nextLine();
        patient = new Patient(name);
        System.out.println("\n Welcome patient");

    }

    public void addNewPill() {
        System.out.println("Please enter the medication name:");
        String name = this.scanner.nextLine();
        System.out.println("Please enter the medication dosage:");
        int dosage = Integer.valueOf(this.scanner.nextLine());
        System.out.println("Please enter the medication frequency (1= once per day, 7=weekly):");
        int freq = Integer.valueOf(this.scanner.nextLine());
        System.out.println("Please enter the start date (yyyy-MM-dd):");
        String start = this.scanner.nextLine();
        System.out.println("Please enter the end date (yyyy-MM-dd):");
        String end = this.scanner.nextLine();
        System.out.println("Please enter any notes:");
        String note = this.scanner.nextLine();
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        ArrayList<LocalTime> time = new ArrayList<>();
        ArrayList<LocalTime> timeList = getListTime(time);
        patient.addPill(name, dosage, freq, startDate, endDate, timeList, note);
    }

    private ArrayList<LocalTime> getListTime(ArrayList<LocalTime> time) {
        Boolean flag = false;
        while (!flag) {
            System.out.println("Please enter the time to take medication, in military time (e.g 14:25)");
            String input = this.scanner.nextLine();
            LocalTime t = LocalTime.parse(input);
            if (!time.contains(t)) {
                time.add(t);
                System.out.println("added time");
            } else {
                System.out.println("duplicate time entered");
            }
            System.out.println("Add another time? y/n");
            input = this.scanner.nextLine();
            if (input.equalsIgnoreCase("n")) {
                flag = true;
            }
        }
        return time;
    }

    public void viewAllPills() {
        ArrayList<Pill> pillList = patient.getPills();
        for (Pill pill : pillList) {
            System.out.println("medication name: " + pill.getName());
        }

    }

    public void deletePill() {
        System.out.println("Please enter the medication to be removed:");
        String name = this.scanner.nextLine();
        System.out.println("Confirm? y/n");
        String con = this.scanner.nextLine();
        if (con.equals("y")) {
            patient.removePill(name);
        }
        System.out.println("Pill Removed");
    }

    public void inspectPill() {
        System.out.println("Please enter the medication to be inspected:");
        String name = this.scanner.nextLine();
        Pill pill = patient.getPill(name);
        if (null != pill) {
            pillInfo(pill);
        } else {
            System.out.println("invalid pill");
        }

    }

    public void pillInfo(Pill pill) {
        System.out.println("name: " + pill.getName());
        System.out.println("Dosage: " + pill.getDosage());
        System.out.println("Frequency: " + pill.getFreq());
        System.out.println("Next Pill: " + pill.getNextIntakeDate().toString());
        System.out.println("Start Date: " + pill.getStartDate().toString());
        System.out.println(("End Date: " + pill.getEndDate()));
        System.out.println(("Time: "));
        ArrayList<LocalTime> time = pill.getTimes();
        for (LocalTime t : time) {
            System.out.println(t.toString());
        }

    }

    public void displayMenu() {
        System.out.println("Hello " + patient.getPatientName() + "! Please select an option:\n");
        System.out.println("r: Register Patient");
        System.out.println("v: View all Pills");
        System.out.println("b: Add new pill");
        System.out.println("d: Delete pill");
        System.out.println("i: Inspect pill");
        System.out.println("q: Quit the program");
    }

    public void processMenuCommands(String input) {
        switch (input) {
            case "r":
                this.addNewPatient();
                return;
            case "v":
                this.viewAllPills();
                return;
            case "b":
                this.addNewPill();
                return;
            case "d":
                this.deletePill();
                return;
            case "i":
                this.inspectPill();
                return;
            case "q":
                this.quit();
                return;
        }

        System.out.println("Invalid. Please try again.");
    }

}
