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
import java.awt.Toolkit;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

import model.*;

public class AlarMedUI extends JFrame implements ActionListener {

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
    private int currentHour;
    private int currentMinute;
    String alert = "beep-125033.wav";

    public AlarMedUI() {
        super("AlarMed");
        this.initiate();
        list.setModel(model);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 800));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.getBtn();

        // Run machine on a separate thread
        runMachine();
    }

    public void runMachine() {
        while (isProgramRunning) {
            try {
                System.out.println("Waiting for a minute...");
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            update();
            System.out.println();
        }
    }

    /*
     * EFFECTS: runMachine when the patient should eat a pill
     */
    public void update() {
        System.out.println("Checking time...");
        currentDay = LocalDate.now();
        currentTime = LocalTime.now();
        currentHour = currentTime.getHour();
        currentMinute = currentTime.getMinute();
        ArrayList<Pill> pills = patient.getPills();
        boolean droppedPill = false;
        for (Pill p : pills) {
            if (p.getNextIntakeDate().equals(currentDay)) {
                ArrayList<LocalTime> times = p.getTimes();
                for (LocalTime time : times) {
                    int hour = time.getHour();
                    int minute = time.getMinute();
                    if (currentHour == hour && currentMinute == minute) {
                        int count = 0;
                        while (count != p.getDosage()) {
                            releasePill(p);
                            count++;
                            droppedPill = true;
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            checkAndRemovePill(p, currentDay);
        }

        if (!droppedPill) {
            System.out.println("No pill has dropped at " + currentHour + ":" + currentMinute);
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

    public void releasePill(Pill p) {
        // output sound
        AudioInputStream audioInputStream;
        System.out.println("Alert at " + currentHour + ":" + currentMinute);
        System.out.println("    >>> Please press the blue button to drop a " + p.getName());
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(alert).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }

        JOptionPane.showMessageDialog((Component) null, "Eat your pills! " + patient.getPatientName());

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
        this.addBtn(newBtn, viewBtn, addBtn);
    }

    private void addBtn(JButton newBtn, JButton viewBtn, JButton addBtn) {
        this.add(newBtn);
        this.add(viewBtn);
        this.add(addBtn);
        this.pack();
        this.setLocationRelativeTo((Component) null);
        this.setVisible(true);
        this.setResizable(false);
    }

    public void initiate() {

        this.scanner = new Scanner(System.in);
        patient = new Patient("");
        this.isProgramRunning = true;
        this.index = 0;
        newPatientUI();
        addPillUI();
    }

    public void newPatientUI() {
        String name = JOptionPane.showInputDialog("Please enter the patient name:");
        patient = new Patient(name);
        JOptionPane.showMessageDialog((Component) null, "Welcome! " + name);
        this.pack();
        this.setLocationRelativeTo((Component) null);
        this.setVisible(true);
        this.setResizable(false);

    }

    public void addPillUI() {
        String name = JOptionPane.showInputDialog("Please enter the medication name:");
        int dosage = Integer.valueOf(JOptionPane.showInputDialog("Please enter the medication dosage:"));
        int freq = Integer.valueOf(
                JOptionPane.showInputDialog("Please enter the medication frequency (1= once per day, 7=weekly):"));
        String start = JOptionPane.showInputDialog("Please enter the start date (yyyy-MM-dd):");
        String end = JOptionPane.showInputDialog("Please enter the end date (yyyy-MM-dd):");
        String note = JOptionPane.showInputDialog("Please any notes:");
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        ArrayList<LocalTime> time = new ArrayList<>();
        time = getListTimeUI(time);
        patient.addPill(name, dosage, freq, startDate, endDate, time, note);
        JOptionPane.showMessageDialog((Component) null, "medication: " + name + " has ben added");
        this.pack();
        this.setLocationRelativeTo((Component) null);
        this.setVisible(true);
        this.setResizable(false);
    }

    private ArrayList<LocalTime> getListTimeUI(ArrayList<LocalTime> time) {
        Boolean flag = false;
        while (!flag) {
            String input = JOptionPane
                    .showInputDialog("Please enter the time to take medication, in military time (e.g 14:25)");
            LocalTime t = LocalTime.parse(input);
            if (!time.contains(t)) {
                time.add(t);
                JOptionPane.showMessageDialog((Component) null, "added time");
            } else {
                JOptionPane.showMessageDialog((Component) null, "duplicate time entered");
            }
            input = JOptionPane.showInputDialog("Add another time? y/n");
            if (input.equalsIgnoreCase("n")) {
                flag = true;
            }
        }
        return time;
    }

    public void viewAllPillsUI() {

        ArrayList<String> pillList = new ArrayList<>();
        for (Pill p : this.patient.getPills()) {
            String name = p.getName();
            pillList.add(name);
        }
        JList<String> list = getList(pillList);

        getListScroller(list);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private JButton getCloseBtn(JScrollPane scroller, JButton btn1, JButton btn2, AlarMedUI ui) {
        JButton closeBtn = new JButton("Close Scroller");
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.remove(scroller);
                btn1.setVisible(false);
                btn2.setVisible((false));
                closeBtn.setVisible(false);
                revalidate(); // Refresh the layout
                repaint(); // Redraw the UI
            }
        });
        return closeBtn;
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
        listScroller.setVisible(true);

        JButton deletePillBtn = getDeletePillBtn(list);
        JButton moreInfoBtn = getMoreInfoBtn(list);
        JButton closeBtn = getCloseBtn(listScroller, deletePillBtn, moreInfoBtn, this);

        add(deletePillBtn);
        add(moreInfoBtn);
        add(closeBtn);

        add(listScroller);
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
        moreInfoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                index = list.getSelectedIndex();

                pill = patient.getPillIndex(index);
                moreInfoUI();
            }
        });
        return moreInfoBtn;
    }

    public void deletePillUI() {
        this.patient.removePill(this.pill);
    }

    public void moreInfoUI() {
        JLabel nameField = new JLabel("name: " + this.pill.getName());
        JLabel dosageField = new JLabel("dosage: " + String.valueOf(this.pill.getDosage()));
        JLabel freqField = new JLabel("frequency: " + String.valueOf(this.pill.getFreq()));
        JLabel nextIntakeField = new JLabel(
                "next intake date: " + String.valueOf(this.pill.getNextIntakeDate().toString()));
        JLabel startField = new JLabel("start date: " + String.valueOf(this.pill.getStartDate().toString()));
        JLabel endField = new JLabel("end date: " + String.valueOf(this.pill.getEndDate().toString()));
        JLabel noteField = new JLabel("note: " + this.pill.getNote());
        JLabel timeField = new JLabel("time: " + this.pill.getTimes());
        JButton closeInfoBtn = getInfoCloseBtn(nameField, dosageField, freqField, nextIntakeField, startField, endField,
                noteField, timeField);
        this.add(nameField);
        this.add(dosageField);
        this.add(freqField);
        this.add(nextIntakeField);
        this.add(startField);
        this.add(endField);
        this.add(noteField);
        this.add(timeField);
        this.add(closeInfoBtn);
        this.pack();
        this.setLocationRelativeTo((Component) null);
        this.setVisible(true);
        this.setResizable(false);

    }

    private JButton getInfoCloseBtn(JLabel f1, JLabel f2, JLabel f3, JLabel f4, JLabel f5, JLabel f6, JLabel f7,
            JLabel f8) {
        JButton closeInfoBtn = new JButton("Close Info");
        closeInfoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f1.setVisible(false);
                f2.setVisible(false);
                f3.setVisible(false);
                f4.setVisible(false);
                f5.setVisible(false);
                f6.setVisible(false);
                f7.setVisible(false);
                f8.setVisible(false);
                closeInfoBtn.setVisible(false);
                revalidate(); // Refresh the layout
                repaint(); // Redraw the UI
            }
        });
        return closeInfoBtn;
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