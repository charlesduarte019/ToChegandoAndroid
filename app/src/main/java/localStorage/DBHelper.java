package localStorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by charl on 10/08/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static String name = "to_chegando";
    private static int version = 1;

    private static String TABLE_FILHO_REGISTRO = "CREATE TABLE filho_registro(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "filho TEXT," +
            "tempo TIME" +
            ");";


    public DBHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_FILHO_REGISTRO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
