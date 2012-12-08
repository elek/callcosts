package net.anzix.callcost.option;


import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.CallPlan;
import org.junit.Test;
import org.junit.Assert;

import static net.anzix.callcost.TestUtil.calendar;

/**
 * @author elekma
 */
public class IncludedCostTest {

    @Test
    public void testGetCost() throws Exception {


        CallPlan plan = new CallPlan(1);
        plan.setPricePerMin(5);

        CallList list = new CallList();
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 30), 301));
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 32), 65));
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 49), 20));

        CalculationResult result = plan.calculateCost(list);

        Assert.assertEquals(45, result.getCost());

        IncludedCost c = new IncludedCost(23);
        result = c.getCost(list, result);

        Assert.assertEquals(22, result.getCost());


    }
}
