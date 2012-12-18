/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.*;
import com.actionbarsherlock.app.SherlockPreferenceActivity;

/**
 * @author elek
 */
public class MyPreferencesActivity extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {
    DataCache clp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clp = DataCache.getInstance(this);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        setPreferenceScreen(createPreferenceHierarchy());

    }

    private PreferenceScreen createPreferenceHierarchy() {
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);


        EditTextPreference netusage = new EditTextPreference(this);
        netusage.setDialogTitle(R.string.pref_netusage);
        netusage.setKey("netusage");
        netusage.setTitle(R.string.pref_netusage);
        netusage.setSummary(R.string.pref_netusage_desc);

        netusage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean rtnval = true;
                if (!newValue.toString().matches("\\d+")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MyPreferencesActivity.this);
                    builder.setTitle("Invalid Input");
                    builder.setMessage("Please use a number (MBytes)!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                    rtnval = false;
                }
                return rtnval;
            }
        });

        root.addPreference(netusage);

        return root;
    }

    public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
        if (arg1.equals("netusage")) {
            clp.getCallList().setRequiredNet(Integer.valueOf(arg0.getString(arg1, "0")));
            clp.invalidate();
        }
    }
}
