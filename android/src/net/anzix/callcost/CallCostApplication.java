package net.anzix.callcost;

import android.app.Application;

/**
 */
public class CallCostApplication extends Application {
    @Override
    public void onTerminate() {
        DataCache.getInstance(this).terminate();
        super.onTerminate();
    }
}
