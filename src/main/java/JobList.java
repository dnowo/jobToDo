import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class JobList {
    private List<ListItem> jobs = new ArrayList<>();
    private JList<Object> jListJobs;
    private JButton editButton;
    private JButton yesButton;
    private JButton noButton;
    private JTextField name;
    private JTextField hour;
    private JFrame popupFrame;

    JobList(){
        jobs.add(new ListItem("Test work to do for today...", "12:00"));
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
                    popupFrame.setBounds(jobToDo.marginProgram);
                    name.setText(item.getLabel());
                    hour.setText(item.getHour());

                    popupWindowEdit();
                    editButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            super.mouseClicked(e);
                            jobs.set(index,new ListItem(name.getText(), hour.getText()));
                            // Have to revalidate list again after button pressed.
                            list.setListData(jobs.toArray());
                            list.revalidate();
                            list.repaint();
                            popupFrame.dispose();
                        }
                    });
                } else {
                    /* If left button pressed - delete item. */
                    popupWindowDelete();

                    yesButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            super.mousePressed(e);
                            jobs.remove(index);
                            list.setListData(jobs.toArray());
                            list.revalidate();
                            list.repaint();
                            popupFrame.dispose();
                        }
                    });
                    noButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            super.mousePressed(e);
                            popupFrame.dispose();
                        }
                    });
                }

            }
        });

    }
    public List<ListItem> getJobs(){
        return jobs;
    }
    public JList<Object> getJobList(){
        return this.jListJobs;
    }

    public void addToList(String label, String hour){
        jobs.add(new ListItem(label, hour));
        jListJobs.setListData(jobs.toArray());
        jListJobs.revalidate();
        jListJobs.repaint();
    }

    private void popupWindowDelete(){
        popupFrame = new JFrame("Delete");
        yesButton = new JButton("Yes");
        noButton = new JButton("No");
        popupFrame.setLayout(new FlowLayout());
        popupFrame.add(new JLabel("You really meant to delete?"));
        popupFrame.add(yesButton);
        popupFrame.add(noButton);
        popupFrame.setBounds(jobToDo.marginProgram);
        popupFrame.setSize(new Dimension(400,75));
        popupFrame.setVisible(true);
    }
    private void popupWindowEdit(){

        Container content = popupFrame.getContentPane();
        SpringLayout sL = new SpringLayout();

        JLabel nameLabel = new JLabel("Type job name below:");
        JLabel hourLabel = new JLabel("Type hour (format eg. 13:23) below:");

        content.add(nameLabel); content.add(name);
        content.add(hourLabel); content.add(hour);
        content.add(editButton);

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

        content.setLayout(sL);
        popupFrame.setSize(new Dimension(500,200));
        popupFrame.setVisible(true);
    }
    public void update(List<ListItem> list){
        jobs.addAll(list);
        jListJobs.setListData(jobs.toArray());
        jListJobs.revalidate();
        jListJobs.repaint();
    }
}
