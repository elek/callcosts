package net.anzix.callcost.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static net.anzix.callcost.data.CallLogTable.*;

/**
 * User: eszti
 */
public class CallLogHelper extends SQLiteOpenHelper {
    public CallLogHelper(Context context) {
        super(context, "callog.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + "(" +
                _ID + " INTEGER PRIMARY KEY," +
                COL_DURATION + " INTEGER," +
                COL_NAME + " STRING," +
                COL_PROVIDER + " STRING," +
                COL_DESTINATION + " STRING," +
                COL_TIME + " INTEGER" +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
