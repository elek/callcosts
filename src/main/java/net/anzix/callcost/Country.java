/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author elek
 */
public interface Country {

    public List<Provider> getProviders();

    public Plan getPlan(String id);

    public String getName();

    public String getCurrency();

    public DestinationTypeDetector getNumberParser();

    public Collection<Plan> getAllPlans();
}
