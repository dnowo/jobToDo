import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Class creating list of jobs and its logic also Mouse clicks, mouse actions.
 * @author Daniel Nowosielecki
 * @version 1.0.0
 * */
public class JobList {
    private List<ListItem> jobs = new ArrayList<>();
    private JList<Object> jListJobs;
    private JButton editButton;
    private JButton yesButton;
    private JButton noButton;
    private JTextField name;
    private JTextField hour;
    private JTextField priorytet;
    private JCheckBox cykliczne;
    private JFrame popupFrame;

    private boolean isClicked = false;
    /**
     * Constructor sets up job list, its action listeners and data - loading settings.
     */
    JobList(){
        jListJobs = new JList<>(jobs.toArray());

        jListJobs.setCellRenderer(new ListRenderer());
        jListJobs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jListJobs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {

                JList list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint()); // Get index of item
                ListItem item = (ListItem) list.getModel().getElementAt(index);

                if (SwingUtilities.isRightMouseButton(event) && event.getClickCount() == 1) {
                    /* If right pressed - edit item */
                    popupFrame = new JFrame("Edit job");
                    name = new JTextField(50);
                    hour = new JTextField(5);
                    editButton = new JButton("Edit");
                    priorytet = new JTextField("Priorytet",10);
                    popupFrame.setBounds(jobToDo.marginProgram);
                    cykliczne = new JCheckBox("Praca cykliczna? ");
                    cykliczne.setSelected(item.isCykliczne());
                    priorytet.setText(String.valueOf(item.getPriorytet()));
                    name.setText(item.getLabel());
                    hour.setText(item.getHour());

                    popupWindowEdit();
                    editButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            super.mouseClicked(e);
                            jobs.set(index,new ListItem(name.getText(), hour.getText(), cykliczne.isSelected(), Integer.parseInt(priorytet.getText())));
                            // Have to revalidate list again after button pressed.
                            list.setListData(jobs.toArray());
                            list.revalidate();
                            list.repaint();
                            popupFrame.dispose();
                        }
                    });
                } else {
                    /* If left button pressed - delete item. */
                    if(!isClicked){
                        popupWindowDelete();
                        yesButton.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mousePressed(MouseEvent e) {
                                super.mousePressed(e);
                                jobs.remove(index);
                                list.setListData(jobs.toArray());
                                list.revalidate();
                                list.repaint();
                                isClicked = false;
                                popupFrame.dispose();
                            }
                        });
                        noButton.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mousePressed(MouseEvent e) {
                                super.mousePressed(e);
                                popupFrame.dispose();
                                isClicked = false;
                            }
                        });
                    }

                }

            }
        });
        int array[] = {1,2,3,4};
    }
    /**
     * A job list as List of ListItems getter.
     * @return jobs     list of jobs.
     * */
    public List<ListItem> getJobs(){
        return jobs;
    }
    /**
     * A job list as JList getter.
     * @return jListJobs    JList of jobs.
     * */
    public JList<Object> getJobList(){
        return this.jListJobs;
    }
    /**
     * Method adds to list a job.
     * @param label     means name of job (200 chars max).
     * @param hour      means hour of job to notify.
     * */
    public void addToList(String label, String hour, boolean cykliczne, int priorytet){
        jobs.add(new ListItem(label, hour, cykliczne, priorytet));
        Comparator<ListItem> itemComparator = (ListItem i1, ListItem i2) -> String.valueOf(i2.getPriorytet()).compareTo(String.valueOf(i1.getPriorytet()));
        jobs.sort(itemComparator);
        jListJobs.setListData(jobs.toArray());
        jListJobs.revalidate();
        jListJobs.repaint();
    }
    public void checkCyklicznosc(){
        for(int i = 0;i<jobs.size();i++)
        {
            if(jobs.get(i).isCykliczne() == false)
            {
                jobs.remove(i);
            }
        }
        jListJobs.setListData(jobs.toArray());
        jListJobs.revalidate();
        jListJobs.repaint();
    }
    /**
     * Method pops out delete ListItem window (a job).
     * */
    private void popupWindowDelete(){

        popupFrame = new JFrame("Delete");
        try {
            BufferedImage img = ImageIO.read(getClass().getResource("/deleteAsk.png"));
            popupFrame.setContentPane(new ImagePrinter(img));
        } catch (IOException e){
            e.printStackTrace();
        }
        JLabel wantToDelete = new JLabel("You really meant to delete?");
        wantToDelete.setForeground(Color.white);
        yesButton = new JButton("Yes");
        noButton = new JButton("No");
        popupFrame.setLayout(new FlowLayout());
        popupFrame.add(wantToDelete);
        popupFrame.add(yesButton);
        popupFrame.add(noButton);
        popupFrame.setBounds(jobToDo.marginProgram);
        popupFrame.setSize(new Dimension(400,75));
        popupFrame.setVisible(true);
        isClicked = true;
        popupFrame.setDefaultCloseOperation(0);
    }
    /**
     *  Method runs edit window.
     * */
    private void popupWindowEdit(){
        try {
            BufferedImage img = ImageIO.read(getClass().getResource("/popupBackground.png"));
            popupFrame.setContentPane(new ImagePrinter(img));
        } catch (IOException e){
            e.printStackTrace();
        }
        Container content = popupFrame.getContentPane();

        SpringLayout sL = new SpringLayout();

        JLabel nameLabel = new JLabel("Type job name below:");
        JLabel hourLabel = new JLabel("Type hour (format eg. 13:23) below:");
        nameLabel.setForeground(Color.white);
        hourLabel.setForeground(Color.white);
        content.add(nameLabel); content.add(name);
        content.add(hourLabel); content.add(hour);
        content.add(editButton);
        content.add(cykliczne);
        content.add(priorytet);

        sL.putConstraint(SpringLayout.WEST, nameLabel,10,SpringLayout.WEST, content);
        sL.putConstraint(SpringLayout.NORTH, nameLabel,10,SpringLayout.WEST, content);
        sL.putConstraint(SpringLayout.WEST, name,10,SpringLayout.WEST, content);
        sL.putConstraint(SpringLayout.NORTH, name,20,SpringLayout.NORTH, nameLabel);
        sL.putConstraint(SpringLayout.NORTH, hourLabel,40,SpringLayout.NORTH, name);
        sL.putConstraint(SpringLayout.NORTH, hour,20,SpringLayout.NORTH, hourLabel);
        sL.putConstraint(SpringLayout.WEST, hour,10,SpringLayout.WEST, content);
        sL.putConstraint(SpringLayout.WEST, hourLabel,10,SpringLayout.WEST, content);
        sL.putConstraint(SpringLayout.WEST, editButton,50,SpringLayout.WEST, hour);
        sL.putConstraint(SpringLayout.NORTH, editButton,18,SpringLayout.NORTH, hourLabel);
        sL.putConstraint(SpringLayout.NORTH, cykliczne,25,SpringLayout.NORTH, editButton);
        sL.putConstraint(SpringLayout.NORTH, priorytet,25,SpringLayout.NORTH, cykliczne);



        content.setLayout(sL);
        popupFrame.setSize(new Dimension(500,200));
        popupFrame.setVisible(true);
    }
    /**
     * Method updates list of jobs.
     * @param list List of ListItems(jobs).
     * */
    public void update(List<ListItem> list){
        jobs.addAll(list);
        jListJobs.setListData(jobs.toArray());
        jListJobs.revalidate();
        jListJobs.repaint();
    }
    public int getSize()
    {
        return jobs.size();
    }
    /**
     * Method imports default data on program start. Saved in .jobs file.
     * */
    public void setDefaultData(){
        ImportData defaultImport = new ImportData(new File(SaveData.pathToFile));
        Thread defaultReadThread = new Thread(defaultImport);
        defaultReadThread.start();
        try {
            defaultReadThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        update(defaultImport.getList());
    }

}
