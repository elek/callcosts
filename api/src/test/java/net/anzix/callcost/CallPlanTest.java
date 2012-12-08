package net.anzix.callcost;

import junit.framework.Assert;
import net.anzix.callcost.CallPlan;
import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.UidGenerator;
import net.anzix.callcost.CostCondition;
import org.junit.BeforeClass;
import org.junit.Test;

import static net.anzix.callcost.TestUtil.calendar;

/**
 * @author elekma
 */
public class CallPlanTest {

    @BeforeClass
    public static void setup() {
        UidGenerator.getInstance().createProviderUid(1, "VODAFONE");
        UidGenerator.getInstance().createProviderUid(1, "TELENOR");
    }

    @Test
    public void rules() {

        CallPlan plan = new CallPlan(1);

        CostCondition c = new CostCondition();
        c.setPricePerMin(120);
        c.setDestination("TELENOR");
        plan.addCond(c);

        c = new CostCondition();
        c.setPricePerMin(60);
        plan.addCond(c);

        plan.addCond(new CostCondition());


        final int TELENOR = UidGenerator.getInstance().getProviderCode("TELENOR");
        final int VODAFONE = UidGenerator.getInstance().getProviderCode("VODAFONE");

        CallList list = new CallList();
        list.addCall(new CallRecord(TELENOR, "06209999999", calendar(2012, 20, 31, 17, 30), 30));
        list.addCall(new CallRecord(VODAFONE, "06709999999", calendar(2012, 20, 31, 17, 32), 100));

        CalculationResult result = plan.calculateCost(list);

        int cost = result.getCost();

        Assert.assertEquals(240, cost);

    }

    @Test
    public void monhtlyFee() {


        CallPlan plan = new CallPlan(1);
        plan.setMonthlyFee(3000);
        plan.setFreeMinutes(30);

        final int TELENOR = UidGenerator.getInstance().getProviderCode("TELENOR");
        final int VODAFONE = UidGenerator.getInstance().getProviderCode("VODAFONE");

        CallList list = new CallList();
        list.addCall(new CallRecord(VODAFONE, "06701234567", calendar(2012, 20, 31, 17, 30), 30));
        list.addCall(new CallRecord(VODAFONE, "06701234567", calendar(2012, 20, 31, 17, 32), 20));

        CalculationResult result = plan.calculateCost(list);

        int cost = result.getCost();

        Assert.assertEquals(3000, cost);

    }

    @Test
    public void freeMinutes() {


        CallPlan plan = new CallPlan(1);
        plan.setMonthlyFee(0);
        plan.setFreeMinutes(30);
        plan.setPricePerMin(5);

        final int TELENOR = UidGenerator.getInstance().getProviderCode("TELENOR");
        final int VODAFONE = UidGenerator.getInstance().getProviderCode("VODAFONE");

        CallList list = new CallList();
        list.addCall(new CallRecord(VODAFONE, "06701234567", calendar(2012, 20, 31, 17, 30), 30));
        list.addCall(new CallRecord(VODAFONE, "06701234567", calendar(2012, 20, 31, 17, 32), 20));

        CalculationResult result = plan.calculateCost(list);

        int cost = result.getCost();

        Assert.assertEquals(0, cost);

    }

    @Test
    public void ppm() {

        CallPlan plan = new CallPlan(1);
        plan.setMonthlyFee(0);
        plan.setFreeMinutes(0);
        plan.setPricePerMin(10);

        CallList list = new CallList();
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 30), 119));
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 32), 342));
        list.addCall(new CallRecord("VODAFONE", calendar(2012, 20, 31, 17, 50), 20));

        CalculationResult result = plan.calculateCost(list);

        int cost = result.getCost();

        Assert.assertEquals(90, cost);

    }

    @Test
    public void ppmAfterFreeMinutes() {

        CallPlan plan = new CallPlan(1);
        plan.setMonthlyFee(0);
        plan.setFreeMinutes(5);
        plan.setPricePerMin(10);

        final int TELENOR = UidGenerator.getInstance().getProviderCode("TELENOR");
        final int VODAFONE = UidGenerator.getInstance().getProviderCode("VODAFONE");

        CallList list = new CallList();
        list.addCall(new CallRecord(VODAFONE, "06701234567", calendar(2012, 20, 31, 17, 30), 119));
        list.addCall(new CallRecord(VODAFONE, "06701234567", calendar(2012, 20, 31, 17, 32), 342));
        list.addCall(new CallRecord(VODAFONE, "06701234567", calendar(2012, 20, 31, 17, 50), 20));

        CalculationResult result = plan.calculateCost(list);

        int cost = result.getCost();

        Assert.assertEquals(40, cost);

    }


}
