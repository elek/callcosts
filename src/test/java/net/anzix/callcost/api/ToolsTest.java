/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.api;

import net.anzix.callcost.Tools;
import android.app.Activity;
import android.database.Cursor;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.anzix.callcost.custom.CustomCountry;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author elek
 */
public class ToolsTest {

   
    /**
     * Test of printNumber method, of class Tools.
     */
    @Test
    public void testPrintNumber() {
        System.out.println("printNumber");
        int no = 0;
        CustomCountry c = new CustomCountry("test","test");
        c.getProperties().put("currency", "Ft");
        assertEquals("1 Ft", Tools.printNumber(1, c));
        assertEquals("100 Ft", Tools.printNumber(100, c));
        c.getProperties().put("divider", "2");
        c.getProperties().put("currency", "$");
        assertEquals("0.01 $", Tools.printNumber(1, c));
        assertEquals("1.23 $", Tools.printNumber(123, c));

    }

}
