package com.example.a45vd.smartcanteen;

        import android.support.v4.app.Fragment;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.example.a45vd.smartcanteen.database.History;
        import com.example.a45vd.smartcanteen.database.HistoryAdapter;


        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;


public class FragmentHistory extends Fragment {

    public static final String TAG = "com.example.a45vd.SmartCanteen";
    ListView lvHistory;
    List<History> TransactionList;
    private ProgressDialog pDialog;
    private static String GET_TRANSACTION_URL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/select_transaction.php";
    private static String GET_URL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/select_transfer.php";
    RequestQueue queue;

    public static FragmentHistory newInstance() {
        FragmentHistory fragment = new FragmentHistory();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        lvHistory = (ListView) getActivity().findViewById(R.id.lvHistory);
        pDialog = new ProgressDialog(getActivity());
        TransactionList = new ArrayList<>();

        if (!isConnected()) {
            Toast.makeText(getActivity().getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }

        downloadTransaction(getActivity().getApplicationContext(), GET_TRANSACTION_URL);
        return rootView;

    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    public void downloadTransaction(Context context, String url) {
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
                            try {
                                JSONArray j = new JSONArray(response);
                                try {
                                    TransactionList.clear();
                                    for (int i = 0; i < j.length(); i++) {
                                        JSONObject transferResponse = (JSONObject) j.get(i);
                                        String date = transferResponse.getString("Date");
                                        String buyer = transferResponse.getString("Buyer");
                                        String itemName = transferResponse.getString("ItemName");
                                        String seller = transferResponse.getString("Seller");
                                        String price = transferResponse.getString("Price");
                                        History history = new History(date, buyer, itemName, seller, price);
                                        TransactionList.add(history);
                                    }
                                    loadTransaction();
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
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



    private void loadTransaction() {
        final HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(), R.layout.fragment_history, TransactionList);
        lvHistory.setAdapter(historyAdapter);
        //Toast.makeText(getApplicationContext(), "Count :" + TransactionList.size(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }


}
