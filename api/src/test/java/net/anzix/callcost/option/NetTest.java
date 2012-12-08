package net.anzix.callcost.option;

import junit.framework.Assert;
import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.CallPlan;
import org.junit.Test;

import static net.anzix.callcost.TestUtil.calendar;

/**
 * @author elekma
 */
public class NetTest {
    @Test
    public void testGetCost() throws Exception {

        CallPlan plan = new CallPlan(1);
        plan.setPricePerMin(5);

        CallList list = new CallList();
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 30), 301));
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 32), 65));
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 49), 20));
        list.setRequiredNet(300);

        CalculationResult result = plan.calculateCost(list);

        Assert.assertEquals(45, result.getCost());

        Net p = new Net(100, 200);
        result = p.getCost(list, result);

        Assert.assertEquals(145, result.getCost());


    }
}
