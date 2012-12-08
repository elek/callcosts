package net.anzix.callcost;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import net.anzix.callcost.Day;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author elek
 */
public class DayTest {

    SimpleDateFormat f = new SimpleDateFormat("yyMMdd HHmm");

    @Test
    public void testMatch() throws ParseException {
        Date d = f.parse("100424 1200");
        System.out.println(d);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        Day day = new Day(1);

        assertEquals(2, day.dayBits(c.get(Calendar.DAY_OF_WEEK)));

        assertTrue(Day.WEEKEND.match(c));
        assertFalse(new Day("0010000").match(c));
        assertTrue(new Day("0000010").match(c));

    }
}
