package com.example.a45vd.smartcanteen;

        import android.support.v4.app.Fragment;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.widget.BottomNavigationView;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonArrayRequest;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
/*      import com.example.user.myApp.database.Listing;
        import com.example.user.myApp.database.ListingAdapter;*/
        import com.example.a45vd.smartcanteen.database.Member;
        import com.google.zxing.integration.android.IntentIntegrator;
        import com.google.zxing.integration.android.IntentResult;


        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static String WalletID = "";
    public static String email;
    public static double Balance;
    public static int LoyaltyPoint;
/*    public static Listing entrySelected;

    public static List<Listing> lList = null;
    public static List<Listing> tlList = null;*/


    TextView tvBalance;

    static final int TOP_UP_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);


        //get User info
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "ERROR: NO EXTRAS FOUND!", Toast.LENGTH_SHORT).show();
            finish();
        }
        WalletID = extras.getString("WalletID");
//        email = extras.getString("email");
        Balance = extras.getDouble("Balance");
       LoyaltyPoint = extras.getInt("LoyaltyPoint");

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, FragmentHome.newInstance());
        transaction.commit();
        final View inflatedView;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_Home:
                        checkBalance(MainActivity.this, "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/select_user.php");
                        fragment = FragmentHome.newInstance();
                        break;
                    case R.id.action_Coupon:
                        fragment = FragmentCoupon.newInstance();
                        break;
                    case R.id.action_giftCard:
                        fragment = FragmentItem.newInstance();
                        break;
                    case R.id.action_history:
                        checkBalance(MainActivity.this, "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/select_user.php");
                        fragment = FragmentHistory.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_main, fragment);
                transaction.commit();
                return true;
            }
        });


    }

    public void checkBalance(Context context, String url) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject;
                            try {
                                String err = "";
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success == 0) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                } else if (success == 1) {
                                    Balance = jsonObject.getDouble("Balance");
                                    LoyaltyPoint = jsonObject.getInt("LoyaltyPoint");
                                    //Toast.makeText(getApplicationContext(), "Balance loaded", Toast.LENGTH_LONG).show();
                                    tvBalance = (TextView) findViewById(R.id.tvBalance);

                                    if (tvBalance != null)
                                        tvBalance.setText(String.format("RM %.2f", MainActivity.Balance));

                                } else if (success == 2) {
                                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    err += "User not found.";

                                } else {
                                    Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_SHORT).show();

                                }
                                //show error
                                if (err.length() > 0) {
                                    Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("WalletID", WalletID);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //Used to select an item programmatically
    //bottomNavigationView.getMenu().getItem(2).setChecked(true);

/*    public void goTopUp(View view) {
        Intent intent = new Intent(this, topUp.class);
        startActivityForResult(intent, TOP_UP_REQUEST);
    }

    public void goExpenses(View view) {
        Intent intent = new Intent(this, TrackExpenses.class);
        startActivity(intent);
    }

    public void goCreateListing(View view) {
        Intent intent = new Intent(this, CreateListing.class);
        startActivity(intent);
    }

    public void goListingDetail(View view) {
        Intent intent = new Intent(this, ListingDetail.class);
        startActivity(intent);
    }

    public void startScan(View view) {
        new IntentIntegrator(this).initiateScan(); // `this` is the current Activity
    }*/









}
