package api.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

// Source: https://gist.github.com/kristopherjohnson/6124652
public class TimestampHelper {
    /**
     * Returns an ISO string of the current date and time.
     */
    public static String getCurrentDate() {
        Date d = new Date();
        return getISO8601StringForDate(d);
    }

    public static String getISO8601StringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
    }

    private TimestampHelper() {}
}