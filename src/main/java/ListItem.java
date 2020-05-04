import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

class ListRenderer implements ListCellRenderer {

    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer(){
        @Override
        protected void paintComponent(Graphics g) {

            try{
                g.drawImage(ImageIO.read(getClass().getResource("/job.png")), 0, 0, null);
            }catch (IOException e){
                e.printStackTrace();
            }
            setOpaque(false);
            setForeground(Color.white);
            setFont(new Font("Roboto", Font.PLAIN,12));
            super.paintComponent(g);
         }
    };

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, hasFocus);
        renderer.setEnabled(list.isEnabled());
        renderer.setPreferredSize(new Dimension((GUI.WIDTH/2)-50, GUI.HEIGHT/6));
        defaultRenderer.setText("<html><p style=\"width:350px;margin: 5px\">"+ value.toString() +"</p></html>");
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


