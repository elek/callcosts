/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Static helper methods.
 *
 * @author elek
 */
public class Tools {

    private static Pattern p = Pattern.compile("\\$\\{([a-zA-Z0-9]+)\\}");

    public static String printNumber(int no, Country c) {
        return printNumber(no, c, c.getDivider(), true);
    }

    public static String printNumber(int no, Country c, int precision, boolean withCurrency) {
        int divider = c.getDivider();
        String currency = c.getCurrency();
        String number = "" + no;

        while (number.length() <= divider) {
            number = "0" + number;
        }
        int sep = number.length() - divider;
        String res = number.substring(0, sep);
        if (divider > 0 && precision > 0) {
            res += "." + number.substring(sep, sep + precision);
        }
        if (withCurrency) {
            res += " " + currency;
        }

        return res;
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
        return getDuration(sec, unit) * price / 60;
    }

    /**
     * Resolve a string by replacing EL like string with the return value of a getter.
     *
     * @param name
     * @param o
     * @return
     */

    public static String resolve(String name, Object o) {
        Matcher m = p.matcher(name);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            Method getter = null;
            try {
                String methodName = m.group(1);
                getter = o.getClass().getMethod("get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1));
                m.appendReplacement(sb, getter.invoke(o).toString());
            } catch (Exception e) {
                throw new AssertionError(e);
            }

        }
        m.appendTail(sb);
        return sb.toString();
    }
}
