package com.example.a45vd.smartcanteen;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;
        import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //declare variables to be used
    EditText txtWalletID, txtPassword;
    String walletID, checkPass, loginPassword,  currentCard, currentCardType;
    double balance;
    int loyaltyPoint;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtWalletID = (EditText) findViewById(R.id.editTextWalletID);
        txtPassword = (EditText) findViewById(R.id.editTextPassword);

        progressDialog = new ProgressDialog(this);
    }

    public void onClickButtonLogin(View view) {
        String LoginPassword = txtPassword.getText().toString();
        String WalletID = txtWalletID.getText().toString();
        if(WalletID.matches("")){
            Toast.makeText(this, "Please fill in username.", Toast.LENGTH_SHORT).show();
        }
        else if(LoginPassword.matches("")){
            Toast.makeText(this, "Please fill in password.", Toast.LENGTH_SHORT).show();
        }
        //checkUser(this, "https://gabriellb-wp14.000webhostapp.com/select_user.php", WalletID,LoginPassword);
        checkUser(this, "https://martpay.000webhostapp.com/gab_select_user.php", WalletID,LoginPassword);

    }

    public void checkUser(Context context, String url, final String WalletID, final String LoginPassword) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);


        if (!progressDialog.isShowing()){
            progressDialog.setMessage("Logging in...");
            progressDialog.show();
        }



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
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                } else if (success == 1) {
                                    checkPass = jsonObject.getString("LoginPassword");
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    if (checkPass.equals(LoginPassword)) {
                                        walletID = WalletID;
                                        balance = jsonObject.getDouble("Balance");
                                        loyaltyPoint = jsonObject.getInt("LoyaltyPoint");
                                        loginPassword = jsonObject.getString("LoginPassword");
                                        Toast.makeText(getApplicationContext(), "Welcome, "+WalletID+".", Toast.LENGTH_LONG).show();
                                        goToMain();
                                    } else {
                                        err += "Password is incorrect.";
                                    }
                                } else if (success == 2) {
                                    err+="Wallet not found.";
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                } else{
                                    Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_SHORT).show();
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                }
                                //show error
                                if(err.length()>0){
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

    public void goToMain() {
        Intent intent = new Intent(this, RedeemMainActivity.class);
        intent.putExtra("WalletID",walletID);
        intent.putExtra("Balance",balance);
        intent.putExtra("LoyaltyPoint",loyaltyPoint);
        intent.putExtra("loginPassword",loginPassword);
        intent.putExtra("currentCard",currentCard);
        intent.putExtra("currentCardType",currentCardType);
        startActivity(intent);
    }

}
