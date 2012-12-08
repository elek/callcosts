package net.anzix.callcost.rulefile;

import junit.framework.Assert;
import net.anzix.callcost.option.Option;
import net.anzix.callcost.option.AndOption;
import net.anzix.callcost.option.OrOption;
import net.anzix.callcost.option.TestOption;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author elekma
 */
public class OptionExpressionParserTest {

    @Test
    public void rpn() {
        Map<String, Map<String, String>> parameters = new HashMap<String, Map<String, String>>();
        parameters.put("a", new TreeMap<String, String>());
        parameters.put("b", new TreeMap<String, String>());
        parameters.put("c", new TreeMap<String, String>());
        parameters.put("d", new TreeMap<String, String>());
        OptionExpressionParser oep = new OptionExpressionParser();
        oep.parseToReversePolish(parameters, "a ( 90 )  AND  ( b ( )  OR c ( ) OR d ( )  )");

        Assert.assertEquals(Arrays.asList(new String[]{"90", "a", "b", "c", "OR", "d", "OR", "AND"}), oep.elements);

        oep = new OptionExpressionParser();
        oep.parseToReversePolish(parameters, "a ( 90 )  OR b ( 100 )  OR c ( 200 )");

        Assert.assertEquals(Arrays.asList(new String[]{"90", "a", "100", "b", "OR", "200", "c", "OR"}), oep.elements);


        parameters.put("hu.vodafone.vodaperc", new TreeMap<String, String>());
        parameters.put("hu.vodafone.zsebnet100", new TreeMap<String, String>());
        parameters.put("hu.vodafone.zsebnet500", new TreeMap<String, String>());
        parameters.put("hu.vodafone.zsebnet1g", new TreeMap<String, String>());

        oep = new OptionExpressionParser();
        oep.parseToReversePolish(parameters, "hu.vodafone.vodaperc ( 90 )  AND  ( hu.vodafone.zsebnet100 (  )  OR hu.vodafone.zsebnet500 (  )  OR hu.vodafone.zsebnet1g (  ) )");
        Assert.assertEquals(Arrays.asList(new String[]{"90", "hu.vodafone.vodaperc", "hu.vodafone.zsebnet100", "hu.vodafone.zsebnet500"
                , "OR", "hu.vodafone.zsebnet1g", "OR", "AND"}), oep.elements);


    }

    @Test
    public void testNext() throws Exception {
        OptionExpressionParser parser = new OptionExpressionParser();
        Map<String, Map<String, String>> options = new HashMap<String, Map<String, String>>();

        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "FreeMinutes");
        params.put("freeSecs", "{}");
        options.put("vodaperc", params);

        params = new HashMap<String, String>();
        params.put("type", "FreeMinutes");
        params.put("freeSecs", "{}");
        options.put("vodasms", params);


        params = new HashMap<String, String>();
        params.put("type", "FreeMinutes");
        params.put("freeSecs", "{}");
        options.put("freesms", params);

        params = new HashMap<String, String>();
        params.put("type", "OrOption");
        params.put("one", "{}");
        params.put("two", "{}");
        options.put("OR", params);

        Option option = parser.parse("vodaperc(90) AND vodasms[100] AND freesms ( 20 )", options, new PojoCreator());
        System.out.println(parser.elements);
        Assert.assertEquals(AndOption.class, option.getClass());
        Assert.assertNotNull(((AndOption) option).one);
        Assert.assertNotNull(((AndOption) option).two);

    }

    @Test
    public void testParse() throws Exception {
        OptionExpressionParser parser = new OptionExpressionParser();
        Map<String, Map<String, String>> options = new HashMap<String, Map<String, String>>();

        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "FreeMinutes");
        params.put("freeSecs", "{}");
        options.put("vodaperc", params);

        params = new HashMap<String, String>();
        params.put("type", "FreeMinutes");
        params.put("freeSecs", "{}");
        options.put("vodasms", params);


        params = new HashMap<String, String>();
        params.put("type", "FreeMinutes");
        params.put("freeSecs", "{}");
        options.put("freesms", params);

        params = new HashMap<String, String>();
        params.put("type", "OrOption");
        params.put("one", "{}");
        params.put("two", "{}");
        options.put("OR", params);

        Option option = parser.parse("vodaperc(90) OR vodasms[100] OR freesms ( 20 )", options, new PojoCreator());
        System.out.println(parser.elements);
        Assert.assertEquals(OrOption.class, option.getClass());
        Assert.assertNotNull(((OrOption) option).one);
        Assert.assertNotNull(((OrOption) option).two);

    }


    @Test
    public void byClassName() throws Exception {
        OptionExpressionParser parser = new OptionExpressionParser();
        Map<String, Map<String, String>> options = new HashMap<String, Map<String, String>>();
        PojoCreator creator = new PojoCreator();
        Option option = parser.parse("net.anzix.callcost.option.TestOption(\"xxxx\",123)", options, creator);
        Assert.assertNotNull(option);
        Assert.assertEquals(TestOption.class, option.getClass());
        Assert.assertEquals("testOption xxxx 123", option.getName());

    }

    @Test
    public void andOr() throws Exception {
        OptionExpressionParser parser = new OptionExpressionParser();
        Map<String, Map<String, String>> options = new HashMap<String, Map<String, String>>();


        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "FreeMinutes");
        params.put("freeSecs", "{}");
        options.put("vodaperc", params);

        params = new HashMap<String, String>();
        params.put("type", "FreeMinutes");
        params.put("freeSecs", "{}");
        options.put("vodasms", params);

        params = new HashMap<String, String>();
        params.put("type", "FreeMinutes");
        params.put("freeSecs", "{}");
        options.put("freesms", params);

        params = new HashMap<String, String>();
        params.put("type", "Net");
        params.put("price", "100");
        params.put("data", "100");
        options.put("zsebnet100", params);

        params = new HashMap<String, String>();
        params.put("type", "Net");
        params.put("price", "100");
        params.put("data", "100");
        options.put("zsebnet1g", params);

        params = new HashMap<String, String>();
        params.put("type", "Net");
        params.put("price", "500");
        params.put("data", "100");
        options.put("zsebnet500", params);

        params = new HashMap<String, String>();
        params.put("type", "OrOption");
        params.put("one", "{}");
        params.put("two", "{}");
        options.put("OR", params);

        Option option = parser.parse("vodaperc ( 90 )  AND  ( zsebnet100() OR zsebnet500() OR zsebnet1g() )", options, new PojoCreator());

        Assert.assertEquals(AndOption.class, option.getClass());
        Assert.assertNotNull(((AndOption) option).one);
        Assert.assertNotNull(((AndOption) option).two);

        parser.parse("( vodaperc ( 90 )  )", options, new PojoCreator());

        parser.parse("( vodaperc ( 90 )  OR vodasms ( 100 )  OR freesms ( 20 )  ) ", options, new PojoCreator());


    }
}

