package localStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by charl on 10/08/2016.
 */
public class DBManager {

    private static DBHelper dbHelper = null;

    public DBManager(Context context){
        if(dbHelper == null){
            dbHelper = new DBHelper(context);
        }
    }

    public void addFilhoRegistro(String nome, String tempo){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("filho", nome);
        contentValues.put("tempo", tempo);

        db.insert("filho_registro", null, contentValues);
    }

    public ArrayList<String> getAllItens(){

        ArrayList<String> registro = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT filho, tempo FROM filho_registro";
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor != null && cursor.moveToFirst()){
            registro = new ArrayList<>();

            do{
                registro.add(cursor.getString(0));
                registro.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }

        return  registro;
    }


}
