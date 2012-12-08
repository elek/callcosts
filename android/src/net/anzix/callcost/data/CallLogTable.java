package net.anzix.callcost.data;

import android.provider.BaseColumns;

/**
 */
public class CallLogTable implements BaseColumns {
    public static final String TABLE = "callcost";
    public static final String COL_TIME = "time";
    public static final String COL_DURATION = "duration";
    public static final String COL_DESTINATION = "destination";
    public static final String COL_NAME = "name";
    public static final String COL_PROVIDER = "provider";
    public static final String[] ALL_COLL = new String[]{_ID, COL_TIME, COL_DURATION, COL_DESTINATION, COL_NAME, COL_PROVIDER};


}
