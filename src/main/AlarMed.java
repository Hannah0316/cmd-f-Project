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
   

    while(this.isProgramRunning) {
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

     public void addNewPatient(){
        System.out.println("Please enter the patient name:");
        String name = this.scanner.nextLine();
        patient = new Patient(name);
        System.out.println("\n Welcome patient");

     }

     public void addNewPill(){
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
        LocalDate startDate =  LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        ArrayList<LocalTime> timeList = getListTime();
        patient.addPill(name, dosage, freq, startDate, endDate, timeList);
     }

     public ArrayList<LocalTime> getListTime(){
        System.out.println("Please enter the time to take medication, in military time (e.g 14:25)");
        System.out.println(" enter 'end' once you have entered all the time");
        String input = "";
        ArrayList<LocalTime> timeList = new ArrayList<>();
        while (input != "end") {
            input = this.scanner.nextLine();
            LocalTime time = LocalTime.parse(input);
            if (!timeList.contains(time)){
            timeList.add(time);
            } else {
                System.out.println("duplicate time entered");
            }
        }
        return timeList;
     }

     public void deletePill() {
        System.out.println("Please enter the medication to be removed:");
        String name = this.scanner.nextLine();
        System.out.println("Confirm? y/n");
        String con = this.scanner.nextLine();
        if (con =="y") {
            patient.removePill(name);
        }
        System.out.println("Pill Removed");
     }


     public void displayMenu() {
        System.out.println("Hello"+ patient.getPatientName()+"! Please select an option:\n");
        System.out.println("v: View all Pills");
        System.out.println("b: Add new pill");
        System.out.println("r: Remove pill");
        System.out.println("q: Quite the program");
     }

     public void processMenuCommands(String input) {
        switch (input) {
           case "new":
              this.makeNewCategory();
              return;
           case "load":
              this.loadCategories();
              return;
           case "quit":
              this.quit();
              return;
           case "save":
              this.saveCategories();
              return;
           case "view":
              this.viewCategories();
              return;
        }
  
        System.out.println("Invalid. Please try again.");
     }

    
    }


