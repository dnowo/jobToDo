import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

class CheckListRenderer implements ListCellRenderer {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {

        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, hasFocus);
        renderer.setEnabled(list.isEnabled());
        renderer.setFont(list.getFont());
        renderer.setBackground(list.getBackground());
        renderer.setForeground(list.getForeground());
        renderer.setText("<html><p style=\"width:100px\">"+ value.toString() +"</p></html>");
        renderer.setBorder(new LineBorder(Color.BLACK));
        renderer.setPreferredSize(new Dimension(GUI.WIDTH-550, GUI.HEIGHT/8));

        return renderer;
    }
}
class ListItem {

    private String label;
    private StringBuilder job;
    private boolean isSelected = false;
    private String hour;
//test 2
    public ListItem(String label, String hour) {
        this.label = label;
        this.hour = hour;
        this.job = new StringBuilder()
                .append("<html><span style='margin: 5px;font-size:15px;'>")
                .append(label)
                .append("<br>")
                .append(hour)
                .append("</span></html>");
    }

    @Override
    public String toString() {
        return job.toString();
    }
}


