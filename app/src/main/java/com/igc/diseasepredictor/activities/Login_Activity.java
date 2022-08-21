package com.igc.diseasepredictor.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.igc.diseasepredictor.R;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Login_Activity extends AppCompatActivity {

    TextInputEditText txtEmail,txtPass,txtMobNo;
    ProgressDialog pd;
    Button btnSignIn,btnGetOTP;
    LinearLayout lytEmail,lytMain,lytMobile;
    CountryCodePicker ccPicker;
    String OTP;
    PinView otpView;

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    PhoneAuthProvider.ForceResendingToken myforceResendingToken;
    DatabaseReference dbref;
    GoogleSignInClient gsclient;
    private static final int REQ_USER_CONSENT = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        init();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsclient = GoogleSignIn.getClient(Login_Activity.this,gso);
        findViewById(R.id.btnGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = gsclient.getSignInIntent();
                startActivityForResult(i,111);
            }
        });
        txtMobNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtMobNo.getText().toString().length()>10) {
                    txtMobNo.setError("Please Enter Valid Mobile Number");
                    txtMobNo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void init()
    {
        txtEmail=findViewById(R.id.txtEmail);
        btnGetOTP=findViewById(R.id.btnGetOTP);
        txtPass=findViewById(R.id.txtPass);
        txtMobNo=findViewById(R.id.txtMobNo);
        btnSignIn=findViewById(R.id.btnSignIn);
        otpView=findViewById(R.id.otpView);
        lytEmail=findViewById(R.id.lytEmail);
        lytMain=findViewById(R.id.lytMain);
        ccPicker=findViewById(R.id.ccPicker);
        lytMobile=findViewById(R.id.lytMobile);
        pd=new ProgressDialog(Login_Activity.this);
        fbAuth = FirebaseAuth.getInstance();
        dbref = FirebaseDatabase.getInstance().getReference();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==111)
        {
            Task<GoogleSignInAccount> taskAcc = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount acc = taskAcc.getResult();
            AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(),null);
            fbAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Uri uri;
                        Toast.makeText(Login_Activity.this, "User Login Successfully...!", Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
                        SharedPreferences.Editor se = sp.edit();
                        se.putString("STATUS", "SUCCESS");
                        fbUser=FirebaseAuth.getInstance().getCurrentUser();
                        se.putString("EMAIL",fbUser.getEmail());
                        se.putString("NAME",fbUser.getDisplayName());
                        se.putString("MOBNO",fbUser.getPhoneNumber());

                        se.putString("IMAGEURL",fbUser.getPhotoUrl().toString());
                        //fbUser=FirebaseAuth.getInstance().getCurrentUser();
                        makeEntry(fbUser);
                        se.apply();
                        startActivity(new Intent(Login_Activity.this,DashBoard_Activity.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Login_Activity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {

            Toast.makeText(Login_Activity.this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks myCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            // OTP = phoneAuthCredential.getSmsCode();
            //Toast.makeText(OTP_Activity.this, "OTP="+OTP, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            myforceResendingToken = forceResendingToken;
            OTP = s;
            //Toast.makeText(OTP_Activity.this, "OTP="+OTP, Toast.LENGTH_SHORT).show();
        }

    };

    public void loginPhone(View view)
    {
        lytMobile.setVisibility(View.VISIBLE);
    }

    public void loginEmail(View view)
    {
        lytEmail.setVisibility(View.VISIBLE);
    }
    public void signUp(View view) {
        if(txtEmail.getText().toString().isEmpty())
        {
            txtEmail.setError("Please Enter The Email Address");
            txtEmail.requestFocus();
        }
        else if(txtPass.getText().toString().isEmpty())
        {
            txtPass.setError("Please Enter The Password");
            txtPass.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString().trim()).matches())
        {
            txtEmail.setError("Please Enter The Valid Email Address");
            txtEmail.requestFocus();
        }
        else
        {
            pd.setTitle("Signing Up!");
            pd.show();
            String email=txtEmail.getText().toString().trim();
            String pass=txtPass.getText().toString().trim();
            fbAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        pd.dismiss();
                        Toast.makeText(Login_Activity.this, "User Registered Successfully...!", Toast.LENGTH_SHORT).show();
                        fbUser = FirebaseAuth.getInstance().getCurrentUser();
                        fbUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(Login_Activity.this, "Verification Email sent.", Toast.LENGTH_SHORT).show();
                                    android.app.AlertDialog.Builder ab=new AlertDialog.Builder(Login_Activity.this);
                                    ab.setTitle("Verification Link Sent!");
                                    ab.setMessage("Please Verify Your Email In Inbox");
                                    ab.setPositiveButton("Ok!",null);
                                }
                                else
                                {
                                    pd.dismiss();
                                    Toast.makeText(Login_Activity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        pd.dismiss();
                        if(task.getException() instanceof FirebaseAuthUserCollisionException)
                        {
                            Toast.makeText(Login_Activity.this, "User Already Registerd!.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            pd.dismiss();
        }
    }
    public void signIn(View view) {
        if (txtEmail.getText().toString().isEmpty()) {
            txtEmail.setError("Please Enter The Email Address");
            txtEmail.requestFocus();
        } else if (txtPass.getText().toString().isEmpty()) {
            txtPass.setError("Please Enter The Password");
            txtPass.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString().trim()).matches()) {
            txtEmail.setError("Please Enter The Valid Email Address");
            txtEmail.requestFocus();
        } else {
            pd.setTitle("Signing In!");
            pd.show();
            final String email = txtEmail.getText().toString().trim();
            String pass = txtPass.getText().toString().trim();
            //startActivity(new Intent(Login_Activity.this, DashBoard_Activity.class));
            //finish();
            fbAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        fbUser = FirebaseAuth.getInstance().getCurrentUser();
                        Toast.makeText(Login_Activity.this, "UID : "+fbUser.getUid(), Toast.LENGTH_SHORT).show();
                        if (fbUser.isEmailVerified()) {
                            pd.dismiss();
                            SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
                            SharedPreferences.Editor se = sp.edit();
                            se.putString("STATUS", "SUCCESS");
                            se.putString("EMAIL", email);
                            se.commit();
                            fbUser=FirebaseAuth.getInstance().getCurrentUser();
                            makeEntry(fbUser);
                            Toast.makeText(Login_Activity.this, "User Login Successfully...!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login_Activity.this, DashBoard_Activity.class));
                            finish();
                        } else {
                            pd.dismiss();
                            Toast.makeText(Login_Activity.this, "Please Verify Email ID.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        pd.dismiss();
                        Toast.makeText(Login_Activity.this, "Email ID Not Registered Yet!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            pd.dismiss();
        }
    }
    public void emailBack(View view) {
        lytEmail.setVisibility(View.GONE);
        lytMain.setVisibility(View.VISIBLE);
    }

    public void resetPassword(View view) {
        if(txtEmail.getText().toString().isEmpty())
        {
            txtEmail.setError("Please Enter Email Address");
            txtEmail.requestFocus();
        }
        else
        {
            fbAuth.sendPasswordResetEmail(txtEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Login_Activity.this, "Reset Password Link Sent!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Login_Activity.this, "Email Not Registered To Server", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void mobileBack(View view) {
        lytMobile.setVisibility(View.GONE);
        lytMain.setVisibility(View.VISIBLE);
    }

    public void getOTP(View view) {
        if(txtMobNo.getText().toString().isEmpty())
        {

            txtMobNo.setError("Please Enter Mobile Number");
            txtMobNo.requestFocus();
        }
        else
        {
            PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder(fbAuth)
                    .setPhoneNumber(ccPicker.getFullNumberWithPlus()+txtMobNo.getText().toString().trim())
                    .setActivity(this)
                    .setCallbacks(myCallback)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .build());
            findViewById(R.id.lytOTP).setVisibility(View.VISIBLE);
            btnGetOTP.setText("Resend OTP");
                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            sec--;
                            if(sec==0)
                            {
                                btnGetOTP.setEnabled(true);
                            }
                            btnGetOTP.setText("Resend "+sec );
                        }
                    },1000);*/

        }
    }
    public void submitOTP(View view) {
        if(txtMobNo.getText().toString().isEmpty())
        {
            txtMobNo.setError("Please Enter Mobile Number");
            txtMobNo.requestFocus();
        }
        else if(otpView.getText().toString().isEmpty())
        {
            otpView.setError("Please Enter OTP");
            otpView.requestFocus();
        }
        else
        {
            pd.setMessage("Please Wait! Verifying OTP");
            pd.show();

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP, otpView.getText().toString());
            fbAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        pd.dismiss();
                        fbUser=FirebaseAuth.getInstance().getCurrentUser();
                        makeEntry(fbUser);
                        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
                        SharedPreferences.Editor se=sp.edit();
                        se.putString("STATUS","SUCCESS");
                        se.putString("MOBNO",txtMobNo.getText().toString().trim());
                        se.commit();
                        Toast.makeText(Login_Activity.this, "User Login Successfully...!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login_Activity.this,DashBoard_Activity.class));
                        finish();
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(Login_Activity.this, "OTP Not Match", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            pd.dismiss();
        }

    }
    public void makeEntry(final FirebaseUser fbUser)
    {
        dbref = FirebaseDatabase.getInstance().getReference("UserInfo").child(""+fbUser.getUid());
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    dbref = FirebaseDatabase.getInstance().getReference().child("UserInfo");
                    Map<String, String> M = new HashMap<>();
                    M.put("UREGISTER","DUE");
                    dbref.child(""+fbUser.getUid()).setValue(M);
                }
                else
                {
                    Toast.makeText(Login_Activity.this, "Dear User! You Already Registered!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}