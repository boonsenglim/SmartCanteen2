package com.example.a45vd.smartcanteen;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.android.volley.toolbox.Volley;

import com.example.a45vd.smartcanteen.database.Redemption;
import com.example.a45vd.smartcanteen.database.RedemptionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static com.example.a45vd.smartcanteen.database.History.BuyerID;


public class FragmentCoupon extends Fragment {

    public static final String TAG = "com.example.user.myApp";
    private static String GET_URL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/select_reward.php";
    public static boolean allowRefresh;

    Button  btnOK;
    TextView tvRewardBalance;
    ListView listViewReward;
    List<Redemption> RList;
    RequestQueue queue;
    TextView tvPromptDiscCode;
    String RedemptionID;

    public static FragmentCoupon newInstance() {
        FragmentCoupon fragment = new FragmentCoupon();
        return fragment;
    }

    public void onSelectClick(View v) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        //Fragment.allowRefresh = true;
/*                        MainActivity.tlList = null;
                        MainActivity.lList = null;
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
        //tvRewardBalance = (TextView) rootView.findViewById(R.id.tvBalance);
        //tvRewardBalance.setText("" + MainActivity.LoyaltyPoint);
        tvPromptDiscCode = (TextView) rootView.findViewById(R.id.tvPromptDiscCode);
        allowRefresh = false;
        listViewReward = (ListView) rootView.findViewById(R.id.lvCouponList);
        RList = new ArrayList<>();
        downloadListing(getActivity().getApplicationContext(), GET_URL);

        listViewReward.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Redemption entry = (Redemption) parent.getItemAtPosition(position);
                checkBalanceThenInsert(getActivity(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/select_user.php", entry);

            }
        });
        return rootView;

/*        if (MainActivity.username.equals(????)) {
            tvPromptDiscCode.setVisibility(View.VISIBLE);
        } else {
            tvPromptDiscCode.setVisibility(View.GONE);
        }*/

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
        final RedemptionAdapter adapter = new RedemptionAdapter(getActivity(), R.layout.fragment_item, RList);
        listViewReward.setAdapter(adapter);
        //Toast.makeText(getActivity(), "Count :" + RList.size(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (allowRefresh) {
            allowRefresh = false;
            tvRewardBalance.setText("" + MainActivity.LoyaltyPoint);
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
                                    MainActivity.Balance = jsonObject.getDouble("balance");
                                    MainActivity.LoyaltyPoint = jsonObject.getInt("RewardPoint");
                                    if (MainActivity.LoyaltyPoint > entry.getPointNeeded()) {
                                        allowRefresh = true;
                                        String entryRewardID = String.valueOf(entry.getRewardID());
                                        insertRedeem(getActivity().getApplicationContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insert_redeem.php", entryRewardID, MainActivity.WalletID);
                                        MainActivity.LoyaltyPoint -= entry.getPointNeeded();
                                        tvRewardBalance.setText(MainActivity.LoyaltyPoint + "");
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
                    params.put("username", MainActivity.WalletID);
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
}
