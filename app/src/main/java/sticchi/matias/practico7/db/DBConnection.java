package sticchi.matias.practico7.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by MatiasGui on 10/3/2016.
 */
public class DBConnection {
    public static String DBNAMEOUT = "android.db";
    public static final String DB_PATH = "/data/data/com.cursoandroid.practico7/";

    public static String CONNECTION_STRING_OUT = DB_PATH + DBNAMEOUT;

    private static SQLiteDatabase dbVentaOUT;

    public DBConnection()
    {
    }

    public static Boolean checkDatabaseOut()
    {
        SQLiteDatabase checkDB = null;
        try
        {
            checkDB = SQLiteDatabase.openDatabase(CONNECTION_STRING_OUT, null,
                    SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        }
        catch (SQLiteException e)
        {

        }

        if(checkDB != null)
        {

            checkDB.close();

        }
        return checkDB != null ? true : false;
    }
}
