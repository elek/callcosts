package net.anzix.callcost;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.option.Option;
import net.anzix.callcost.data.SMSRecord;
import net.anzix.callcost.option.FreeMinutes;
import net.anzix.callcost.option.IncludedCost;
import net.anzix.callcost.option.Net;
import net.anzix.callcost.option.NoOption;
import net.anzix.callcost.option.PricePerCall;
import net.anzix.callcost.option.PricePerSms;

import java.util.*;

/**
 * @author elek
 */
public class CallPlan implements Plan {


    public static final String PRICE_PER_MIN = "pricePerMin";


    private int id;

    private String name;

    private String updated;

    private Provider provider;

    private List<CostCondition> conds = new ArrayList();

    private int unit = 60;

    private int monthlyFee;

    private boolean prepaid;

    private List<Option> includedOptions = new ArrayList<Option>();

    private Option options = new NoOption();

    private String note;

    public CallPlan() {
    }

    public CallPlan(int key) {
        this.id = key;
    }

    public boolean isPrepaid() {
        return prepaid;
    }

    public String getName() {
        return name;
    }


    @Override
    public CalculationResult recaculateCost(CallList list, CalculationResult res, CallRecord[] calls,
                                            SMSRecord[] smss, boolean net) {
        CalculationResult result = new CalculationResult(res);
        for (CallRecord record : calls) {
            calculateCost(record, result, new HashMap<CostCondition, Integer>());
        }
        return result;
    }


    @Override
    public CalculationResult calculateCost(CallList list) {
        CalculationResult result = new CalculationResult(this, monthlyFee);
        Map<CostCondition, Integer> includedCost = new HashMap<CostCondition, Integer>();
        for (CallRecord record : list.getCalls()) {
            result.setBillableSecs(record, Tools.getDuration(record.getDuration(), unit));
            calculateCost(record, result, includedCost);
        }
        for (SMSRecord record : list.getSMSs()) {
            calculateSMSCost(record, result);
        }
        result.setUnpaidNetUsage(list.getRequiredNet());
        for (Option o : includedOptions) {
            result = o.getCost(list, result);
        }
        result.getOptions().clear();
        return options.getCost(list, result);
    }

    private int calculateSMSCost(SMSRecord record, CalculationResult result) {
        for (CostCondition cond : conds) {
            if (cond.getType() == CostCondition.Type.SMS && cond.match(record.getDate(), record.getProvider())) {
                int c = cond.getPricePerEvent();
                result.setEstimatedCost(record, c);
                return c;
            }
        }
        result.setEstimatedCost(record, 0);
        return 0;

    }

    public int calculateCost(CallRecord record, CalculationResult result, Map<CostCondition, Integer> includedCost) {
        for (CostCondition cond : conds) {
            if (cond.getType() == CostCondition.Type.CALL && cond.match(record.getDate(), record.getProvider())) {
                int c = result.getBillableSecs(record) * cond.getPricePerMin() / 60;
                if (cond.getCostIncluded() != 0) {
                    if (!includedCost.containsKey(cond)) {
                        includedCost.put(cond, cond.getCostIncluded());
                    }
                    if (includedCost.get(cond) <= 0) {
                        c = result.getBillableSecs(record) * cond.getPricePerMinAfter() / 60;
                    } else {
                        int remaining = includedCost.get(cond) - c;
                        if (remaining < 0) {
                            c = (remaining * -1) * cond.getPricePerMinAfter() / cond.getPricePerMin();
                            includedCost.put(cond, 0);
                        } else {
                            c = 0;
                            includedCost.put(cond, remaining);
                        }
                    }
                }
                c += cond.getPricePerEvent();
                result.setEstimatedCost(record, c);
                return c;
            }
        }
        result.setEstimatedCost(record, 0);
        return 0;
    }

    public void reset() {
    }

    public int getId() {
        return id;
    }

    public Provider getProvider() {
        return provider;
    }

    @Override
    public Option getOptions() {
        return options;
    }


    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public void addCond(CostCondition cond) {
        conds.add(cond);
    }

    public void setMonthlyFee(int monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public void setPrepaid(boolean prepaid) {
        this.prepaid = prepaid;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int addSMS(Calendar time, String to) {
        return 0;
    }


    private int intParam(Map<String, String> properties, String string, int def) {
        String val = (String) properties.get(string);
        if (val == null) {
            return def;
        } else if (val.equals("unlimited")) {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt(val);
    }


    public void setId(int id) {
        this.id = id;
    }

    public void addRules(CostCondition condition) {
        conds.add(condition);
    }

    public void setFreeMinutes(int min) {
        includedOptions.add(new FreeMinutes(min));
    }

    public void setFreeUnits(int min) {
        includedOptions.add(new FreeMinutes(min));
    }

    public void setPricePerMin(int min) {
        conds.add(new CostCondition(min));
    }

    public void setNetIncluded(int mb) {
        includedOptions.add(new Net(0, mb));
    }

    public void setOptions(Option options) {
        this.options = options;
    }

    public void setIncludedCost(int includedCost) {
        includedOptions.add(new IncludedCost(includedCost));
    }

    public void setPricePerCall(int price) {
        includedOptions.add(new PricePerCall(price));
    }

    public void setPricePerSms(int price) {
        includedOptions.add(new PricePerSms(price));
    }

    public void setIncludedOptions(Option option) {
        includedOptions.add(option);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
