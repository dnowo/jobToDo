import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputValidator {
    private String toValidateLabel;
    private String toValidateHour;

    public InputValidator(String toValidateLabel, String toValidateHour) {
        this.toValidateLabel = toValidateLabel;
        this.toValidateHour = toValidateHour;
    }
    public boolean isLabelGood(){
        if(toValidateLabel.length() > 200) return false;
        return true;
    }
    public boolean isHourGood(){
        Date hour;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        if(!toValidateHour.matches("[012][0-9]:[0-5][0-9]") || toValidateHour.length() > 5) return false;
        try{
            hour = format.parse(toValidateHour);
        }catch (ParseException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
