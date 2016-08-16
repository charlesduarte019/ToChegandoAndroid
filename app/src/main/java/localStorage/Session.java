package localStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by charl on 25/04/2016.
 */
public class Session {

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private final String TAG_Login = "is_logged_in", TAG_Id = "is_idRespons_in";

    public Session(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean setLogin(boolean status) {
        spEditor = sp.edit();
        spEditor.putBoolean(TAG_Login, status);
        spEditor.commit();
        return true;
    }

    public boolean getLoggedIn() {
        return sp.getBoolean(TAG_Login, false);
    }

    public boolean setID(Integer id){
        spEditor = sp.edit();
        spEditor.putInt(TAG_Id, id);
        spEditor.commit();
        return true;
    }

    public Integer getIdResponIn(){
        return sp.getInt(TAG_Id, 0);
    }

}
