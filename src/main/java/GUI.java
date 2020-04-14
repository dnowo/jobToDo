import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;



public class GUI extends JFrame {
    private Rectangle marginProgram = new Rectangle(130,130,0,0);
    static final int WIDTH = 1000;
    static final int HEIGHT = 500;

    JobList listOfJobs = new JobList();

    GUI(){
        super("Jobs To Do");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(marginProgram);

        /* Date & Time */
        SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        String date = formatDate.format(new Date());
        String time = formatHour.format(new Date());

        /* Layout JPanels JFrames*/
        JPanel mainGridPanel = new JPanel();
        JPanel flowLayoutLeft = new JPanel();
        JPanel gridLayoutRight = new JPanel();

        JLabel labelTime = new JLabel(time, SwingConstants.CENTER);

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

        /*Scroll jobs pane*/
        JPanel jp = new JPanel();
        listOfJobs.addToList("xD","14:00");
        jp.add(listOfJobs.getJobList());
        flowLayoutLeft.add(BorderLayout.CENTER, new JScrollPane(jp));

        /* Right app panel */
        gridLayoutRight.setLayout(new GridLayout(8,1));
        gridLayoutRight.add(new JLabel("Dziś jest " + date,  SwingConstants.CENTER));
        gridLayoutRight.add(labelTime);
        gridLayoutRight.add(new Label(System.getProperty("user.name") + " what's your works today?", SwingConstants.CENTER));
        gridLayoutRight.add(new JButton("Read from file..."));

        /* Add button */
        JButton buttonAdd = new JButton("Add");
        buttonAdd.addActionListener(e -> {
            popupWindowAdd();
        });
        gridLayoutRight.add(buttonAdd);

        /*
            Main app panel
        */
        GridLayout gridLayout = new GridLayout(1,2);

        mainGridPanel.setLayout(gridLayout);
        mainGridPanel.add(flowLayoutLeft);
        mainGridPanel.add(gridLayoutRight);

        /* Refresh time every 1s */
        int delay = 1000; //ms
        ActionListener refresh = evt -> {
            String time1 = new SimpleDateFormat("HH:mm:ss")
                    .format(new Date(System.currentTimeMillis()));
            labelTime.setText(time1);
            mainGridPanel.repaint();
            mainGridPanel.revalidate();
        };

        /* Start a timer */
        new Timer(delay, refresh).start();

        this.add(mainGridPanel);
        this.setSize(WIDTH,HEIGHT);
        this.setVisible(true);
        this.setLocation(150,50);

    }

    private void popupWindowAdd(){
        JFrame addFrame = new JFrame("A new job");

        Container content = addFrame.getContentPane();
        SpringLayout sL = new SpringLayout();

        JLabel nameLabel = new JLabel("Type job name below:");
        JLabel hourLabel = new JLabel("Type hour (format eg. 13:23) below:");
        JTextField name = new JTextField(50);
        JTextField hour = new JTextField(5);
        JButton addButton = new JButton("Add");

        content.add(nameLabel);
        content.add(name);
        content.add(hourLabel);
        content.add(hour);
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

        content.setLayout(sL);
        addFrame.setBounds(marginProgram);
        addFrame.setSize(new Dimension(500,200));
        addFrame.setVisible(true);

        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String nameJob = name.getText();
                String hourJob = hour.getText();
                listOfJobs.addToList(nameJob,hourJob);

                System.out.println(name.getText());
                System.out.println(hour.getText());
            }
        });

    }



}
