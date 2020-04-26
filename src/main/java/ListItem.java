import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

class ListRenderer implements ListCellRenderer {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {

        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, hasFocus);
        renderer.setEnabled(list.isEnabled());
        renderer.setFont(new Font("Roboto", Font.PLAIN,12));
        renderer.setText("<html><p style=\"width:350px;margin: 5px\">"+ value.toString() +"</p></html>");
        //renderer.setBorder(new LineBorder(Color.BLACK,1,true));
        renderer.setPreferredSize(new Dimension((GUI.WIDTH/2)-50, GUI.HEIGHT/6));
        return renderer;
    }
}
class ListItem {

    private String label;
    private StringBuilder job;
    private boolean isSelected = false;
    private String hour;

    public ListItem(String label, String hour) {
        this.label = label;
        this.hour = hour;
        this.job = new StringBuilder()
                .append("<html>")
                .append(label)
                .append("<br><span style=\"font-weight: bold;font-size: 8px;\">")
                .append(hour)
                .append("</span></html>");
    }
    public void setLabel(String label){
        this.label = label;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getHour() {
        return hour;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return job.toString();
    }
}


