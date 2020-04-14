import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class JobList {
    private List<ListItem> jobs = new ArrayList<>();
    private JList listOfJobs;

    JobList(){
        this.listOfJobs = new JList(jobs.toArray());

        listOfJobs.setCellRenderer(new CheckListRenderer());
        listOfJobs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listOfJobs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JList list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint()); // Get index of item
                // is clicked
                ListItem item = (ListItem) list.getModel().getElementAt(index);
                System.out.println(index);
                jobs.remove(index);
                list.setListData(jobs.toArray());
                list.revalidate();
                list.repaint();
            }
        });
    }

    public JList getJobList(){
        return this.listOfJobs;
    }

    public void addToList(String label, String hour){
        jobs.add(new ListItem(label, hour));
        listOfJobs.setListData(jobs.toArray());
        listOfJobs.revalidate();
        listOfJobs.repaint();
    }
}
