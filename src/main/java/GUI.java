import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GUI extends JFrame {

    static final int WIDTH = 1000;
    static final int HEIGHT = 500;

    private JobList listOfJobs = new JobList();

    public String currentTime = new SimpleDateFormat("HH:mm")
            .format(new Date(System.currentTimeMillis()));
    private JLabel labelTime = new JLabel(currentTime, SwingConstants.CENTER);

    GUI(){
        super(jobToDo.appName);

        /* Date & Time */
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        String date = formatDate.format(new Date());

        /* Layout JPanels JFrames*/
        JPanel mainGridPanel = new JPanel();
        JPanel flowLayoutLeft = new JPanel();
        JPanel gridLayoutRight = new JPanel();

        /* Set system default layout */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception ex){
            System.err.println("Couldn't change default view!");
            throw new RuntimeException();
        }

        /* Left side of app (job list) */
        flowLayoutLeft.setLayout(new BorderLayout());
        flowLayoutLeft.setBackground(Color.decode(jobToDo.secondaryColor));

        /* Scroll jobs pane */
        JPanel jp = new JPanel();
        listOfJobs.setDefaultData();
        jp.add(listOfJobs.getJobList());
        flowLayoutLeft.add(BorderLayout.CENTER, new JScrollPane(jp));

        /* Right app panel */
        gridLayoutRight.setLayout(new GridLayout(8,1));

        gridLayoutRight.add(labelTime, SwingConstants.CENTER);
        labelTime.setText("<html><span style='font-weight: bold; font-size: 20px;'>"+ currentTime +"</span></html>");
        gridLayoutRight.add(new JLabel("Today is " + date,  SwingConstants.CENTER));


        /* Add button */
        JButton saveData = new JButton("Save jobs");
        JButton buttonAdd = new JButton("Add");
        JButton readData = new JButton("Read jobs from file");
        JLabel infoLabel = new JLabel("<html><p style='margin: 10px'>Left click - Delete job.<br/>" +
                "Right click - Edit job.<br />" +
                "Before you exit program, be sure pressed 'Save jobs'.</p></html>");

        buttonAdd.addActionListener(e -> popupWindowAdd());
        gridLayoutRight.add(buttonAdd);
        gridLayoutRight.add(new JLabel(""));
        gridLayoutRight.add(readData);
        readData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Specify file to read");
                FileNameExtensionFilter jobFilter = new FileNameExtensionFilter("jobs files (*.jobs)","jobs");
                fileChooser.setFileFilter(jobFilter);
                fileChooser.setCurrentDirectory(new File(SaveData.pathToFile));
                int fileToRead = fileChooser.showSaveDialog(GUI.this);
                if(fileToRead == JFileChooser.APPROVE_OPTION){
                    File fileRead = fileChooser.getSelectedFile();
                    ImportData impotedData = new ImportData(fileRead);

                    Thread readThread = new Thread(impotedData);
                    readThread.start();
                    try {
                        readThread.join();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    listOfJobs.update(impotedData.getList());
                }
            }
        });
        gridLayoutRight.add(saveData);
        saveData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Thread saveThread = new Thread(new SaveData(listOfJobs.getJobs()));
                saveThread.start();
                try {
                    saveThread.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        gridLayoutRight.add(infoLabel);

        /* Main app panel */
        GridLayout gridLayout = new GridLayout(1,2);

        mainGridPanel.setLayout(gridLayout);
        mainGridPanel.add(flowLayoutLeft);
        mainGridPanel.add(gridLayoutRight);

        /* Refresh time every 1s */
        int delay = 1000; //ms
        ActionListener refresh = evt -> {

            currentTime = new SimpleDateFormat("HH:mm")
                    .format(new Date(System.currentTimeMillis()));

            labelTime.setText("<html><span style='font-weight: bold; font-size: 20px;'>"+ currentTime +"</span></html>");

            mainGridPanel.repaint();
            mainGridPanel.revalidate();
        };

        /* Start a timer */
        new Timer(delay, refresh).start();

        /* Add to tray functionality */

        /* System tray */
        if(!SystemTray.isSupported()){
            System.out.println("System tray is not supported.");
            return ;
        }
        SystemTray systemTray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage("./images/favicon.png");

        PopupMenu trayPopupMenu = new PopupMenu();

        MenuItem action = new MenuItem("Show");
        action.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               setVisible(true);
               toFront();
               setState(Frame.NORMAL);
            }
        });
        trayPopupMenu.add(action);

        MenuItem close = new MenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayPopupMenu.add(close);

        TrayIcon trayIcon = new TrayIcon(image, jobToDo.appName, trayPopupMenu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(e.getClickCount() >= 2){
                    setVisible(true);
                    toFront();
                    setState(Frame.NORMAL);
                }
            }
        });

        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if(e.getNewState()==ICONIFIED){
                    try {
                        systemTray.add(trayIcon);
                        setVisible(false);
                        System.out.println("Added to SystemTray");
                    } catch (AWTException ex) {
                        System.out.println("Unable to add to tray");
                    }
                }
                if(e.getNewState()==7){
                    try{
                        systemTray.add(trayIcon);
                        setVisible(false);
                        System.out.println("added to SystemTray");
                    }catch(AWTException ex){
                        System.out.println("Unable to add to tray");
                    }
                }
                if(e.getNewState()==MAXIMIZED_BOTH){
                    systemTray.remove(trayIcon);
                    setVisible(true);
                    System.out.println("Tray icon removed");
                }
                if(e.getNewState()==NORMAL){
                    systemTray.remove(trayIcon);
                    setVisible(true);
                    System.out.println("Tray icon removed");
                }
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(jobToDo.marginProgram);
        this.add(mainGridPanel);
        this.setSize(WIDTH,HEIGHT);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocation(150,50);
        try{
            this.setIconImage(ImageIO.read(new File("./images/favicon.png")));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void popupWindowAdd(){
        JFrame addFrame = new JFrame("A new job");

        Container content = addFrame.getContentPane();
        SpringLayout sL = new SpringLayout();

        JLabel nameLabel = new JLabel("Type job name below:");
        JLabel hourLabel = new JLabel("Type hour (format eg. 13:23) below:");
        JLabel warningLabel = new JLabel("<html><p style='color: red;'>You trying to add a wrong data! Check it again please.</p></html>");
        JTextField name = new JTextField(50);
        JTextField hour = new JTextField(5);
        JButton addButton = new JButton("Add");

        content.add(nameLabel); content.add(name);
        content.add(hourLabel); content.add(hour);

        content.add(warningLabel); warningLabel.setVisible(false);

        content.add(addButton);

        sL.putConstraint(SpringLayout.WEST, nameLabel,10,SpringLayout.WEST, content);
        sL.putConstraint(SpringLayout.NORTH, nameLabel,10,SpringLayout.WEST, content);
        sL.putConstraint(SpringLayout.WEST, name,10,SpringLayout.WEST, content);
        sL.putConstraint(SpringLayout.NORTH, name,20,SpringLayout.NORTH, nameLabel);
        sL.putConstraint(SpringLayout.NORTH, hourLabel,40,SpringLayout.NORTH, name);
        sL.putConstraint(SpringLayout.NORTH, hour,20,SpringLayout.NORTH, hourLabel);
        sL.putConstraint(SpringLayout.WEST, hour,10,SpringLayout.WEST, content);
        sL.putConstraint(SpringLayout.WEST, hourLabel,10,SpringLayout.WEST, content);
        sL.putConstraint(SpringLayout.WEST, addButton,50,SpringLayout.WEST, hour);
        sL.putConstraint(SpringLayout.NORTH, addButton,18,SpringLayout.NORTH, hourLabel);
        sL.putConstraint(SpringLayout.NORTH, warningLabel,28,SpringLayout.NORTH, hour);
        sL.putConstraint(SpringLayout.WEST, warningLabel,10,SpringLayout.WEST, hour);

        content.setLayout(sL);
        addFrame.setBounds(jobToDo.marginProgram);
        addFrame.setSize(new Dimension(500,200));
        addFrame.setVisible(true);

        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                String nameJob = name.getText();
                String hourJob = hour.getText();
                InputValidator inputValidator = new InputValidator(nameJob, hourJob);
                if (inputValidator.isLabelGood() && inputValidator.isHourGood()){
                    listOfJobs.addToList(nameJob, hourJob);
                    warningLabel.setVisible(false);
                    addFrame.dispose();
                }else {
                    warningLabel.setVisible(true);
                }

            }
        });
    }

}