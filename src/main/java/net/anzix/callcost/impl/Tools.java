/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.impl;

import java.util.Map;
import net.anzix.callcost.api.Country;

/**
 *
 * @author elek
 */
public class Tools {

    public static int getDuration(int sec, int unit) {
        int secs = ((sec / unit) + 1) * unit;
        if (unit == 1) {
            unit--;
        }
        return secs;
    }

    public static int unitBasedPrice(int sec, int unit, int price) {
        if (sec == 0) {
            return 0;
        }
        return Tools.getDuration(sec, unit) * price / 60;
    }

    public static String printNumber(int no, Country c) {
        int divider = c.getDivider();
        String currentcy = c.getCurrency();
        String number = "" + no;

        while (number.length() <= divider) {
            number = "0" + number;
        }
        int sep = number.length() - divider;
        return number.substring(0, sep) + (divider > 0 ? ("." + number.substring(sep)) : "") + " " + currentcy;



    }

    public static int getInt(Map<String, Object> properties, String string, int def) {
        String val = (String) properties.get(string);
        if (val == null) {
            return def;
        } else if (val.equals("unlimited")) {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt(val);
    }
}
