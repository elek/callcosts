package net.anzix.callcost.rulefile;

import junit.framework.Assert;
import net.anzix.callcost.Converter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author elekma
 */
public class PojoCreatorTest {
    @Test
    public void testCreate() throws Exception {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("asd", "true");
        parameters.put("qwe", "qwe");

        PojoCreator pc = new PojoCreator();
        Test1 t1 = pc.create(Test1.class, parameters);

        Assert.assertEquals("qwe", t1.qwe);
        Assert.assertEquals(true, t1.asd);

    }

    @Test
    public void testCreate2() throws Exception {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("test4.1.a", "rrrr1");
        parameters.put("test4.1.b", "rrrr2");

        parameters.put("test4.2.a", "asd");
        parameters.put("test4.2.b", "qwe");

        PojoCreator pc = new PojoCreator();
        Test5 t = pc.create(Test5.class, parameters);

        Assert.assertNotNull(t.test4);
        Assert.assertEquals(2, t.test4.size());
        Assert.assertEquals("rrrr1", t.test4.get(0).a);
        Assert.assertEquals("rrrr2", t.test4.get(0).b);

    }

    @Test
    public void viaSetter() throws Exception {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("qwe", "rrrr1");


        PojoCreator pc = new PojoCreator();

        Test6 t = pc.create(Test6.class, parameters);


        Assert.assertEquals("rrrr1", t.a);

    }

    @Test
    public void customCreator() throws Exception {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("qwe", "rrrr1");


        PojoCreator pc = new PojoCreator();
        pc.addConverter(Test1.class.getCanonicalName(), "qwe", new Converter() {
            @Override
            public Object convert(String value) {
                return "xxx";
            }
        });
        Test1 t = pc.create(Test1.class, parameters);


        Assert.assertEquals("xxx", t.qwe);

    }

    public static class Test1 {

        private boolean asd;

        private String qwe;

    }

    public static class Test2 {

        private List<Test4> collection;

    }

    public static class Test4 {

        String a;

        String b;
    }

    public static class Test3 {

        private boolean asd;

        private String qwe;

        private Test2 test2;

    }

    public static class Test5 {

        private List<Test4> test4 = new ArrayList<Test4>();

        public void addTest4(Test4 test4) {
            this.test4.add(test4);

        }

    }

    public static class Test6 {

        String a;

        public void setQwe(String v) {
            this.a = v;
        }
    }
}
