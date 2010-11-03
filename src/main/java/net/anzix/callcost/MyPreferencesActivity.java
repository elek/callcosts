/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import net.anzix.callcost.api.World;
import net.anzix.callcost.api.Country;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

/**
 *
 * @author elek
 */
public class MyPreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        setPreferenceScreen(createPreferenceHierarchy());

    }

    private PreferenceScreen createPreferenceHierarchy() {
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);


        int noOfCountries = World.instance().getCountries().size();
        String[] values = new String[noOfCountries];
        String[] entries = new String[noOfCountries];
        int i = 0;
        for (Country c : World.instance().getCountries()) {
            values[i] = c.getId();
            entries[i] = c.getName();
            i++;
        }

        ListPreference country = new ListPreference(this);
        country.setEntries(entries);
        country.setEntryValues(values);
        country.setDialogTitle("Country");
        country.setKey("country");
        country.setTitle("Country");
        country.setSummary("Choose your country");
        root.addPreference(country);


        EditTextPreference netusage = new EditTextPreference(this);
        netusage.setDialogTitle("Net usage");
        netusage.setKey("netusage");
        netusage.setTitle("Net usage");
        netusage.setSummary("Net usage by month in MByte");
        root.addPreference(netusage);

        EditTextPreference daywindow = new EditTextPreference(this);
        daywindow.setDialogTitle("Day windo");
        daywindow.setKey("daywindow");
        daywindow.setTitle("Day window");
        daywindow.setSummary("Calculate with the last X day");

        root.addPreference(daywindow);

        return root;
    }

    public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
        if (arg1.equals("country")) {
            World.instance().changeCountry(arg0.getString(arg1, "hu"));
        }
    }
}
