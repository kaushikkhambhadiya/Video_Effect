package com.capstone.videoeffect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    //Variables
    EditText txtFullName, txtEmail, txtPassword, txtcnfpassword, txtPhone;
    Button btnCreate;
    TextView txtLogIn;
    ImageView ivback;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    private final String TAG = SignUpActivity.class.getSimpleName();
    private Handler mHandler= new Handler();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtFullName = findViewById(R.id.txtFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtcnfpassword = findViewById(R.id.txtcnfpassword);
        txtPhone = findViewById(R.id.txtPhone);
        btnCreate = findViewById(R.id.btnCreate);
        txtLogIn = findViewById(R.id.txtLogIn);
        ivback = findViewById(R.id.ivback);
        progressBar = findViewById(R.id.progressBar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mHandler.post(
                new Runnable() {
                    public void run() {
                        InputMethodManager inputMethodManager =  (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInputFromWindow(txtFullName.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                        txtFullName.requestFocus();
                    }
                });

        firebaseAuth = FirebaseAuth.getInstance();

        //If already Logged In
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), VideoHomeActivity.class));
            finish();
        }

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //OnCreate press
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullname = txtFullName.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String cnfpassword = txtcnfpassword.getText().toString().trim();
                final String phone = txtPhone.getText().toString().trim();

                //Some Authentication
                if (TextUtils.isEmpty(fullname)) {
                    txtFullName.setError("Name Required.");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError("Email Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    txtPassword.setError("Password Required.");
                    return;
                }

                if (password.length() < 5) {
                    txtPassword.setError("Password should be at least 6 characters.");
                }

                if (!password.equals(cnfpassword)) {
                    txtcnfpassword.setError("Confirm Password does not match with Password.");
                }

                if (TextUtils.isEmpty(phone)) {
                    txtPhone.setError("Phone Number Required.");
                    return;
                }

                //Progress
                progressBar.setVisibility(View.VISIBLE);

                //Register New User
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in is successful
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            String fullname = txtFullName.getText().toString().trim();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullname).build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });

                            Toast.makeText(SignUpActivity.this, "Registration Successful!.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        //Registered User
        txtLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}