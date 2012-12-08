package net.anzix.callcost;

import java.util.Calendar;

/**
 * @author elekma
 */
public class TestUtil {
    public static int calendar(int year, int month, int day, int hour, int minutes) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minutes);
        return (int) (c.getTimeInMillis() / 1000);
    }
}
