package com.example.foodapp.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodapp.Adapter.ProductAdapter;
import com.example.foodapp.Adapter.SpinnerTypeFoodAdapter;
import com.example.foodapp.Model.ItemCart;
import com.example.foodapp.Model.Product;
import com.example.foodapp.Model.TypeFood;
import com.example.foodapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private ImageView imgProduct;
    private Spinner spinnerTypeFood;
    private EditText edNameProduct;
    private EditText edPrice;
    private EditText edSale;
    private EditText edQuantity,ed_describe;
    private Button btnAddProduct,btn_imgOther;
    private List<TypeFood> listTypeFood;
    private List<Product> listProduct = new ArrayList<>();
    private ProductAdapter productAdapter;
    private TypeFood typeFood;
    private SpinnerTypeFoodAdapter spinnerTypeFoodAdapter;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    private ActivityResultLauncher<Intent> galleryLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        FindId();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        listTypeFood = new ArrayList<>();
        GallaryLauncher();
        DatabaseReference dataTypeFood = database.getReference("list_typeFood");


        dataTypeFood.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TypeFood typeFood1 = snapshot.getValue(TypeFood.class);
                if (typeFood1 != null) {
                    listTypeFood.add(typeFood1);
                }
                spinnerTypeFoodAdapter.notifyDataSetChanged();
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
        spinnerTypeFoodAdapter = new SpinnerTypeFoodAdapter(this, listTypeFood);
        spinnerTypeFood.setAdapter(spinnerTypeFoodAdapter);



        DatabaseReference databaseReference = database.getReference("list_product");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    listProduct.add(product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        SelectedSpinnerTypeFood();
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddProduct();
            }
        });
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        btn_imgOther.setOnClickListener(view -> {
            selectMultilImg();
        });

    }

    public void GallaryLauncher() {
            galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    Glide.with(getApplicationContext()).load(bitmap).into(imgProduct);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void selectMultilImg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                // Người dùng đã chọn nhiều ảnh
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    uploadImageToFirebase(imageUri);
                }
            } else if (data.getData() != null) {
                // Người dùng đã chọn một ảnh
                Uri imageUri = data.getData();
//                uploadImageToFirebase(imageUri);
            }
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        // Tạo tham chiếu đến Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Tạo tên tệp ảnh duy nhất
        String filename = UUID.randomUUID().toString();

        // Tạo tham chiếu đến vị trí tệp ảnh trên Firebase Storage
        StorageReference imageRef = storageRef.child("images/" + filename);

        // Tải tệp ảnh lên Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                String imageUrl = downloadUri.toString();
//                                imgOther.add(imageUrl);

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProductActivity.this, "Chọn ảnh mô tả thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onClickAddProduct() {
        Calendar calendar = Calendar.getInstance();
        StorageReference mountainsRef = storageReference.child("imageProduct" + calendar.getTimeInMillis() + ".png");
        imgProduct.setDrawingCacheEnabled(true);
        imgProduct.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgProduct.getDrawable()).getBitmap();
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
                        int id = 0;
                        if (listProduct == null || listProduct.isEmpty() || listProduct.size() == 0) {
                            id = 1;
                        } else {
                            Product product = listProduct.get((listProduct.size()) - 1);
                            id = product.getId() + 1;
                        }
                        String url = uri.toString();
                        String name = edNameProduct.getText().toString();
                        int priceSale = Integer.parseInt(edSale.getText().toString());
                        int price = Integer.parseInt(edPrice.getText().toString());
                        String describe = ed_describe.getText().toString();
                        Product product = new Product(id, name, url, price, priceSale,describe, typeFood,1);
                        databaseReference.child(String.valueOf(product.getId())).setValue(product, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(AddProductActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }


    public void SelectedSpinnerTypeFood() {
        spinnerTypeFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeFood = listTypeFood.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void FindId() {
        imgProduct = (ImageView) findViewById(R.id.img_type);
        spinnerTypeFood = (Spinner) findViewById(R.id.spinner_typeFood);
        edNameProduct = (EditText) findViewById(R.id.ed_nameProduct);
        edPrice = (EditText) findViewById(R.id.ed_price);
        edSale = (EditText) findViewById(R.id.ed_sale);
        edQuantity = (EditText) findViewById(R.id.ed_quantity);
        btnAddProduct = (Button) findViewById(R.id.btn_addType);
        btn_imgOther = (Button) findViewById(R.id.btn_imgOther);
        ed_describe = findViewById(R.id.ed_describe);
    }
}