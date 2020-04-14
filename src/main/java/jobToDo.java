import javax.swing.*;
import javax.swing.plaf.TableHeaderUI;

public class jobToDo{
    public final static String primaryColor = "#FF0000";
    public final static String secondaryColor = "#0000FF";
    public final static String textColor = "#000000";

    public static void main(String[] args) {

        ImportData importFromFile = new ImportData();
        Thread thread0 = new Thread(importFromFile);
        thread0.start();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }

}
