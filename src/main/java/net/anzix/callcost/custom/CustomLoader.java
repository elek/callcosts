/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.custom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import net.anzix.callcost.api.Country;
import net.anzix.callcost.api.Plan;
import net.anzix.callcost.api.Provider;
import net.anzix.callcost.api.World;

/**
 *
 * @author elek
 */
public class CustomLoader {

    private Map<String, PropertyContainer> objects = new HashMap();

    private Map<String, Object> properties = new HashMap<String, Object>();

    public void read(World world, InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            String[] parts = line.split("=");
            String key = parts[0];
            String value = parts[1];
            if (key.contains("[")) {
                key = key.replaceAll("\\]", "");

                String propPath[] = key.split("\\[");

                PropertyContainer o = objects.get(propPath[0]);
                if (o == null) {
                    throw new IllegalArgumentException("Undefined parent object " + propPath[0]);
                }
                Map map = o.getProperties();
                for (int i = 1; i < propPath.length - 1; i++) {
                    System.out.println("GETTING " + key + "/" + propPath[i]);
                    Map tm = (Map) map.get(propPath[i]);
                    if (tm == null) {
                        tm = new TreeMap();
                        map.put(propPath[i], tm);
                    }
                    map = tm;
                }
                map.put(propPath[propPath.length - 1], value);


            } else {
                int type = key.split("\\.").length;
                if (type == 1) {
                    CustomCountry country = new CustomCountry(key, value);
                    world.addCountry(country);
                    objects.put(key, country);
                } else if (type == 2) {
                    String parent = key.substring(0, key.lastIndexOf('.'));
                    CustomProvider p = new CustomProvider(key, value);
                    Country c = (Country) objects.get(parent);
                    c.addProvider(p);
                    objects.put(key, p);
                } else if (type == 3) {
                    String parent = key.substring(0, key.lastIndexOf('.'));
                    Provider provider = (Provider) objects.get(parent);
                    String planType = "Call";
                    String[] idAndType = key.split("<");
                    if (idAndType.length > 1) {
                        planType = idAndType[1].substring(0, idAndType[1].length() - 1);
                        key = idAndType[0];
                    }
                    if (planType.equalsIgnoreCase("call")) {
                        CallPlan p = new CallPlan(key, value);
                        provider.addPlan(p);
                        objects.put(key, p);
                    } else if (planType.equalsIgnoreCase("net")) {
                        Net p = new Net(key, value);
                        provider.getNetPlans().add(p);
                        objects.put(key, p);
                    } else {
                        throw new IllegalArgumentException("Unknown type " + planType + " in line " + line);
                    }

                }
            }
        }

        reader.close();
    }
}
