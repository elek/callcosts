/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.custom;

import net.anzix.callcost.custom.CustomLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import net.anzix.callcost.api.Country;
import net.anzix.callcost.api.Plan;
import net.anzix.callcost.api.World;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author elek
 */
public class CustomLoaderTest {

    public CustomLoaderTest() {
    }

    /**
     * Test of read method, of class CustomLoader.
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("read");
        World word = new World();
        InputStream stream = new FileInputStream(new File("res/raw/hungary.def"));
        CustomLoader instance = new CustomLoader();
        instance.read(word, stream);
        stream.close();

        assertNotNull(word.getCountry("hu"));
        Country c = word.getCountry("hu");
        assertEquals("Ft", c.getCurrency());
        assertEquals(3, c.getProviders().size());

//        Plan p = c.getPlan("hu.tmobile.domino7");
//        assertNotNull(p);

//        p = c.getPlan("hu.vodafone.50plus");
//        assertNotNull(p);
    }
}
