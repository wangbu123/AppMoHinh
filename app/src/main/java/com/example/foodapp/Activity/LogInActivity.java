package com.example.foodapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.MainActivity;
import com.example.foodapp.Model.User;
import com.example.foodapp.R;
import com.example.foodapp.Support.CheckEmail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogInActivity extends AppCompatActivity {


    private EditText edEmail;
    private EditText edPasswd;
    private TextView title1, tv_signUp;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private List<User> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        findID();
        list = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        Intent i = new Intent(this, SignUp2Activity.class);
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(i);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent i1 = new Intent(this, MainActivity.class);
            startActivity(i1);
            finish();
        }


    }

    private void checkLogin() {
        CheckEmail checkEmail = new CheckEmail();
        boolean isValidateEmail = checkEmail.validateEmail(edEmail.getText().toString().trim());
        if (!isValidateEmail) {
            Toast.makeText(this, "Định dạng email không chính xác", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(edEmail.getText().toString().trim(), edPasswd.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SharedPreferences preferences = getSharedPreferences("user_login", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("email", edEmail.getText().toString().trim());
                                editor.commit();
                                Intent i = new Intent(LogInActivity.this, MainActivity.class);
                                startActivity(i);
                                finishAffinity();
                            } else {
                                Toast.makeText(LogInActivity.this, "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }

    }


    private void findID() {
        edEmail = (EditText) findViewById(R.id.ed_sdt);
        edPasswd = (EditText) findViewById(R.id.ed_passwd);
        title1 = (TextView) findViewById(R.id.title1);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tv_signUp = findViewById(R.id.tv_signUp);
    }

}