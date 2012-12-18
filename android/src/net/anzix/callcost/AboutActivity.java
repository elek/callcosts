/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.anzix.callcost;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;

/**
 * @author elek
 */
public class AboutActivity extends SherlockActivity {

    DataCache clp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clp = DataCache.getInstance(this);
        setContentView(R.layout.about);
        ((TextView) findViewById(R.id.parsing)).setText("" + (clp.metrics.parsingTime) + " ms");
        ((TextView) findViewById(R.id.calculation)).setText("" + (clp.metrics.calculationTime) + " ms");
        TextView v = ((TextView) findViewById(R.id.clean));
        v.setClickable(true);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clp.cleanDbCache(getApplication());
                Toast.makeText(AboutActivity.this, "Cache is cleared", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
