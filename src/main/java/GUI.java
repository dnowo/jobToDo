import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class GUI extends JFrame {

    static final int WIDTH = 1000;
    static final int HEIGHT = 500;

    private JobList listOfJobs = new JobList();

    public String currentTime = new SimpleDateFormat("HH:mm")
            .format(new Date(System.currentTimeMillis()));
    private JLabel labelTime = new JLabel(currentTime, SwingConstants.CENTER);
    private TrayIcon trayIcon;

    GUI(){
        super(jobToDo.appName);

        /* Date & Time */
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        String date = formatDate.format(new Date());
        labelTime.setForeground(Color.white);

        /* Layout JPanels JFrames*/
        JPanel mainGridPanel = new JPanel();
        JPanel borderLayoutLeft = new JPanel();
        JPanel gridLayoutRight = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try{
                    g.drawImage(ImageIO.read(new File("./images/rightGridBackground.png")), 0, 0, null);

                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        /* Set system default layout */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception ex){
            System.err.println("Couldn't change default view!");
            throw new RuntimeException();
        }

        /* Left side of app (job list) */
        BorderLayout bL = new BorderLayout();
        borderLayoutLeft.setLayout(bL);
        borderLayoutLeft.setBackground(Color.BLACK);

        /* Scroll jobs pane */
        JPanel jp = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try{
                    BufferedImage image = ImageIO.read(new File("./images/leftGridBackground.png"));
                    int iw = image.getWidth(this);
                    int ih = image.getHeight(this);
                    if (iw > 0 && ih > 0) {
                        for (int x = 0; x < getWidth(); x += iw) {
                            for (int y = 0; y < getHeight(); y += ih) {
                                g.drawImage(image, x, y, iw, ih, this);
                            }
                        }
                    }

                }catch(IOException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void setBackground(Color bg) {
                super.setBackground(Color.BLACK);
            }
        };
        listOfJobs.setDefaultData();
        jp.add(listOfJobs.getJobList());
        borderLayoutLeft.add(BorderLayout.CENTER, new JScrollPane(jp));

        /* Right app panel */
        gridLayoutRight.setLayout(new GridLayout(8,1));
        gridLayoutRight.add(labelTime, SwingConstants.CENTER);
        labelTime.setText("<html><span style='font-weight: bold; font-size: 20px;'>"+ currentTime +"</span></html>");
        JLabel dateLabel = new JLabel("Today is " + date,  SwingConstants.CENTER);
        dateLabel.setForeground(Color.white);
        gridLayoutRight.add(dateLabel);


        /* Add button */
        JLabel saveData = new JLabel();
        JLabel deleteData = new JLabel();
        JLabel buttonAdd = new JLabel();
        JLabel readData = new JLabel();
        JLabel infoLabel = new JLabel();

        try{
            buttonAdd = new JLabel(new ImageIcon(ImageIO.read(new File("./images/buttonAdd.png"))));
            deleteData = new JLabel(new ImageIcon(ImageIO.read(new File("./images/buttonDelete.png"))));
            saveData = new JLabel(new ImageIcon(ImageIO.read(new File("./images/buttonSave.png"))));
            readData = new JLabel(new ImageIcon(ImageIO.read(new File("./images/buttonRead.png"))));
            infoLabel = new JLabel(new ImageIcon(ImageIO.read(new File("./images/infoLabel.png"))));
        }catch(IOException e){
            e.printStackTrace();
        }
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                popupWindowAdd();
            }
        });

        gridLayoutRight.add(new JLabel(""));
        gridLayoutRight.add(buttonAdd);
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

        gridLayoutRight.add(deleteData);
        gridLayoutRight.add(infoLabel);
        deleteData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                java.util.List<ListItem> toEmpty = listOfJobs.getJobs();
                toEmpty.clear();
                listOfJobs.update(toEmpty);
            }
        });
        /* Main app panel */
        GridLayout gridLayout = new GridLayout(1,2);

        mainGridPanel.setLayout(gridLayout);
        mainGridPanel.add(borderLayoutLeft);
        mainGridPanel.add(gridLayoutRight);

        /* Add to tray functionality */
        if(!SystemTray.isSupported()){
            System.out.println("System tray is not supported.");
            return;
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
                systemTray.remove(trayIcon);
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

        trayIcon = new TrayIcon(image, jobToDo.appName, trayPopupMenu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(e.getClickCount() == 2){
                    setVisible(true);
                    toFront();
                    setState(Frame.NORMAL);
                    systemTray.remove(trayIcon);
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

        /* Refresh time every 1s */
        int delay = 1000; //ms
        Calendar now = Calendar.getInstance();
        int secs = now.get(Calendar.SECOND);
        AtomicInteger checkTime = new AtomicInteger(Math.abs(60-secs)); //s

        ActionListener refresh = evt -> {
            currentTime = new SimpleDateFormat("HH:mm")
                    .format(new Date(System.currentTimeMillis()));
            labelTime.setText("<html><span style='font-weight: bold; font-size: 20px;'>"+ currentTime +"</span></html>");

            checkTime.getAndDecrement();
            String label,hour;
            if(checkTime.get() == 0) {
                ListItem item = Notifications.notify(listOfJobs.getJobs());
                if(item != null) {
                    label = item.getLabel();
                    hour = item.getHour();
                    trayIcon.displayMessage(label, hour, TrayIcon.MessageType.NONE);
                }
                checkTime.set(30);
            }

            mainGridPanel.repaint();
            mainGridPanel.revalidate();
        };


        /* Start a timer */
        new Timer(delay, refresh).start();

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
        try {
            BufferedImage img = ImageIO.read(new File("./images/popupBackground.png"));
            addFrame.setContentPane(new ImagePrinter(img));
        } catch (IOException e){
            e.printStackTrace();
        }
        Container content = addFrame.getContentPane();
        SpringLayout sL = new SpringLayout();

        JLabel nameLabel = new JLabel("Type job name below:");
        JLabel hourLabel = new JLabel("Type hour (format eg. 13:23) below:");
        JLabel warningLabel = new JLabel("<html><p style='color: red; font-weight: bold;'>You trying to add a wrong data! Check it again please.</p></html>");
        JTextField name = new JTextField(50);
        JTextField hour = new JTextField(5);
        JButton addButton = new JButton("Add");
        hourLabel.setForeground(Color.white);
        nameLabel.setForeground(Color.white);

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