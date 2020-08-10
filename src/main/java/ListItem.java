import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 * Class defines base job structure. Implements ListCellRenderer.
 * @author Daniel Nowosielecki
 * @version 1.0.0
 * */
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
    /**
     * Method configuring one list item parameters and look.
     * @return renderer     JLabel of job.
     * */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, hasFocus);
        renderer.setEnabled(list.isEnabled());
        renderer.setPreferredSize(new Dimension((GUI.WIDTH/2)-50, GUI.HEIGHT/6));
        defaultRenderer.setText("<html><p style=\"width:350px;margin: 5px\">"+ value.toString() +"</p></html>");
        return renderer;
    }
}
/**
 * Class creates a box for the job. It's shape.
 * */
class ListItem {

    private String label;
    private StringBuilder job;
    private boolean isSelected = false;
    private String hour;
    private boolean cykliczne;
    private int priorytet;

    public ListItem(String label, String hour, boolean cykliczne, int priorytet) {
        this.label = label;
        this.hour = hour;
        this.cykliczne = cykliczne;
        this.priorytet = priorytet;
        this.job = new StringBuilder()
                .append("<html>")
                .append(label)
                .append("<br><span style=\"font-weight: bold;font-size: 8px;\">")
                .append(hour)
                .append(" Cykliczne: ")
                .append(cykliczne)
                .append("<br> Priorytet: ")
                .append(priorytet)
                .append("</span></html>");
    }
    /**
     * Method sets job.
     * */
    public void setLabel(String label){
        this.label = label;
    }
    /**
     * Method sets hour.
     * */
    public void setHour(String hour) {
        this.hour = hour;
    }
    /**
     * Method gets hour.
     * */
    public String getHour() {
        return hour;
    }
    /**
     * Method gets job.
     * */
    public String getLabel() {
        return label;
    }

    public boolean isCykliczne() {
        return cykliczne;
    }

    public int getPriorytet() {
        return priorytet;
    }

    @Override
    public String toString() {
        return job.toString();
    }
}


