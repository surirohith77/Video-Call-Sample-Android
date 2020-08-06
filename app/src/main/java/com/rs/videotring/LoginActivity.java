package com.rs.videotring;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    EditText etName, etMbl;

    Button btnLogin;

    String name, mbl;
    private shared_preference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sharedPreference = new shared_preference(this);

        if (sharedPreference.readloginstatus()) {

            startActivity(new Intent(this,MainActivity.class));
            finish();
        }


        etName = findViewById(R.id.etname);
        etMbl = findViewById(R.id.etmbl);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               name = etName.getText().toString().trim();
                mbl = etMbl.getText().toString().trim();

                sendhttprequest();
            }
        });


    }

    private void sendhttprequest() {

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

                         onSuccess();
                     }
                else {

                         Toast.makeText(LoginActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                     //   onFailed(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                //    onFailed(2);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "testdata");
                params.put("name", name);
                params.put("mobile", "91"+mbl);

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

    private void onSuccess() {

        setPreferences();
        sharedPreference.WriteLoginStatus(true);


        Toast.makeText(LoginActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this,MainActivity.class));

    }

    private void setPreferences(){

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        sharedPref.edit().putString(getString(R.string.mbl), "91"+mbl).apply();
        sharedPref.edit().putString(getString(R.string.name), name).apply();



    }

}
