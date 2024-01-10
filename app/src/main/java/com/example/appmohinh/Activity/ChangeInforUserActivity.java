package com.example.appmohinh.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appmohinh.Model.User;
import com.example.appmohinh.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeInforUserActivity extends AppCompatActivity {
    private CircleImageView imgUser;
    private EditText edNameUser;
    private EditText edPhoneNumber;
    private EditText edBirthday;
    private Button btnChangeProfile;
    private List<User> listUser = new ArrayList<>();
    User objUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    int mYear, mMonth, mDay;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    private ActivityResultLauncher<Intent> galleryLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_infor_user);
        findId();
        GallaryLauncher();
        getInforUser();
        edBirthday.setFocusable(false);
        edBirthday.setFocusableInTouchMode(false);

        imgUser.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);
        });
        edBirthday.setOnClickListener(view -> {
            showDiaLigBirthDay();
        });


        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageReference.child("imageProduct" + calendar.getTimeInMillis() + ".png");
                imgUser.setDrawingCacheEnabled(true);
                imgUser.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgUser.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String url = uri.toString();
                                User mObjUser = listUser.get(listUser.size() - 1);
                                mObjUser.setBirthday(edBirthday.getText().toString().trim());
                                mObjUser.setName(edNameUser.getText().toString().trim());
                                mObjUser.setPhoneNumber2(edPhoneNumber.getText().toString().trim());
                                mObjUser.setImg(url);

                                DatabaseReference mRefUpdateProfile = database.getReference("list_user");
                                mRefUpdateProfile.child(String.valueOf(mObjUser.getId())).setValue(mObjUser.updateProfile(), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Toast.makeText(ChangeInforUserActivity.this, "Cập nhập thành công", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                    }
                });


            }
        });
    }

    private void findId() {
        imgUser = (CircleImageView) findViewById(R.id.img_user);
        edNameUser = (EditText) findViewById(R.id.ed_nameUser);
        edPhoneNumber = (EditText) findViewById(R.id.ed_phoneNumber);
        edBirthday = (EditText) findViewById(R.id.ed_birthday);
        btnChangeProfile = (Button) findViewById(R.id.btn_changeProfile);
    }

    public void GallaryLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                // Xử lý ảnh đã chọn ở đây
                Uri selectedImageUri = result.getData().getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    Glide.with(getApplicationContext()).load(bitmap).into(imgUser);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    public void showDiaLigBirthDay() {
        DatePickerDialog.OnDateSetListener date = (datePicker, year, month, day) -> {
            mYear = year;
            mMonth = month;
            mDay = day;
            GregorianCalendar calendar = new GregorianCalendar(mYear, mMonth, mDay);
            edBirthday.setText(format.format(calendar.getTime()));
        };
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                date, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void getInforUser() {
        SharedPreferences preferences = getSharedPreferences("user_login", MODE_PRIVATE);
        String email = preferences.getString("email", "");
        DatabaseReference mRefUser = database.getReference("list_user");
        mRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User mObjUser = dataSnapshot.getValue(User.class);
                    if (mObjUser != null && mObjUser.getPhoneNumber().equals(email)) {
                        listUser.add(mObjUser);
                    }
                    objUser = listUser.get((listUser.size()) - 1);
                    Glide.with(getApplicationContext()).load(objUser.getImg()).error(R.drawable.avatar).into(imgUser);
                    edBirthday.setText(objUser.getBirthday());
                    edNameUser.setText(objUser.getName());
                    edPhoneNumber.setText(objUser.getPhoneNumber2());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}