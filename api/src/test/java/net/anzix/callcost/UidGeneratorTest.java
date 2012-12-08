package net.anzix.callcost;

import junit.framework.Assert;
import net.anzix.callcost.UidGenerator;
import org.junit.Test;

/**
 */
public class UidGeneratorTest {

    @Test
    public void test() {

        UidGenerator pc = UidGenerator.getInstance();
        pc.reset();

        Assert.assertEquals(1, pc.getProviderCode("UNKNOWN"));

        Assert.assertEquals("UNKNOWN", pc.getProviderName(1));

        try {
            pc.getProviderCode("TEST");
            Assert.fail();
        } catch (IllegalArgumentException ex) {
            //hello PMD
            Assert.assertTrue(true);
        }

        pc.createProviderUid(1, "TELCO");

        Assert.assertEquals(266, pc.getProviderCode("TELCO"));
        Assert.assertEquals("TELCO", pc.getProviderName(266));

    }
}
