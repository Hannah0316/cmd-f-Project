package main;

import java.time.LocalDate;
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

     public void initiate() {

        this.scanner = new Scanner(System.in);
        patient = new Patient("");
        this.isProgramRunning = true;
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
        String dosage = this.scanner.nextLine();
        System.out.println("Please enter the medication frequency (1= once per day, 7=weekly):");
        String freq = this.scanner.nextLine();
        System.out.println("Please enter the start date (yyyy-MM-dd):");
        String start = this.scanner.nextLine();
        System.out.println("Please enter the end date (yyyy-MM-dd):");
        String end = this.scanner.nextLine();
        LocalDate startDate =  LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        patient.addPill(name, dosage, freq, start, end);
     }

     public void deletePill() {
        System.out.println("Please enter the medication to be removed:");
        String name = this.scanner.nextLine();
        Pill pill = getPillInfo(name);
        System.out.println("Confirm? y/n");
        String con = this.scanner.nextLine();
        if (con =="y") {
            removePill(pill);
        }
        System.out.println("Pill Removed");
        
     }
    
     public void 

    
}
