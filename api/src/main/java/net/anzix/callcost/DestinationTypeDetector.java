package net.anzix.callcost;

/**
 * Detect the operator for a specific phone number.
 * <p/>
 * Based on best effort.
 *
 * @author elek
 */
public interface DestinationTypeDetector {

    public int detect(String number);
}
