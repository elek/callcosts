package net.anzix.callcost.option;

import junit.framework.Assert;
import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.CallPlan;
import net.anzix.callcost.option.FreeMinutes;
import org.junit.Test;

import static net.anzix.callcost.TestUtil.calendar;

/**
 * @author elekma
 */
public class FreeMinutesTest {
    @Test
    public void testGetCost() throws Exception {

        CallPlan plan = new CallPlan(1);
        plan.setPricePerMin(5);

        CallList list = new CallList();
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 30), 30));
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 32), 20));

        CalculationResult result = plan.calculateCost(list);


        Assert.assertEquals(10, result.getCost());

        FreeMinutes f = new FreeMinutes(2);
        result = f.getCost(list, result);

        Assert.assertEquals(0, result.getCost());

    }

    @Test
    public void overrun() throws Exception {


        CallPlan plan = new CallPlan(1);
        plan.setPricePerMin(5);
        CallList list = new CallList();
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 30), 119));
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 32), 342));
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 50), 20));

        CalculationResult result = plan.calculateCost(list);

        Assert.assertEquals(45, result.getCost());

        FreeMinutes f = new FreeMinutes(2);
        result = f.getCost(list, result);

        Assert.assertEquals(35, result.getCost());

    }

    @Test
    public void overrun2() throws Exception {



        CallPlan plan = new CallPlan(1);
        plan.setPricePerMin(5);

        CallList list = new CallList();
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 30), 119));
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 32), 342));
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 50), 20));

        CalculationResult result = plan.calculateCost(list);

        Assert.assertEquals(45, result.getCost());

        FreeMinutes f = new FreeMinutes(3);
        result = f.getCost(list, result);

        Assert.assertEquals(30, result.getCost());

    }




}
