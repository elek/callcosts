/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;


import junit.framework.Assert;
import net.anzix.callcost.Country;
import net.anzix.callcost.Tools;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
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
        Country c = new Country(1);
        c.setDivider(0);
        c.setCurrency("Ft");

        assertEquals("1 Ft", Tools.printNumber(1, c));
        assertEquals("100 Ft", Tools.printNumber(100, c));
        assertEquals("100", Tools.printNumber(100, c, 0, false));

        c.setDivider(2);
        c.setCurrency("$");

        assertEquals("0.01 $", Tools.printNumber(1, c));
        assertEquals("1.23 $", Tools.printNumber(123, c));
        assertEquals("1 $", Tools.printNumber(100, c, 0, true));
        assertEquals("1.2", Tools.printNumber(123, c, 1, false));

    }

    @Test
    public void resolve() {
        Assert.assertEquals("qwe 2 q", Tools.resolve("qwe ${blaBla} q", this));
        Assert.assertEquals("qwe q", Tools.resolve("qwe q", this));

    }

    public int getBlaBla() {
        return 2;
    }

}
