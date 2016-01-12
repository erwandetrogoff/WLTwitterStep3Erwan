package worldline.ssm.rd.ux.wltwitter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by isen on 07/01/2016.
 */
public class WLTwitterDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABSE_NAME ="tweets.db";
    private static final int DATABSE_VERSION = 1;


    public WLTwitterDatabaseHelper(Context context){

        super(context, DATABSE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
     db.execSQL(WLTwitterDatabaseContract.TABLE_TWEETS_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop the current tweet table
    db.execSQL("DROP TABLE IF EXISTS" + WLTwitterDatabaseContract.TABLE_TWEETS);
        //Recreate
    onCreate(db);
    }
}
