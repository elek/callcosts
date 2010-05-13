/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.anzix.callcost.custom;

import net.anzix.callcost.custom.PatternTypeDetector;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
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
        Map test = new HashMap();
        Map rec = new HashMap();
        test.put("1", rec);
        rec.put("pattern", "0670\\d{7}");
        rec.put("name", "VODA");
        Pattern p;
        PatternTypeDetector instance = new PatternTypeDetector(test);
        assertEquals("UNKNOWN", instance.detect("ASD"));
        assertEquals("VODA", instance.detect("06701234567"));
    }

}