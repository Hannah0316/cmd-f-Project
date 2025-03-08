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
        patient = new Patient("", LocalDate.now(), LocalDate.now());
        this.isProgramRunning = true;
     }

     public void handleMenu() {
        this.displayMenu();
        String input = this.scanner.nextLine();
        this.processMenuCommands(input);
     }

     public 

    
}
