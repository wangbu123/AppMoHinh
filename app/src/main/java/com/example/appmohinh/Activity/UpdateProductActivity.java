package com.example.appmohinh.Activity;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appmohinh.Adapter.SpinnerTypeAdapter;
import com.example.appmohinh.Model.Product;
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

public class UpdateProductActivity extends AppCompatActivity {
    private ImageView NewImgProduct;
    private Spinner spinnerTypeFood;
    private EditText edNewNameProduct;
    private EditText edNewPrice;
    private EditText edNewSale;
    private EditText edNewDescribe;
    private RadioGroup rdogr;
    private RadioButton rdoConHang;
    private RadioButton rdoHetHang;
    private Button btnImgOther;
    private Button btnUpdate;
    private Product objProduct;
    private int status;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private List<Type> listTypeFood;
    private SpinnerTypeAdapter adapter;
    private ActivityResultLauncher<Intent> mlLauncher;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference mRefStorage = storage.getReference();
    private Type objTypeFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        finID();
        OpenGallary();
        listTypeFood = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        objProduct = (Product) bundle.getSerializable("objProduct");
        Glide.with(this).load(objProduct.getImg()).into(NewImgProduct);
        edNewNameProduct.setText(objProduct.getName());
        edNewDescribe.setText(objProduct.getDescribe());
        edNewPrice.setText("" + objProduct.getPrice());
        edNewSale.setText("" + objProduct.getPriceSale());
        if (objProduct.getStatus() == 1)
            rdoConHang.setChecked(true);
        if (objProduct.getStatus() == 2) {
            rdoHetHang.setChecked(true);
        }

        rdogr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rdo_conHang:
                        status = 1;
                        break;
                    case R.id.rdo_hetHang:
                        status = 2;
                        break;
                }
            }
        });

        DatabaseReference mRefTypeFood = database.getReference("list_typeFood");
        mRefTypeFood.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Type objTypeFood = dataSnapshot.getValue(Type.class);
                    if (objTypeFood != null) {
                        listTypeFood.add(objTypeFood);
                    }
                }
                adapter = new SpinnerTypeAdapter(getApplicationContext(), listTypeFood);
                spinnerTypeFood.setAdapter(adapter);
                for (int i = 0; i < spinnerTypeFood.getCount(); i++) {
                    if (objProduct.getTypeFood().getId() == listTypeFood.get(i).getId()) {
                        spinnerTypeFood.setSelection(i);
                        spinnerTypeFood.setSelected(true);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        NewImgProduct.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mlLauncher.launch(i);

        });
        btnUpdate.setOnClickListener(view -> {
            clickUpdateProduct();
        });
        spinnerTypeFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                objTypeFood = listTypeFood.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void clickUpdateProduct() {
        Calendar calendar = Calendar.getInstance();
        StorageReference mountainsRef = mRefStorage.child("imageProduct" + calendar.getTimeInMillis() + ".png");
        NewImgProduct.setDrawingCacheEnabled(true);
        NewImgProduct.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) NewImgProduct.getDrawable()).getBitmap();
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
                        DatabaseReference databaseReference = database.getReference("list_product");
                        String img = uri.toString();
                        objProduct.setImg(img);
                        objProduct.setName(edNewNameProduct.getText().toString().trim());
                        objProduct.setTypeFood(objTypeFood);
                        objProduct.setDescribe(edNewDescribe.getText().toString().trim());
                        objProduct.setPrice(Integer.parseInt(edNewPrice.getText().toString()));
                        objProduct.setPriceSale(Integer.parseInt(edNewSale.getText().toString()));
                        objProduct.setStatus(status);
                        databaseReference.child(String.valueOf(objProduct.getId())).setValue(objProduct.updateProduct(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(UpdateProductActivity.this, "Cập nhập thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });


                    }
                });
            }
        });
    }

    public void OpenGallary() {
        mlLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri Image = result.getData().getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Image);
                    Glide.with(this).load(bitmap).into(NewImgProduct);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void finID() {
        NewImgProduct = (ImageView) findViewById(R.id.NewImg_product);
        spinnerTypeFood = (Spinner) findViewById(R.id.spinner_typeFood);
        edNewNameProduct = (EditText) findViewById(R.id.ed_NewNameProduct);
        edNewPrice = (EditText) findViewById(R.id.ed_NewPrice);
        edNewSale = (EditText) findViewById(R.id.ed_NewSale);
        edNewDescribe = (EditText) findViewById(R.id.ed_NewDescribe);
        rdogr = (RadioGroup) findViewById(R.id.rdogr);
        rdoConHang = (RadioButton) findViewById(R.id.rdo_conHang);
        rdoHetHang = (RadioButton) findViewById(R.id.rdo_hetHang);
        btnImgOther = (Button) findViewById(R.id.btn_imgOther);
        btnUpdate = (Button) findViewById(R.id.btn_update);
    }
}