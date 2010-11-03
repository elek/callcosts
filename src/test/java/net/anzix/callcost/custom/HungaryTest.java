/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.custom;

import net.anzix.callcost.impl.Calculator;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.api.Country;
import net.anzix.callcost.api.Provider;

import net.anzix.callcost.api.World;
import net.anzix.callcost.data.CalculationResult;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author elek
 */
public class HungaryTest {

    @Test
    public void testRead() throws Exception {
        System.out.println("read");
        World word = new World();

        InputStream stream = new FileInputStream(new File("res/raw/hungary.def"));
        CustomLoader instance = new CustomLoader();
        instance.read(word, stream);
        stream.close();
        World.init(word, "hu");
        assertNotNull(word.getCountry("hu"));

        Country c = word.getCurrentCountry();

        assertNotNull(c.getNumberParser());
        PatternTypeDetector ptd = (PatternTypeDetector) c.getNumberParser();
        for (String key : ((CustomCountry) c).getProperties().keySet()) {
            System.out.println("KEY" + key);
        }
        assertNotNull(ptd.getMap());


        assertEquals(4, c.getProviders().size());
        Provider tmobile = c.getProvider("hu.tmobile");
        assertNotNull(tmobile);
        assertTrue(tmobile.getPlans().size() > 0);

        CallList list = new CallList();
        list.addCall(new CallRecord("06701234567", Calendar.getInstance(), 23));
        assertTrue("Not enough plan", c.getAllPlans().size() > 0);
        List<CalculationResult> result = Calculator.calculateplans(c.getAllPlans(), list, 500);
        assertTrue(result.size() > 0);
        for (CalculationResult record : result) {
            System.out.println(record.getPlan().getName());
            System.out.println(record.getAllCosts() + "=" + record.getCallCost() + "+" + record.getNetCost() + "+" + record.getSmsCost());
            if (record.getNetPlan() != null) {
                System.out.println(record.getNetPlan().getName());
            }
        }

        assertEquals(3, tmobile.getNetPlans().size());



        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd HHmm");
        list = new CallList();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(sdf.parse("100509 0653"));

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(sdf.parse("100510 1253"));

        list.addCall(new CallRecord("VODAFONE", cal1, 65));
        list.addCall(new CallRecord("TMOBILE", cal2, 23));
        list.addCall(new CallRecord("VODAFONE", cal2, 65));

        CallPlan p = (CallPlan) c.getPlan("hu.vodafone.vitamaxk");
        p.getCost(list);
        assertEquals(40, list.getCalls().get(0).getCalculatedCost());
        assertEquals(49, list.getCalls().get(1).getCalculatedCost());
        assertEquals(40, list.getCalls().get(2).getCalculatedCost());

        p = (CallPlan) c.getPlan("hu.vodafone.vitamaxparty");
        p.getCost(list);
        assertEquals(58, list.getCalls().get(0).getCalculatedCost());
        assertEquals(49, list.getCalls().get(1).getCalculatedCost());
        assertEquals(98, list.getCalls().get(2).getCalculatedCost());

        p = (CallPlan) c.getPlan("hu.vodafone.100plus");
        assertEquals(3000, p.getCost(list));
        assertEquals(0, list.getCalls().get(0).getCalculatedCost());
        assertEquals(0, list.getCalls().get(1).getCalculatedCost());
        assertEquals(0, list.getCalls().get(2).getCalculatedCost());

    }
}
