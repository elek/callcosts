package net.anzix.callcost.tmobile;

import java.util.Calendar;
import net.anzix.callcost.DestinationType;
import net.anzix.callcost.def.CostCondition;
import net.anzix.callcost.def.SimplePlan;

/**
 *
 * @author elek
 */
public class MediaMania extends SimplePlan {

    private int lebeszelheto = 0;

    private String name;

    public MediaMania(String name, int mothlyCost, int costInside, int costOutside) {
        this.name = name;
        setMonthlyCost(mothlyCost);
        addCond(new CostCondition(costInside).to(DestinationType.TMOBILE));
        addCond(new CostCondition(costOutside));

    }

    @Override
    public int call(Calendar time, String to, int durationSec) {
        int cost = super.call(time, to, durationSec);
        if (!to.equals(DestinationType.KEK)) {
            if (lebeszelheto > 0) {
                lebeszelheto -= cost;
                if (lebeszelheto > 0) {
                    return 0;
                } else {
                    return lebeszelheto * -1;
                }
            } else {
                return cost;
            }
        }
        return cost;

    }

    @Override
    public int getCosts() {
        return super.getCosts();
    }

    @Override
    public void reset() {
        super.reset();
        lebeszelheto = getMonthlyCost();
    }

    @Override
    public boolean isPrepaid() {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }
}
