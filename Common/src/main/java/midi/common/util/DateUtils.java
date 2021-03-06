package midi.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

    public static String toMinSec(double micros) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) micros / 1000);
        return new SimpleDateFormat("mm:ss").format(calendar.getTime());
    }
}
