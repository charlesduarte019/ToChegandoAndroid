package network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by charl on 06/05/2016.
 */
public class ConnectivityInfo{

    private static Context context;

    private static ConnectivityManager connectivityManager;
    private static NetworkInfo networkInfo;

    private ConnectivityInfo(Context context) {
        this.context = context;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isOnline() {
        return isConnected();
    }

    public static boolean isConnected() {
        return is3G() || isWifi();
    }

    public static boolean is3G() {
        validateState();
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return networkInfo.isConnected();
        }
        return false;
    }

    public static boolean isWifi() {
        validateState();
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return networkInfo.isConnected();
        }
        return false;
    }

    private static void validateState() {
        if (context == null) {
            throw new IllegalStateException("call #init(Context) before requesting ConnectivityInfo operations");
        }
    }

}
