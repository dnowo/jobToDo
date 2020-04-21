import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ImportData implements Runnable {
    private File fileToRead;
    public List<ListItem> list = new ArrayList<>();

    public ImportData(File fileToRead) {
        this.fileToRead = fileToRead;
    }

    @Override
    public void run() {
        readData();
    }
    private void readData(){

        try{
            Scanner reader = new Scanner(fileToRead);
            while(reader.hasNextLine()){
                String data = reader.nextLine();
                String[] splited = data.split("~!~");
                list.add(new ListItem(splited[0], splited[1]));
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        /*ToDo*/
        //for(ListItem i : list) System.out.println(i.getHour() + i.getLabel());

    }

    public List<ListItem> getList() {
        return this.list;
    }
}
