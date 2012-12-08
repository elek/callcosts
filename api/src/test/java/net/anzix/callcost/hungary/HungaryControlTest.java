package net.anzix.callcost.hungary;

import junit.framework.Assert;
import net.anzix.callcost.UidGenerator;
import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.Country;
import net.anzix.callcost.Plan;
import net.anzix.callcost.World;
import net.anzix.callcost.rulefile.RuleFileParser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author elekma
 */
public class HungaryControlTest {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Map<Integer, Integer> tested = new HashMap<Integer, Integer>();

    @Test
    public void testAll() throws Exception {
        World w = new World();
        File f = new File("../api/src/main/resources/hungary.properties");
        Assert.assertTrue("Hungary definition doesn't exist: " + f.getAbsolutePath(), f.exists());
        InputStream stream = new FileInputStream(f);
        RuleFileParser instance = new RuleFileParser();
        instance.read(w, stream);
        stream.close();
        Country hu = w.getCountry(UidGenerator.getInstance().getContryCode("Hungary"));
        test(hu, "hungary-control.properties");

    }

    private void test(Country c, String s) throws ParseException {

        Scanner scanner = new Scanner(getClass().getResourceAsStream(s)).useDelimiter("\n");
        CallList l = new CallList();
        Plan plan = null;
        int lineNo = 0;
        Map<CallRecord, Integer> expectedCosts = null;
        CalculationResult result = null;

        String filter = null;//"hu.tmobile.mozaiks";
        int filterIndex = 1;

        while (scanner.hasNext()) {
            lineNo++;
            String line = scanner.next().trim();

            if (line.startsWith("plan:")) {
                String planName = line.substring("plan:".length()).trim();
                Plan p = c.findPlan(planName);
                if (p == null) {
                    throw new AssertionError("No such plan " + planName);
                }
                plan = p;
                Assert.assertNotNull("[line:" + lineNo + "]No such plan " + line + " in the country " + c.getName() + " " + c.getAllPlans().size(), plan);
                l = new CallList();
                expectedCosts = new HashMap<CallRecord, Integer>();
            } else if (line.startsWith("net:")) {
                l.setRequiredNet(Integer.parseInt(line.substring("net:".length()).trim()));
            } else if (line.startsWith("call:")) {
                String callStr = line.substring("call:".length()).trim();
                String[] parsedLine = callStr.split(",");
                CallRecord r = new CallRecord(parsedLine[2], calendar(parsedLine[1]), (int) (Double.parseDouble(parsedLine[3]) * 60));
                r.classify(c.getNumberParser());
                l.addCall(r);
                expectedCosts.put(r, convertToPrice(parsedLine[0]));
            } else if (line.startsWith("assert:")) {
                if (!tested.containsKey(plan.getId())) {
                    tested.put(plan.getId(), new Integer(1));
                } else {
                    tested.put(plan.getId(), tested.get(plan.getId()) + 1);
                }

                if (filter != null && (!filter.equals(plan.getId()) || tested.get(plan.getId()) != filterIndex)) {
                    System.out.println("ignoring test for " + plan.getName());
                    continue;
                } else {
                    System.out.println("testing " + plan.getName());
                }

                String assertStr = line.substring("assert:".length()).trim();
                int expectedCost = convertToPrice(assertStr);


                result = plan.calculateCost(l);
                int recNo = 0;
                for (CallRecord rec : l.getCalls()) {
                    Assert.assertEquals("[line:" + lineNo + "]Error in record " + recNo++ + " for plan " + plan.getName(), expectedCosts.get(rec).intValue(), result.getEstimatedCost(rec));
                }

                Assert.assertEquals("[line:" + lineNo + "]Calulated costs are different for " + plan.getName(), expectedCost, result.getCost());

            } else if (line.startsWith("assertPlusNet")) {
                if (filter != null && (!filter.equals(plan.getId()) || tested.get(plan.getId()) != filterIndex)) {
                    System.out.println("ignoring test for " + plan.getId());
                    continue;
                } else {
                    System.out.println("testing " + plan.getId());
                }
                int remainingNet = Integer.parseInt(line.substring("assertPlusNet:".length()).trim());
                Assert.assertEquals(remainingNet, result.getUnpaidNetUsage());
            } else {
                throw new IllegalArgumentException("Unsupported command: " + line);
            }
        }


    }

    private int convertToPrice(String s) {
        return (int) (Double.parseDouble(s) * 100);
    }

    private int calendar(String s) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(df.parse(s));
        return (int) (c.getTimeInMillis() / 1000);
    }
}
