import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Class creating data import on new thread.
 * @author Daniel Nowosielecki
 * @version 1.0.0
 * */
public class ImportData implements Runnable {
    private File fileToRead;
    private List<ListItem> list = new ArrayList<>();

    public ImportData(File fileToRead) {
        this.fileToRead = fileToRead;
    }

    @Override
    public void run() {
        readData();
    }
    /**
     * Method reads data from .jobs file.
     * */
    private void readData(){
        try{
            Scanner reader = new Scanner(fileToRead);
            while(reader.hasNextLine()){
                String data = reader.nextLine();
                String[] splited = data.split("~!~");
                list.add(new ListItem(splited[0], splited[1],Boolean.parseBoolean(splited[2]), Integer.parseInt(splited[3])));
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    /**
     *  Getter for list of imported jobs.
     * */
    public List<ListItem> getList() {
        return this.list;
    }
}
