/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.anzix.callcost;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 *
 * @author elek
 */
public class MyPreferencesActivity extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
