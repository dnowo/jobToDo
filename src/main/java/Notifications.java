import java.util.*;
/**
 * Class reminds of incoming jobs.
 * @author Daniel Nowosielecki
 * @version 1.0.0
 * */
public class Notifications {
    /**
     * Methods notifies when some job is to do.
     * */
    static ListItem notify(List<ListItem> list){
        Calendar now = Calendar.getInstance();
        String timeNow = String.format("%02d:%02d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
        for (ListItem i : list){
            if(i.getHour().equals(timeNow)) return i;
        }
        return null;
    }
}
