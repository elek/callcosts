package net.anzix.callcost.api;

/**
 *
 * @author elek
 */
public interface NetPlan {

    public boolean isPrepaid();

    public String getName();

    public int getCosts(int gb);
}
