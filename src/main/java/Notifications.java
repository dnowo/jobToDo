import java.util.*;

public class Notifications {
    static ListItem notify(List<ListItem> list){
        Calendar now = Calendar.getInstance();
        String timeNow = String.format("%02d:%02d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
        for (ListItem i : list){
            if(i.getHour().equals(timeNow)) return i;
        }
        return null;
    }
}
