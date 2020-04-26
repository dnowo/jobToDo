import javax.swing.*;
import java.awt.*;

public class jobToDo{
    public final static String primaryColor = "#FF0000";
    public final static String secondaryColor = "#0000FF";
    public final static String textColor = "#000000";
    public final static String appName = "Job to do";
    public final static Rectangle marginProgram = new Rectangle(130,130,0,0);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }

}
