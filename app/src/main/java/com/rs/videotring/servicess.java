/*
package com.rs.videotring;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class servicess extends Service {

    private Timer mTimer;
    private Handler mHandler = new Handler();

    private static final int TIMER_INTERVAL = 3000; // 2 Minute
    private static final int TIMER_DELAY = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        if (mTimer != null)
            mTimer = null;

        // Create new Timer
        mTimer = new Timer();

        // Required to Schedule DisplayToastTimerTask for repeated execution with an interval of `2 min`
        mTimer.scheduleAtFixedRate(new DisplayToastTimerTask(), TIMER_DELAY, TIMER_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Cancel timer
        mTimer.cancel();
    }

    // Required to do some task
    // Here I just display a toast message "Hello world"
    private class DisplayToastTimerTask extends TimerTask {

        @Override
        public void run() {

            // Do something....

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(servicess.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Hello world", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

//    public void testhttp(){
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please wait");
//        progressDialog.show();
//
//        String url = "http://artlng.com/services.aspx?";
//
//        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    JSONObject object = new JSONObject(response);
//                    boolean er =       object.getBoolean("error");
//
//                    progressDialog.dismiss();
//
//                    if (!er){
//
//                        // onSuccess();
//                       String videotest =    object.getString("call");
//
//                        if (videotest.equals("0")){
//                            sendhttprequest();
//
//                        }if (videotest.equals("1")){
//                            checkForReceiveingCall();
//                        }
//
//
//                    }
//                    else {
//
//                        //   Toast.makeText(MainActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
//                        //   onFailed(1);
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    progressDialog.dismiss();
//                    //    onFailed(2);
//                    Toast.makeText(servicess.this, "Problem there in server", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                progressDialog.dismiss();
//                Toast.makeText(servicess.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();
//
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//
//                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
//                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//                //Adding the parameters to the request
//                params.put("request", "testservice");
//                params.put("mobile", sharedPref.getString("mobile",""));
//                //   params.put("mobile", "+91"+mbl);
//
//                return params;
//            }
//
//        };
//
//        int socketTimeout = 30000; //30 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(
//                socketTimeout,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//
//
//        jsonRequest.setRetryPolicy(policy);
//
//        ApplicationRequest.getInstance(this).addToRequestQueue(jsonRequest);
//
//
//    }
}*/
