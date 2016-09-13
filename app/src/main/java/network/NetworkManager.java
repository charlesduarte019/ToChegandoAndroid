package network;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class NetworkManager {

    private NetworkObserved networkObserved;

//    private static final String IP = "http://192.168.74.142:80/ComuJson/"; // Cabo

    private static final String IP = "http://192.168.74.71:80/ComuJson/"; // Rede c019
//    private static final String IP = "http://192.168.0.119:80/ComuJson/"; // Rede Home
//    private static final String IP = "http://192.168.74.61:80/ComuJson/"; // Rede J.Lucas

    public void setNetworkObserved(NetworkObserved networkObserved) {
        this.networkObserved = networkObserved;
    }

    public void post(final Map<String, String> entity, String URL) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, (IP + URL),
                getResponseListener(),
                getErrorResponse()
        ) {
            @Override
            protected Map<String, String> getParams() {
                return entity;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);

        ConnectionController.getInstance().addToRequestQueue(postRequest);

    }

    public Response.Listener<String> getResponseListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                Log.d("Response", response);
                networkObserved.doOnResponse(response);
            }
        };
    }

    public Response.ErrorListener getErrorResponse() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
    }

}

