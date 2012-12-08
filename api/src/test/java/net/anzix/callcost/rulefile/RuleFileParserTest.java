/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.rulefile;

import junit.framework.Assert;
import net.anzix.callcost.World;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author elek
 */
public class RuleFileParserTest {

    public RuleFileParserTest() {
    }

    /**
     * Test of read method, of class RuleFileParser.
     */
    @Test
    public void testRead() throws Exception {
        World word = new World();
        File resourceFile = new File("../api/src/main/resources/hungary.properties");
        Assert.assertTrue("Resource file is missing: " + resourceFile.getAbsolutePath(), resourceFile.exists());
        InputStream stream = new FileInputStream(resourceFile);
        RuleFileParser instance = new RuleFileParser();
        instance.read(word, stream);
        stream.close();

//        assertNotNull(word.getCountry("hu"));
//        Country c = word.getCountry("hu");
//        assertEquals("Ft", c.getCurrency());
//        assertEquals(5, c.getProviders().size());
//
//        Provider p = c.findProvider("hu.vodafone");
//        Assert.assertNotNull(p);
//
//        Plan plan = p.findPlan("hu.vodafone.m1");
//        Assert.assertNotNull(plan);


//        Plan p = c.getPlan("hu.tmobile.domino7");
//        assertNotNull(p);

//        p = c.getPlan("hu.vodafone.50plus");
//        assertNotNull(p);
    }

    @Test
    public void optionId(){
        RuleFileParser cl = new RuleFileParser();
        Assert.assertEquals("gonetl",cl.createOptionId("Go!Net L"));
    }
}
