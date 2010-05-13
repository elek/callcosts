/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.def;

import java.util.Calendar;

/**
 *
 * @author elek
 */
public class Day {

    private int bits = 0;

    public static Day ALL = new Day("1111111");

    public static Day WEEKDAY = new Day("1111100");

    public static Day WEEKEND = new Day("0000011");

    public Day(int bits) {
        this.bits = bits;
    }

    public Day(String bits) {
        this.bits = Integer.parseInt(bits, 2);
    }

    public boolean match(Calendar cal) {
        return (dayBits(cal.get(Calendar.DAY_OF_WEEK)) & bits) > 0;

    }

    public static Day valueOf(String value) {
        if (value.equals("MONDAY")) {
            return new Day("1000000");
        } else if (value.equals("TUESDAY")) {
            return new Day("0100000");
        } else if (value.equals("WEDNESDAY")) {
            return new Day("0010000");
        } else if (value.equals("THURSDAY")) {
            return new Day("0001000");
        } else if (value.equals("FRIDAY")) {
            return new Day("0000100");
        } else if (value.equals("SATURDAY")) {
            return new Day("0000010");
        } else if (value.equals("SUNDAY")) {
            return new Day("0000001");
        } else if (value.equals("WEEKDAY")) {
            return WEEKDAY;
        } else if (value.equals("WEEKEND")) {
            return WEEKEND;
        } else {
            return ALL;
        }

    }

    public int dayBits(int calDay) {
        switch (calDay) {
            case Calendar.MONDAY:
                return Integer.parseInt("1000000", 2);
            case Calendar.TUESDAY:
                return Integer.parseInt("0100000", 2);
            case Calendar.WEDNESDAY:
                return Integer.parseInt("0010000", 2);
            case Calendar.THURSDAY:
                return Integer.parseInt("0001000", 2);
            case Calendar.FRIDAY:
                return Integer.parseInt("0000100", 2);
            case Calendar.SATURDAY:
                return Integer.parseInt("0000010", 2);
            case Calendar.SUNDAY:
                return Integer.parseInt("0000001", 2);
            default:
                return 0;
        }
    }
}
