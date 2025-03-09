package main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import model.*;

public class AlarMed {

    private Patient patient;
    private Scanner scanner;
    private boolean isProgramRunning;

    public AlarMed() {
        this.initiate();

        while (this.isProgramRunning) {
            this.handleMenu();
        }
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
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        ArrayList<LocalTime> time = new ArrayList<>();
        ArrayList<LocalTime> timeList = getListTime(time);
        patient.addPill(name, dosage, freq, startDate, endDate, timeList);
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
        System.out.println("Hello" + patient.getPatientName() + "! Please select an option:\n");
        System.out.println("r: Register Patient");
        System.out.println("v: View all Pills");
        System.out.println("b: Add new pill");
        System.out.println("d: Delete pill");
        System.out.println("i: Inspect pill");
        System.out.println("q: Quite the program");
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
