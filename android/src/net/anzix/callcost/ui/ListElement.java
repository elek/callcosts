package net.anzix.callcost.ui;

import android.widget.AdapterView;

public class ListElement extends MemberElement {


    private String label;
    private String description;
    private AdapterView.OnItemClickListener link;
    private String value;


    public ListElement(String label, String description, String value) {
        this.label = label;
        this.description = description;
        this.value = value;
    }

    public ListElement(String label, String description) {
        this.label = label;
        this.description = description;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AdapterView.OnItemClickListener getLink() {
        return link;
    }

    public void setLink(AdapterView.OnItemClickListener link) {
        this.link = link;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
