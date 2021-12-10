package jav.app.monthlygrandtest1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.media.Rating;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jav.app.monthlygrandtest1.Model.Products;
import jav.app.monthlygrandtest1.RecyclerAdapter.RecyclerViewAdapter;
import jav.app.monthlygrandtest1.RecyclerAdapter.RecyclerViewAdapterCategory;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dashboard extends AppCompatActivity implements RecyclerViewAdapter.OnProductClickListener,
        RecyclerViewAdapterCategory.OnCategoryClickListener, AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView, recyclerViewHorizontal;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerViewAdapterCategory recyclerViewAdapterCategory;
    private List<String> categoryList;
    private RecyclerViewAdapter.OnProductClickListener onProductClickListener = this::onProductClick;
    private RecyclerViewAdapterCategory.OnCategoryClickListener onProductClickListenerCategory = this::onCategoryClick;
    private RequestQueue queue;
    String[] country = {"All Product", "15", "10", "5","descending"};
    List<Products> productList;
    private boolean productLoadingCompleteFlag = false;
    String selectedCategory = "";
    KProgressHUD kProgressHUD ;
    private EditText searchBar;
    Switch wifiState;
    WifiManager wifiManager ;
    String url = "https://fakestoreapi.com/products";

    boolean categoryFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        variableInitialization();
        spinnerImplementation();

        setActionBar(getIntent().getStringExtra("person"));

    }


    public void setActionBar(String heading) {
        // TODO Auto-generated method stub

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange_500)));
        actionBar.setTitle("Welcome : "+heading);
        actionBar.show();
    }

    private void variableInitialization() {
        categoryList = new ArrayList<>();
        productList = new ArrayList<>();
        searchBar = findViewById(R.id.searchEditText);
        queue = Volley.newRequestQueue(this);
        wifiState = findViewById(R.id.wifi_state1);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        kProgressHUD = KProgressHUD.create(Dashboard.this, KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Continue...").setCancellable(true);
        horizontalRecyclerViewSettings();
        verticalRecyclerViewSettings();

        parseJSON();
        readingJson();
        kProgressHUD.show();

//        if ( isInternetConnected()){
//            kProgressHUD.show();
//
//            LoadUrl loadUrl = new LoadUrl();
//            loadUrl.execute("https://fakestoreapi.com/products");
//        }else{
//            Toast.makeText(this, "Your internet is not connected", Toast.LENGTH_SHORT).show();
//        }
        search();
    }

    private void verticalRecyclerViewSettings() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerViewAdapter = new RecyclerViewAdapter(Dashboard.this, onProductClickListener, productList);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    //vertical recyclerview
    private void readingJson() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("shhsh",""+response);
                if(isInternetConnected()){
                    productList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject sonObj = response.getJSONObject(i);
                            productList.add(new Products(sonObj.getInt("id"),
                                    sonObj.getString("title"),
                                    sonObj.getDouble("price"),sonObj.getString("description"),
                                    sonObj.getString("category"),sonObj.getString("image"),
                                    sonObj.getJSONObject("rating").getDouble("rate"),
                                    sonObj.getJSONObject("rating").getInt("count"))
                            );
                            recyclerViewAdapter.notifyDataSetChanged();
                            productLoadingCompleteFlag = true;

                            kProgressHUD.dismiss();
                            if (selectedCategory.length() > 0) {
                                filter(selectedCategory);
                            }
                        } catch (Exception e) {
                            kProgressHUD.dismiss();
                            Toast.makeText(Dashboard.this, ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(isInternetConnected()){
                    productList.clear();
                    readingJson();
                }else{
                    readingJson();
                }
                Log.d("Error", ""+error);
//                Toast.makeText(Dashboard.this, "Error"+error, Toast.LENGTH_SHORT).show();
//                readingJson();
                // Log.d(TAG, "onErrorResponse: ");
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    //horizontal category recyclerview
    private void parseJSON() {
        String url = "https://fakestoreapi.com/products/categories";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                categoryList.add("all");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        categoryList.add("" + response.getString(i));
                        recyclerViewAdapterCategory.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Dashboard.this, "" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
                , new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "" + error);
                parseJSON();
                // Log.d(TAG, "onErrorResponse: ");
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

    private void horizontalRecyclerViewSettings() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHorizontal = findViewById(R.id.recyclerViewhorizontal);
        recyclerViewHorizontal.setHasFixedSize(true);
        recyclerViewHorizontal.setLayoutManager(layoutManager);

        recyclerViewAdapterCategory = new RecyclerViewAdapterCategory(this, onProductClickListenerCategory, categoryList);
        recyclerViewHorizontal.setAdapter(recyclerViewAdapterCategory);

        recyclerViewAdapterCategory.notifyDataSetChanged();
    }

    public void search() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter2(s.toString());
            }

        });
    }

    private void filter(String text) {
        List<Products> filteredList = new ArrayList<>();

        if (text.equals("all")) {
            filteredList = productList;
        } else {
            for (Products item : productList) {
                if (item.getCategory().toLowerCase().equals(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        recyclerViewAdapter.filterList(filteredList);
    }

    private void filter2(String text) {
        List<Products> filteredList = new ArrayList<>();

        for (Products item : productList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    item.getCategory().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        recyclerViewAdapter.filterList(filteredList);
    }

    public void onProductClick(Products products) {
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra("productName", "" + products.getTitle());
        intent.putExtra("productDescription", "" + products.getDescription());
        intent.putExtra("productPrice", "" + products.getPrice());
        intent.putExtra("productImage", "" + products.getImage());
        intent.putExtra("ratting", "" + products.getRate());
        intent.putExtra("count", "" + products.getCount());
        startActivity(intent);
    }

    //category call back function
    @SuppressLint("ResourceAsColor")
    @Override
    public void onCategoryClick(String category, int position, View view) {
        selectedCategory = category;
        if (productLoadingCompleteFlag) {
            filter(category);
        } else {
            Toast.makeText(this, "please wait for loading", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure! you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dashboard.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout){
            categoryList.clear();
            productList.clear();
            startActivity(new Intent(Dashboard.this,LoginScreen.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void spinnerImplementation() {
        Spinner spin = (Spinner) findViewById(R.id.filter);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (country[position].equals("descending")){
            if ( isInternetConnected()){
                productList.clear();
                kProgressHUD.show();
                url = "https://fakestoreapi.com/products?sort=desc";
                readingJson();
            }else{
                Toast.makeText(this, "Your internet is not connected", Toast.LENGTH_SHORT).show();
                kProgressHUD.setDetailsLabel("waiting internet");
                kProgressHUD.show();
                url = "https://fakestoreapi.com/products?sort=desc";
                readingJson();
            }
        }else {
            if (productLoadingCompleteFlag){
                if ( isInternetConnected()){
                    productList.clear();
                    kProgressHUD.show();
                    url = "https://fakestoreapi.com/products?limit="+country[position];
                    readingJson();
                }else{
                    Toast.makeText(this, "Your internet is not connected", Toast.LENGTH_SHORT).show();
                    kProgressHUD.setDetailsLabel("waiting internet");
                    kProgressHUD.show();
                    url = "https://fakestoreapi.com/products?limit="+country[position];
                    readingJson();
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // WIFI STATE ALL CODE WRITTEM BELOW
//    https://gist.github.com/codinginflow/6e15ba176861b1d3f1cf8bc82b816b49

    public void wifiSate(){
        wifiState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wifiManager.setWifiEnabled(true);
                    wifiState.setText("WiFi is ON");

                } else {
                    wifiManager.setWifiEnabled(false);
                    wifiState.setText("WiFi is OFF");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiStateReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiStateReceiver);
    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiStateExtra) {
                case WifiManager.WIFI_STATE_ENABLED:
                    wifiState.setChecked(true);
                    wifiState.setText("WiFi is ON");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    wifiState.setChecked(false);
                    wifiState.setText("WiFi is OFF");
                    break;
            }
        }
    };

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