import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
/**
 * Class saves data of jobs in file.
 * @author Daniel Nowosielecki
 * @version 1.0.0
 * */
public class SaveData implements Runnable {
    private List<ListItem> list;
    public static final String pathToFile =  "C:" + File.separator
            + "Users" + File.separator
            + System.getProperty("user.name") + File.separator
            + "Documents" + File.separator
            + "JobToDo" + File.separator
            + "Jobs.jobs";
    private File file;

    SaveData(List<ListItem> list){
        this.list = list;
        file = new File(pathToFile);
        file.getParentFile().mkdirs();
        try{
            if(file.createNewFile()) System.out.println("File created!");
            else if(file.exists()) {
                System.out.println("File exists!");
                file.delete();
                file.createNewFile();
            } else System.out.println("Error while creating a file!");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        writeData();
    }
    /**
     * Method writes jobs data from program to file.
     * */
    private void writeData(){
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try{
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            for(ListItem i : list){
                bufferedWriter.write(i.getLabel()
                        + "~!~"
                        + i.getHour()
                        + "~!~"
                        + i.isCykliczne()
                        + "~!~"
                        + i.getPriorytet()
                        );
                bufferedWriter.newLine();
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Can't write to file.");
        }finally {
            try{
                bufferedWriter.close();
                fileWriter.close();
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Can't close writers.");
            }
        }

    }
}
