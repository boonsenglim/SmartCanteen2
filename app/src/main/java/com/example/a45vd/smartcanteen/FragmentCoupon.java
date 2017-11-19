package com.example.a45vd.smartcanteen;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import com.example.a45vd.smartcanteen.database.Redemption;
import com.example.a45vd.smartcanteen.database.RedemptionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class FragmentCoupon extends Fragment implements View.OnClickListener{

    public static final String TAG = "com.example.user.myApp";
    private static String GET_URL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/select_reward.php";
    public static boolean allowRefresh;

    TextView tvRewardBalance;
    Button btn1;
    Button btn2;
    ListView listViewReward;
    List<Redemption> RList;
    RequestQueue queue;

    public static FragmentCoupon newInstance() {
        FragmentCoupon fragment = new FragmentCoupon();
        return fragment;
    }

    public void onSelectClick(View v) {

        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        //Fragment.allowRefresh = true;
/*                        RedeemMainActivity.tlList = null;
                        RedeemMainActivity.lList = null;
                        break;*/

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Successful redeem").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discount_coupon, container, false);
        allowRefresh = false;
        RList = new ArrayList<>();
        downloadListing(getActivity().getApplicationContext(), GET_URL);

        btn1 = (Button) rootView.findViewById(R.id.btn1);
        btn1.setOnClickListener(this);

        btn2 = (Button) rootView.findViewById(R.id.btn2);
        btn2.setOnClickListener(this);

        return rootView;

    }

    public void insertRedeem(Context context, String url, final String id, final String username) {
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
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success == 1) {
                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    onResume();
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("username", username);
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


    public void downloadListing(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            RList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject rewardResponse = (JSONObject) response.get(i);
                                int RewardID = Integer.parseInt(rewardResponse.getString("RewardID"));
                                int PointNeeded = Integer.parseInt(rewardResponse.getString("pointNeeded"));
                                int AmountAvailable = Integer.parseInt(rewardResponse.getString("AmountAvailable"));
                                String rewardTitle = rewardResponse.getString("RewardTitle");
                                Redemption reward = new Redemption(RewardID, PointNeeded, AmountAvailable, rewardTitle);
                                RList.add(reward);
                            }
                            loadListing();

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void loadListing() {
        final RedemptionAdapter adapter = new RedemptionAdapter(getActivity(), R.layout.fragment_discount_coupon, RList);
        listViewReward.setAdapter(adapter);
        //Toast.makeText(getActivity(), "Count :" + RList.size(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (allowRefresh) {
            allowRefresh = false;
            tvRewardBalance.setText("" + RedeemMainActivity.LoyaltyPoint);
            downloadListing(getActivity().getApplicationContext(), GET_URL);
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }

    }

    public void checkBalanceThenInsert(Context context, String url, final Redemption entry) {
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
                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                } else if (success == 1) {
                                    RedeemMainActivity.balance = jsonObject.getDouble("balance");
                                    RedeemMainActivity.LoyaltyPoint = jsonObject.getInt("RewardPoint");
                                    if (RedeemMainActivity.LoyaltyPoint > entry.getPointNeeded()) {
                                        allowRefresh = true;
                                        String entryRewardID = String.valueOf(entry.getProductName());
                                        insertRedeem(getActivity().getApplicationContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insert_redeem.php", entryRewardID, RedeemMainActivity.walletID);
                                        RedeemMainActivity.LoyaltyPoint -= entry.getPointNeeded();
                                        tvRewardBalance.setText(RedeemMainActivity.LoyaltyPoint + "");
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "Insufficient point!", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (success == 2) {
                                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    err += "User not found.";

                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "err", Toast.LENGTH_SHORT).show();

                                }
                                //show error
                                if (err.length() > 0) {
                                    Toast.makeText(getActivity().getApplicationContext(), err, Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", RedeemMainActivity.walletID);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    @Override
    public void onClick(View v) {
        Date timenow = new Date();

        switch (v.getId()) {
            case R.id.btn1:
                if (RedeemMainActivity.LoyaltyPoint >= 500){
                    RedeemMainActivity.LoyaltyPoint -= 500;
                    try {
                        String disCode = "5" + timenow.getTime() + RedeemMainActivity.walletID;
                        String desc = "RM 5 discount";
                        update(getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_point.php");
                        insert(getContext() , "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insert_redemption.php",disCode,desc);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Not enough points", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn2:
                if (RedeemMainActivity.LoyaltyPoint >= 1000){
                    RedeemMainActivity.LoyaltyPoint -= 1000;
                    try {
                        String disCode = "10" + timenow.getTime() + RedeemMainActivity.walletID;
                        String desc = "RM 10 discount";
                        update(getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_point.php");
                        insert(getContext() , "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insert_redemption.php",disCode,desc);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Not enough points", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void update(Context context, String url) {
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

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("LoyaltyPoint", String.valueOf(RedeemMainActivity.LoyaltyPoint));
                    params.put("WalletID", String.valueOf(RedeemMainActivity.walletID));
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

    public void insert(Context context, String url, final String disCode, final String desc) {

        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Description",desc);
                    params.put("CouponCode", disCode);
                    params.put("WalletID", RedeemMainActivity.walletID);
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

}
