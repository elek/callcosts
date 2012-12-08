package net.anzix.callcost.hungary.hu.tmobile;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.option.Option;
import net.anzix.callcost.World;
import net.anzix.callcost.rulefile.MoneyConverter;

/**
 */
public class DominoNet implements Option {

    @Override
    public CalculationResult getCost(CallList list, CalculationResult result) {
        MoneyConverter c = new MoneyConverter();
        int requredNet = result.getUnpaidNetUsage();
        CalculationResult newResult = new CalculationResult(result);
        if (requredNet > 10 * 1024) {
            return newResult;
        } else if (requredNet > 8 * 1024) {
            newResult.addOption(this, World.toInternal(10000));
            newResult.reduceUnpaidNet(10 * 1024);
        } else if (requredNet > 6 * 1024) {
            newResult.addOption(this, World.toInternal(7500));
            newResult.reduceUnpaidNet(8 * 1024);
        } else if (requredNet > 4 * 1024) {
            newResult.addOption(this, World.toInternal(6000));
            newResult.reduceUnpaidNet(6 * 1024);
        } else if (requredNet > 2 * 1024) {
            newResult.addOption(this, World.toInternal(4500));
            newResult.reduceUnpaidNet(4 * 1024);
        } else if (requredNet > 1 * 1024) {
            newResult.addOption(this, World.toInternal(3000));
            newResult.reduceUnpaidNet(2 * 1024);
        } else if (requredNet > 250) {
            newResult.addOption(this, World.toInternal(2000));
            newResult.reduceUnpaidNet(1024);
        } else if (requredNet > 100) {
            newResult.addOption(this, World.toInternal(1000));
            newResult.reduceUnpaidNet(250);
        } else if (requredNet > 0) {
            newResult.addOption(this, World.toInternal(500));
            newResult.setUnpaidNetUsage(100);
        }
        return newResult;
    }

    @Override
    public String getName() {
        return "Domino Net";
    }
}
