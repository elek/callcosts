package net.anzix.callcost.ui;

/**
 * Element to display a separator in the list.
 */
public class SeparatorElement extends MemberElement {

    private String label;

    public SeparatorElement(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
