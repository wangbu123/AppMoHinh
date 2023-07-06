package com.example.foodapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodapp.MainActivity;
import com.example.foodapp.Model.User;
import com.example.foodapp.R;
import com.example.foodapp.Support.CheckEmail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUp2Activity extends AppCompatActivity {
    private EditText edName, ed_email;
    private EditText edPasswd;
    private EditText edPasswd2;
    private Button btnNext;
    private List<User> listUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        findID();
        getCountUser();
        mAuth = FirebaseAuth.getInstance();
        listUser = new ArrayList<>();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSignUpUser();
            }
        });
    }

    public void getCountUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mAuth = database.getReference("list_user");
        mAuth.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User objUser = snapshot.getValue(User.class);
                if (objUser != null) {
                    listUser.add(objUser);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ClickSignUpUser() {
        CheckEmail checkEmail = new CheckEmail();
        boolean isValidateEmail = checkEmail.validateEmail(ed_email.getText().toString().trim());
        if (isValidateEmail) {
            mAuth.createUserWithEmailAndPassword(ed_email.getText().toString().trim(), edPasswd.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference mAuth = database.getReference("list_user");
                                int id = 0;
                                if (listUser.isEmpty() || listUser.size() == 0 || listUser == null) {
                                    id = 1;
                                } else {
                                    User user = listUser.get(listUser.size() - 1);
                                    id = user.getId() + 1;
                                }
                                String email = ed_email.getText().toString().trim();
                                String name = edName.getText().toString().trim();
                                String passwd = edPasswd.getText().toString().trim();
                                String passwd2 = edPasswd2.getText().toString().trim();
                                if (!passwd2.equalsIgnoreCase(passwd)) {
                                    Toast.makeText(getApplicationContext(), "Xác nhận mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String idUser = mUser.getUid();
                                    User user = new User(id, email, name, "", "", "", passwd,idUser);

                                    mAuth.child(String.valueOf(user.getId())).setValue(user, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            SharedPreferences preferences = getSharedPreferences("user_login", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("email", ed_email.getText().toString().trim());
                                            editor.commit();
                                            Intent i = new Intent(SignUp2Activity.this, MainActivity.class);
                                            startActivity(i);
                                            finishAffinity();
                                            Toast.makeText(SignUp2Activity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Đăng ký không thành công",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Định dạng email không chính xác", Toast.LENGTH_SHORT).show();
            return;
        }

    }


    private void findID() {
        edName = (EditText) findViewById(R.id.ed_name);
        edPasswd = (EditText) findViewById(R.id.ed_passwd);
        edPasswd2 = (EditText) findViewById(R.id.ed_passwd2);
        btnNext = (Button) findViewById(R.id.btn_next);
        ed_email = findViewById(R.id.ed_email);
    }
}