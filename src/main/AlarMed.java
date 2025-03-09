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

public class AlarMed extends JFrame implements ActionListener{

    private Patient patient;
    private Scanner scanner;
    private boolean isProgramRunning;
    private Pill pill;
    private int index;
    private JComboBox<String> printCombo;
    private JList<Category> list = new JList();
    private DefaultListModel<Category> model = new DefaultListModel<>();
    private JSplitPane splitPane = new JSplitPane();
    private JPanel panel = new JPanel();
    private LocalDate currentDay;
    private LocalTime currentTime;

    public AlarMed() {
        super("AlarMed");
        this.initiate();
        list.setModel(model);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 800));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.getBtn();

        // Run user interface on a separate thread
        new Thread(this::runUserInterface).start();

        // Run machine on a separate thread
        new Thread(this::runMachine).start();
    }

    public void runMachine() {
        while (!patient.getPills().isEmpty()) {
            try {
                System.out.println("running");
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            update();
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
        currentDay = LocalDate.now();
        ArrayList<Pill> pills = patient.getPills();
        for (Pill p : pills) {
            if (p.getNextIntakeDate() == currentDay) {
                currentTime = LocalTime.now();
                int currentHour = currentTime.getHour();
                int currentMinute = currentTime.getMinute();
                ArrayList<LocalTime> times = p.getTimes();
                for (LocalTime time : times) {
                    int hour = time.getHour();
                    int minute = time.getMinute();
                    if (currentHour == hour && currentMinute == minute) {
                        releasePill();
                        System.out.println("Dropped a " + p.getName());
                    }
                }
                System.out.println("No pill times equal current time");
            }
            checkAndRemovePill(p, currentDay);
        }
        System.out.println("Nothing to eat today");
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

    private void getBtn() {
      JButton newBtn = new JButton("New User");
      newBtn.setActionCommand("new");
      newBtn.addActionListener(this);
      JButton viewBtn = new JButton("View Pills");
      viewBtn.setActionCommand("view");
      viewBtn.addActionListener(this);
      JButton addBtn = new JButton("Add Pill");
      addBtn.setActionCommand("add");
      addBtn.addActionListener(this);
      JButton removeBtn = new JButton("Remove Pill");
      removeBtn.setActionCommand("remove");
      removeBtn.addActionListener(this);
      JButton carrotBtn = new JButton("Carrot Punching Earth");
      carrotBtn.setActionCommand("carrot");
      carrotBtn.addActionListener(this);
      this.addBtn(newBtn, viewBtn, addBtn, removeBtn, carrotBtn);
   }

   private void addBtn(JButton newBtn, JButton viewBtn, JButton addBtn, JButton removeBtn, JButton carrotBtn) {
    this.add(newBtn);
    this.add(viewBtn);
    this.add(addBtn);
    this.add(removeBtn);
    this.add(carrotBtn);
    this.pack();
    this.setLocationRelativeTo((Component)null);
    this.setVisible(true);
    this.setResizable(false);
 }

    public void initiate() {

        this.scanner = new Scanner(System.in);
        patient = new Patient("");
        this.isProgramRunning = true;
        this.index = 0;
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

    public void newPatientUI() {
      String name = JOptionPane.showInputDialog("Please enter the patient name:");
      patient = new Patient(name);
      JOptionPane.showMessageDialog((Component)null, "Welcome! "+ name);
      this.pack();
      this.setLocationRelativeTo((Component)null);
      this.setVisible(true);
      this.setResizable(false);

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


   public void addPillUI() {
    String name = JOptionPane.showInputDialog("Please enter the medication name:");
    int dosage  = Integer.valueOf(JOptionPane.showInputDialog("Please enter the medication dosage:"));
    int freq = Integer.valueOf(JOptionPane.showInputDialog("Please enter the medication frequency (1= once per day, 7=weekly):"));
    String start = JOptionPane.showInputDialog("Please enter the start date (yyyy-MM-dd):");
    String end = JOptionPane.showInputDialog("Please enter the start date (yyyy-MM-dd):");
    LocalDate startDate = LocalDate.parse(start);
    LocalDate endDate = LocalDate.parse(end);
    ArrayList<LocalTime> time = new ArrayList<>();
    time = getListTimeUI(time);
    patient.addPill(name, dosage, freq, startDate, endDate, time);
    JOptionPane.showMessageDialog((Component)null, "medication: " + name + " has ben added");
    this.pack();
    this.setLocationRelativeTo((Component)null);
    this.setVisible(true);
    this.setResizable(false);
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


    private ArrayList<LocalTime> getListTimeUI(ArrayList<LocalTime> time) {
        Boolean flag = false;
        while (!flag) {
            String input = JOptionPane.showInputDialog("Please enter the time to take medication, in military time (e.g 14:25)");
            LocalTime t = LocalTime.parse(input);
            if (!time.contains(t)) {
                time.add(t);
                JOptionPane.showMessageDialog((Component)null, "added time");
            } else {
                JOptionPane.showMessageDialog((Component)null,"duplicate time entered");
            }
            input = JOptionPane.showInputDialog("Add another time? y/n");
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

    public void viewAllPillsUI() {
     ArrayList<String> pillList = new ArrayList<>();
        for (Pill p : this.patient.getPills()) {
            String name = p.getName();
            pillList.add(name);
        }
        JList<String> list = getList(pillList);

        getListScroller(list);
        JButton deletePillBtn = getDeletePillBtn(list);
        JButton moreInfoBtn = getMoreInfoBtn(list);

        add(deletePillBtn);
        add(moreInfoBtn);

        setLocationRelativeTo(null);
        setVisible(true);

    }

    private JList<String> getList(ArrayList<String> pillList) {
        System.out.println(pillList);
        JList<String> list = new JList(pillList.toArray());
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        list.setVisibleRowCount(-1);
        return list;
    }
  
    private void getListScroller(JList<String> list) {
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        add(listScroller);
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

    private JButton getDeletePillBtn(JList<String> list) {
        JButton deletePillBtn = new JButton("Delete Pill");
        deletePillBtn.setActionCommand("deletePill");
        deletePillBtn.addActionListener(this);
        deletePillBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                index = list.getSelectedIndex();

                pill = patient.getPillIndex(index);
                deletePillUI();
            }
        });
        return deletePillBtn;
     }
  
     private JButton getMoreInfoBtn(JList<String> list) {
        JButton moreInfoBtn = new JButton("More Info");
        moreInfoBtn.setActionCommand("moreInfo");
        moreInfoBtn.addActionListener(this);
        return moreInfoBtn;
     }

    public void deletePillUI() {
        this.patient.removePill(this.pill);
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

    public void moreInfoUI() {
      JLabel nameField = new JLabel("name:" +this.pill.getName());
      JLabel dosageField = new JLabel("dosage:"+String.valueOf(this.pill.getDosage()));
      JLabel freqField = new JLabel("frequency:"+String.valueOf(this.pill.getFreq()));
      JLabel nextIntakeField = new JLabel("next intake date:"+String.valueOf(this.pill.getNextIntakeDate().toString()));
      JLabel startField = new JLabel("start date:"+String.valueOf(this.pill.getStartDate().toString()));
      JLabel endField = new JLabel("end date:"+String.valueOf(this.pill.getEndDate().toString()));
      this.add(nameField);
      this.add(dosageField);
      this.add(freqField);
      this.add(nextIntakeField);
      this.add(startField);
      this.add(endField);
      this.pack();
      this.setLocationRelativeTo((Component)null);
      this.setVisible(true);
      this.setResizable(false);

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

    public void actionPerformed(ActionEvent e) {
  
        if (e.getActionCommand().equals("new")) {
           this.newPatientUI();
        }
  
        if (e.getActionCommand().equals("add")) {
           this.addPillUI();
        }
  
        if (e.getActionCommand().equals("view")) {
           this.viewAllPillsUI();
        }

        if (e.getActionCommand().equals("deletePill")) {
            this.deletePillUI();
         }

         if (e.getActionCommand().equals("moreInfo")) {
            this.moreInfoUI();
         }
     }

}
