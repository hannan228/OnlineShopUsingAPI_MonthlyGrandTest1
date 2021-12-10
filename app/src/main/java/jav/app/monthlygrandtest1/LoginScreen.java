package jav.app.monthlygrandtest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import jav.app.monthlygrandtest1.Model.Products;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginScreen extends AppCompatActivity {
    private RequestQueue queue;
    KProgressHUD kProgressHUD ;
    EditText userName,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        variableInitialization();
    }

    private void variableInitialization(){
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        queue = Volley.newRequestQueue(this);
        kProgressHUD = KProgressHUD.create(LoginScreen.this, KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Continue...").setCancellable(true);

    }

    public void login(View view) {
        String name =""+ userName.getText();
        String passwordd =""+ password.getText();
        if (passwordd.isEmpty() && name.isEmpty()){
            Toast.makeText(this, "Please Enter Credentials first", Toast.LENGTH_SHORT).show();
        }else {
            if (name.length() > 0) {
                if (passwordd.length() > 0) {
                    if ( isInternetConnected()){
                        kProgressHUD.show();
                        LoadUrl loadUrl = new LoadUrl();
                        loadUrl.execute("https://www.7timer.info/bin/astro.php?lon=113.2&lat=23.1&ac=0&unit=metric&output=json&tzshift=0");
                    }else{
                        Toast.makeText(this, "Your internet is not connected", Toast.LENGTH_SHORT).show();
                    }
//                    readingJson();
                } else {
                    Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void guestMode(View view) {
        goToDashboard("Guest");
    }

    private void goToDashboard(String string){
        Intent intent = new Intent(this,Dashboard.class);
        intent.putExtra("person",""+string);
        startActivity(intent);
        finish();
    }

    private void readingJson() {

        String url = "https://fakestoreapi.com/users";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("shhsh",""+response);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject sonObj = response.getJSONObject(i);

                        if (sonObj.getString("username").equals(""+userName.getText())){
                            if (sonObj.getString("password").equals(""+password.getText())){
                                goToDashboard(""+userName.getText());
                                Toast.makeText(LoginScreen.this, "LogIn Successful", Toast.LENGTH_SHORT).show();
                                kProgressHUD.dismiss();
                                break;
                            }else {
                                Toast.makeText(LoginScreen.this, "Password does not matched", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }else {
                            Toast.makeText(LoginScreen.this, "Username does not match", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        kProgressHUD.dismiss();
                        Toast.makeText(LoginScreen.this, "Something went wrong"+e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", ""+error);
                kProgressHUD.dismiss();
                Toast.makeText(LoginScreen.this, "Error"+error, Toast.LENGTH_LONG).show();
                // Log.d(TAG, "onErrorResponse: ");
            }

        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public boolean isInternetConnected(){
        boolean connected = false;
        ConnectivityManager connectivityManager1 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }

    class LoadUrl extends AsyncTask<String, Integer, String> {

        public LoadUrl() { }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... url) {
            readingJson();

            return "finished";
        }
        @Override
        protected void onPostExecute(String aLong) {
            Log.d("sanam",""+aLong);

            super.onPostExecute(aLong);
        }
    }

}