package com.example.appmohinh.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appmohinh.Model.Type;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTypeActivity extends AppCompatActivity {
    private ImageView imgType;
    private EditText edNameType;
    private Button btnAddType;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    List<Type> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);
        findId();
        getCountIdTypeFood();
        imgType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChooseImg();
            }
        });
        btnAddType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTypeFood();
            }
        });
    }

    ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent i = result.getData();
                        if (i == null) {
                            return;
                        }
                        Uri uri = i.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            Glide.with(getApplicationContext()).load(bitmap).into(imgType);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

    private void onClickChooseImg() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(i);
    }

    private void addTypeFood() {
        Calendar calendar = Calendar.getInstance();
        StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");
        imgType.setDrawingCacheEnabled(true);
        imgType.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgType.getDrawable()).getBitmap();
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
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mReference = database.getReference("list_typeFood");
                        int id = 0;
                        if (list == null || list.isEmpty() || list.size() == 0) {
                            id = 1;
                        }else {
                            Type typeFood = list.get((list.size())-1);
                            id = typeFood.getId() +1;
                        }
                        String url = uri.toString();
                        String name = edNameType.getText().toString();
                        Type typeFood = new Type(id,url,name);
                        mReference.child(String.valueOf(typeFood.getId())).setValue(typeFood, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(AddTypeActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                });
            }
        });

    }

    public void getCountIdTypeFood() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mReference = database.getReference("list_typeFood");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Type typeFood = dataSnapshot.getValue(Type.class);
                    list.add(typeFood);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void findId() {
        imgType = (ImageView) findViewById(R.id.img_type);
        edNameType = (EditText) findViewById(R.id.ed_nameType);
        btnAddType = (Button) findViewById(R.id.btn_addType);
    }
}