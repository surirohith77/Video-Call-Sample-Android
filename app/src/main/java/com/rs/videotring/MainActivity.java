package com.rs.videotring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements RvListener {

    private LinearLayout llEmptyState;
    private List<Users> itemList;
    private UserAdapter adapter;
    private ImageView ivEmptyImage;
    private TextView tvEmptyMessage;
    String mobile, senderid;
    String videotest;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

       //name = sharedPref.getString("name","");
        mobile = sharedPref.getString("mobile","");



        initializeView();
        initializeRecyclerView();

     /*   Intent intent = new Intent(this,servicess.class);
        startService(intent);*/

       // keepcalling();
        testhttp();

//        sendhttprequest();
//
//        checkForReceiveingCall();
    }



//        handler = new Handler();
//
//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                  testhttp();
//            }
//        },3000);
    

    public void testhttp(){
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

                        // onSuccess();
                        videotest =    object.getString("call");

                        if (videotest.equals("0")){
                            sendhttprequest();

                        }if (videotest.equals("1")){
                            checkForReceiveingCall();
                        }


                    }
                    else {

                        //   Toast.makeText(MainActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                        //   onFailed(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    //    onFailed(2);
                    Toast.makeText(MainActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "testservice");
                params.put("mobile", mobile);
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

    private void initializeView() {

        llEmptyState = findViewById(R.id.list_ll_empty_state);
        ivEmptyImage = findViewById(R.id.list_iv_empty_image);
        tvEmptyMessage = findViewById(R.id.list_tv_empty_message);
    }

    private void initializeRecyclerView() {

        RecyclerView rvRequest = findViewById(R.id.list_rec_view);
        rvRequest.setHasFixedSize(true);
        rvRequest.setLayoutManager(new LinearLayoutManager(this));

      /*  rvRequest.setLayoutManager(new LinearLayoutManager(this));
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL);
        rvRequest.addItemDecoration(decoration);
*/
        itemList = new ArrayList<>();
        adapter = new UserAdapter( itemList,this);
        rvRequest.setAdapter(adapter);

    }

    @Override
    public void Rvclick(View view, int Position) {

        Users users = itemList.get(Position);

        Toast.makeText(this, ""+users.getMobile()+"\n"+users.getName(), Toast.LENGTH_SHORT).show();

        switch (view.getId()){

            case R.id.callBtn:
                Intent intent = new Intent(MainActivity.this,CallingActivity.class);
                intent.putExtra("visit_user_id",users.getMobile());
                startActivity(intent);
                break;
        }

    }

    private void sendhttprequest() {

        itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait........");
        progressDialog.show();

        ApiDao apiService =
                ApiClient.getClient().create(ApiDao.class);

        Call<List<Users>> call = apiService.getUsers("testlist",mobile);
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(@NonNull Call<List<Users>> call, @NonNull retrofit2.Response<List<Users>> response) {
                List<Users> KyclistActivity = response.body();


                if (KyclistActivity != null) {

                    itemList.addAll(KyclistActivity);
                    progressDialog.dismiss();
                    adapter.notifyDataSetChanged();
                    updateViews(getString(R.string.error_order_request_not_available), R.drawable.ic_invalid_data);
                }
                else {
                    updateViews(getString(R.string.error_invalid_data), R.drawable.ic_invalid_data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Users>>call, @NonNull Throwable t) {
                // Log error here since request failed
                updateViews(getString(R.string.error_connection_request_failed), R.drawable.ic_connection_break);
                progressDialog.dismiss();
            }
        });

    }

    private void updateViews(String message, int drawableID) {

        // Stop refresh animation
        //  swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();

        if (itemList.isEmpty()) {
            tvEmptyMessage.setText(message);
            ivEmptyImage.setImageResource(drawableID);
            llEmptyState.setVisibility(View.VISIBLE);
        } else {
            llEmptyState.setVisibility(View.GONE);
        }
    }


    private void checkForReceiveingCall() {

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

                       // onSuccess();
                        senderid =    object.getString("senderid");

                      /*  Intent intent = new Intent(MainActivity.this,CallingActivity.class);
                      intent.putExtra("visit_user_id",senderid);
                        startActivity(intent);
                        finish();
*/

                      startActivity(new Intent(MainActivity.this,VideoChatActivity.class));

                    }
                    else {

                     //   Toast.makeText(MainActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                        //   onFailed(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    //    onFailed(2);
                    Toast.makeText(MainActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "testresponse");
                params.put("receive", mobile);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }

    public void Logout(MenuItem item) {

        shared_preference   sp = new shared_preference(this);
        sp.WriteLoginStatus(false);
        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }
}
