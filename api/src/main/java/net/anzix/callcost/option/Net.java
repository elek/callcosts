package net.anzix.callcost.option;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;

/**
 * @author elekma
 */
public class Net implements Option {

    private int price;

    private int data;

    private String name;

    public Net() {

    }

    public Net(String name, int price, int data) {
        this.name = name.replaceAll("_"," ");
        this.data = data;
        this.price = price * 100;
    }

    public Net(int price, int data) {
        this.price = price;
        this.data = data;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    @Override
    public CalculationResult getCost(CallList list, CalculationResult origResult) {
        CalculationResult result = new CalculationResult(origResult);
        if (result.getUnpaidNetUsage() > 0) {
            result.addOption(this,price);
            int unpaid = result.getUnpaidNetUsage() - data;
            if (unpaid < 0) {
                unpaid = 0;
            }
            result.setUnpaidNetUsage(unpaid);
        }
        return result;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Net net = (Net) o;

        if (data != net.data) return false;
        if (price != net.price) return false;
        if (name != null ? !name.equals(net.name) : net.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = price;
        result = 31 * result + data;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
