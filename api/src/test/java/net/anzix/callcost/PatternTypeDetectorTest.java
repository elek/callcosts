/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.anzix.callcost;

import net.anzix.callcost.PatternTypeDetector;
import net.anzix.callcost.UidGenerator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * @author elek
 */
public class PatternTypeDetectorTest {

    public PatternTypeDetectorTest() {
    }


    /**
     * Test of detect method, of class PatternTypeDetector.
     */
    @Test
    public void testDetect() {

        int countryId = UidGenerator.getInstance().createCountryUid("Hungary");
        UidGenerator.getInstance().createProviderUid(countryId, "VODA");

        Map test = new HashMap();
        Map rec = new HashMap();
        test.put("1", rec);
        rec.put("pattern", "0670\\d{7}");
        rec.put("name", "VODA");
        Pattern p;
        PatternTypeDetector instance = new PatternTypeDetector();
        instance.addPattern(new PatternTypeDetector.Pattern("0670\\d{7}", "VODA"));
        assertEquals(1, instance.detect("ASD"));
        assertEquals(UidGenerator.getInstance().getProviderCode("VODA"), instance.detect("06701234567"));
    }

}