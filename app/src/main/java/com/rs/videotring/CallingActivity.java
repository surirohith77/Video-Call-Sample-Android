package com.rs.videotring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class CallingActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    TextView nameCOntact;
    ImageView profileImage, cancelCallBtn , acceptCallBtn;
    String senderUserId, receiverUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        //name = sharedPref.getString("name","");
        senderUserId = sharedPref.getString("mobile","");

        Bundle bundle  = getIntent().getExtras();

        if (bundle!=null){

         receiverUserId =   bundle.getString("visit_user_id");
        }

        mediaPlayer = MediaPlayer.create(this,R.raw.ringing);

        nameCOntact = findViewById(R.id.tvUSername);
        profileImage = findViewById(R.id.iv);
        cancelCallBtn = findViewById(R.id.ivCancelCall);
        acceptCallBtn = findViewById(R.id.ivAcceptCall);

        startActivity(new Intent(CallingActivity.this,VideoChatActivity.class));

        getandsetUserProfileINfo();

    }

    private void getandsetUserProfileINfo() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        String url = "http://artlng.com/services.aspx?";


        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    boolean er =       object.getBoolean("error");



                    progressDialog.dismiss();


                    if (!er){

                        nameCOntact.setText(receiverUserId);
                        // onSuccess();
                       // senderid =    object.getString("senderid");

                      /*  Intent intent = new Intent(CallingActivity.this,CallingActivity.class);
                        intent.putExtra("visit_user_id",senderid);
                        startActivity(intent);
                        finish();*/


                    }
                    else {

                        //   Toast.makeText(MainActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                        //   onFailed(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    //    onFailed(2);
                    Toast.makeText(CallingActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(CallingActivity.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "testadd");
                params.put("sender", senderUserId);
                params.put("receiver", receiverUserId);
                //   params.put("mobile", "+91"+mbl);

                return params;
            }

        };

        int socketTimeout = 30000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);


        jsonRequest.setRetryPolicy(policy);

        ApplicationRequest.getInstance(this).addToRequestQueue(jsonRequest);
    }
}
