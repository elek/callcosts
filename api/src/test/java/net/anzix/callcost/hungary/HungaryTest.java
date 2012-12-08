/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.hungary;

import junit.framework.Assert;
import net.anzix.callcost.World;
import net.anzix.callcost.rulefile.RuleFileParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * @author elek
 */
public class HungaryTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void withNet() throws Exception {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");

        logger.debug("asd");
        World word = new World();
        InputStream stream = getClass().getResourceAsStream("/hungary.properties");
        Assert.assertNotNull(stream);
        RuleFileParser instance = new RuleFileParser();
        instance.read(word, stream);
        stream.close();

        //todo
//        Provider p = word.getCountry("hu").getProvider(1);
//        Assert.assertNotNull(p);
//
//        Plan plan = p.getPlan("hu.vodafone.m1");
//        Assert.assertNotNull(plan);
//
//        final int TELENOR = UidGenerator.getInstance().getProviderCode("TELENOR");
//        final int VODAFONE = UidGenerator.getInstance().getProviderCode("VODAFONE");
//        CallList list = new CallList();
//        list.addCall(new CallRecord(TELENOR, "06206357891", TestUtil.calendar(2012, 10, 16, 17, 30), 5423));
//        list.addCall(new CallRecord(VODAFONE, "06706357891", TestUtil.calendar(2012, 10, 16, 17, 30), 5423));
//        list.setRequiredNet(500);
//        CalculationResult res = plan.calculateCost(list);
//        System.out.println(res.getOptions());

    }


}
