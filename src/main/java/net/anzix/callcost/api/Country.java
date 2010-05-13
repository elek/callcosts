/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.api;

import java.util.Collection;
import java.util.List;

/**
 * Class represents a countr.
 * 
 * @author elek
 */
public interface Country {

    public Plan getPlan(String id);

    public Collection<Plan> getAllPlans();

    public String getId();

    public String getName();

    public String getCurrency();

    public void addProvider(Provider p);

    public Provider getProvider(String id);

    public Collection<Provider> getProviders();

    /**
     * Load the country datase.
     *
     * Inactive but available countries won't be loaded.
     */
    public void load();

    public DestinationTypeDetector getNumberParser();

    public int getDivider();
}
