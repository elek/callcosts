/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.rulefile;

import net.anzix.callcost.*;
import net.anzix.callcost.option.Net;
import net.anzix.callcost.option.Option;
import net.anzix.callcost.CostCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Parses a definition file and loads country/operator/plan info.
 *
 * @author elek
 */
public class RuleFileParser implements Converter<Option> {

    private static Logger logger = LoggerFactory.getLogger(RuleFileParser.class);

    private Map<String, PropertyContainer> objects = new HashMap();

    private Map<String, Object> properties = new HashMap<String, Object>();

    private String currentType;

    private Map<String, String> parameterStack = null;

    Map<String, Map<String, String>> optionParameters = new HashMap<String, Map<String, String>>();

    private PojoCreator creator = new PojoCreator();

    private UidGenerator idGenerator = UidGenerator.getInstance();

    private Country lastCountry;

    private Provider lastProvider;

    public RuleFileParser() {
        creator.addConverter(Option.class, this);
        MoneyConverter mc = new MoneyConverter();
        creator.addConverter(CallPlan.class.getCanonicalName(), "pricePerMin", mc);
        creator.addConverter(CallPlan.class.getCanonicalName(), "monthlyFee", mc);
        creator.addConverter(CallPlan.class.getCanonicalName(), "includedCost", mc);
        creator.addConverter(CostCondition.class.getCanonicalName(), "pricePerMin", mc);
        creator.addConverter(CostCondition.class.getCanonicalName(), "pricePerEvent", mc);
        creator.addConverter(CostCondition.class.getCanonicalName(), "costIncluded", mc);
        creator.addConverter(CostCondition.class.getCanonicalName(), "pricePerMinAfter", mc);
        creator.addConverter(CallPlan.class.getCanonicalName(), "pricePerSms", mc);
        creator.addConverter(CallPlan.class.getCanonicalName(), "pricePerCall", mc);
        creator.addConverter(PatternTypeDetector.Pattern.class.getCanonicalName(), "name", new Converter() {

            @Override
            public Object convert(String value) {
                UidGenerator.getInstance().createProviderUid(lastCountry.getId(), value);
                return value;
            }
        });

        creator.addConverter(Net.class.getCanonicalName(), "price", mc);


        Map<String, String> param = new HashMap<String, String>();
        param.put("type", "OrOption");
        param.put("two", "{}");
        param.put("one", "{}");
        optionParameters.put("OR", param);
    }

    public void read(World world, InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line = null;
        int i = -1;
        while ((line = reader.readLine()) != null) {
            i++;
            try {
                line = line.trim();
                if (line.length() == 0 || line.charAt(0) == '#') {
                    continue;
                }
                if (line.trim().startsWith("[")) {
                    if (parameterStack != null) {
                        createComponent(world);
                    }
                    if (line.contains(":")) {
                        String[] parts = line.replace('[', ' ').replace(']', ' ').trim().split(":");
                        currentType = parts[0];
                    } else {
                        currentType = line.substring(1, line.length() - 1);
                    }

                    parameterStack = new LinkedHashMap<String, String>();
                } else {
                    String[] parts = line.trim().split("=");
                    parameterStack.put(parts[0].trim(), parts[1].trim());
                }
            } catch (Exception ex) {
                logger.error("Can't parse line " + i + " in file (" + ex.getMessage() + "): " + line, ex);
            }


        }
        createComponent(world);
        reader.close();
    }

    private void createComponent(World world) {
        if (currentType.equals("country")) {
            lastCountry = creator.create(Country.class, parameterStack);
            lastCountry.setId(idGenerator.createCountryUid(lastCountry.getName()));
            world.addCountry(lastCountry);
        } else if (currentType.equals("pattern")) {
            PatternTypeDetector t = creator.create(PatternTypeDetector.class, parameterStack);
            lastCountry.setDtd(t);
        } else if (currentType.equals("provider")) {
            lastProvider = creator.create(Provider.class, parameterStack);
            lastProvider.setId(idGenerator.createProviderUid(lastCountry.getId(), lastProvider.getName()));
            lastCountry.addProvider(lastProvider);
        } else if (currentType.equals("option")) {
            //creating a textual reference id from name
            if (parameterStack.containsKey("name")) {
                optionParameters.put(createOptionId(parameterStack.get("name")), parameterStack);
            }
        } else if (currentType.equals("plan")) {
            CallPlan p = creator.create(CallPlan.class, parameterStack);
            p.setId(idGenerator.createPlanUid(lastCountry.getId(), lastProvider.getId(), p.getName()));
            lastProvider.addPlan(p);
        } else {
            logger.error("Unknown component " + currentType);
        }
    }

    protected String createOptionId(String name) {
        return name.replaceAll(" ", "").replaceAll("[!\\.]", "").toLowerCase();
    }


    private void setParameters(Object c, Map<String, String> parameterStack) {


    }


    private void setCollectionField(Object c, String parameter, Map<String, Map<String, String>> stringMapMap) {

    }


    @Override
    public Option convert(String value) {
        return new OptionExpressionParser().parse(value, optionParameters, creator);
    }
}
