package com.example.appmohinh.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appmohinh.Activity.AddProductActivity;
import com.example.appmohinh.Activity.AddTypeActivity;
import com.example.appmohinh.Activity.SearchProductActivity;
import com.example.appmohinh.Adapter.HistorySearchAdapter;
import com.example.appmohinh.Adapter.PhotoBannerAdapter;
import com.example.appmohinh.Adapter.ProductAdapter;
import com.example.appmohinh.Adapter.ProductFillterAdapter;
import com.example.appmohinh.Adapter.TypeAdapter;
import com.example.appmohinh.Model.Product;
import com.example.appmohinh.Model.ProductSearch;
import com.example.appmohinh.Model.Type;
import com.example.appmohinh.Model.photoBanner;
import com.example.appmohinh.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private ViewPager pagerBanner;
    private CircleIndicator circleIndicator;
    private PhotoBannerAdapter bannerAdapter;
    private TextView tv_addTypeFood, tv_addProduct, tv_history, tv_titleHistory;
    private Timer mTimer;
    private List<photoBanner> mlist;
    private RecyclerView recy_typeFood, recy_Product, recy_fillterProduct;
    private TypeAdapter typeFoodAdapter;
    private List<Type> listTypeFood;
    private List<Product> listProducts;
    private ProductAdapter adapter;
    private ProductFillterAdapter productFillterAdapter;
    private List<Product> listFillter;
    private List<ProductSearch> listSearch;

    private LinearLayout linner_main, linner_fillter;
    private SearchView ed_fillter;
    private ImageView btn_backFillter;
    private HistorySearchAdapter historySearchAdapter;
    private String name;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pagerBanner = view.findViewById(R.id.id_pagerBanner);
        SharedPreferences preferences = getActivity().getSharedPreferences("user_login", Context.MODE_PRIVATE);

        circleIndicator = view.findViewById(R.id.id_circleIndicator);
        tv_addTypeFood = view.findViewById(R.id.tv_addType);
        tv_addProduct = view.findViewById(R.id.id_addProduct);
        tv_history = view.findViewById(R.id.tv_history);
        tv_titleHistory = view.findViewById(R.id.tv_titleHistory);
        recy_typeFood = view.findViewById(R.id.recy_Type);
        recy_Product = view.findViewById(R.id.recy_Product);
        recy_fillterProduct = view.findViewById(R.id.recy_fillterProduct);
        linner_fillter = view.findViewById(R.id.linner_fillter);
        linner_main = view.findViewById(R.id.linner_main);
        ed_fillter = view.findViewById(R.id.ed_fillter);
        btn_backFillter = view.findViewById(R.id.btn_backFillter);
        ed_fillter.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linner_main.setVisibility(View.GONE);
                linner_fillter.setVisibility(View.VISIBLE);
                btn_backFillter.setVisibility(View.VISIBLE);
            }
        });
        btn_backFillter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linner_fillter.setVisibility(View.GONE);
                linner_main.setVisibility(View.VISIBLE);
                btn_backFillter.setVisibility(View.GONE);
            }
        });
        mlist = getListPhoto();
        bannerAdapter = new PhotoBannerAdapter(getActivity(), mlist);
        pagerBanner.setAdapter(bannerAdapter);
        circleIndicator.setViewPager(pagerBanner);
        bannerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSilde();

        tv_addTypeFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddTypeActivity.class);
                startActivity(i);
            }
        });
        tv_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddProductActivity.class);
                startActivity(i);
            }
        });
        name = preferences.getString("email", "");
        if (!name.equals("admin@gmail.com")) {
            tv_addProduct.setVisibility(View.GONE);
            tv_addTypeFood.setVisibility(View.GONE);
        }

        getDataFromRealtimeDatabase();
        setData();
        searchProduct();
    }

    public void setData() {
        listTypeFood = new ArrayList<>();
        listProducts = new ArrayList<>();
        listFillter = new ArrayList<>();
        listSearch = new ArrayList<>();
        typeFoodAdapter = new TypeAdapter(getActivity(), listTypeFood);
        adapter = new ProductAdapter(getActivity(), listProducts);
        historySearchAdapter = new HistorySearchAdapter(listSearch, getActivity());
        productFillterAdapter = new ProductFillterAdapter(getActivity(), listFillter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recy_typeFood.setAdapter(typeFoodAdapter);
        recy_Product.setAdapter(adapter);
        recy_typeFood.setLayoutManager(linearLayoutManager);
        recy_Product.setLayoutManager(linearLayoutManager2);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recy_fillterProduct.setLayoutManager(linearLayoutManager3);
        recy_fillterProduct.addItemDecoration(dividerItemDecoration);
        recy_fillterProduct.setAdapter(historySearchAdapter);


    }

    public void getDataFromRealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = database.getReference("list_typeFood");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Type typeFood = snapshot.getValue(Type.class);
                if (typeFood != null) {
                    listTypeFood.add(typeFood);
                }
                typeFoodAdapter.notifyDataSetChanged();
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

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference2 = database2.getReference("list_product");
        mDatabaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                if (product != null) {
                    listProducts.add(product);
                    listFillter.add(product);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                for (int i = 0; i < listProducts.size(); i++) {
                    if (product.getId() == listProducts.get(i).getId()) {
                        listProducts.set(i, product);
                    }
                }
                adapter.notifyDataSetChanged();
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
        FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database3.getReference("list_searchProduct");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductSearch productSearch = snapshot.getValue(ProductSearch.class);
                if (productSearch != null && productSearch.getUserSearch().equals(name)) {
                    listSearch.add(productSearch);
                }
                if (listSearch.isEmpty() || listSearch == null || listSearch.size() == 0) {
                    tv_titleHistory.setVisibility(View.VISIBLE);
                } else {
                    tv_titleHistory.setVisibility(View.GONE);
                }
                historySearchAdapter.notifyDataSetChanged();

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

    public void searchProduct() {
        ed_fillter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("list_searchProduct");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ProductSearch productSearch = dataSnapshot.getValue(ProductSearch.class);
                            listSearch.add(productSearch);
                        }
                        historySearchAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                int idSearch = 0;
                if (listSearch.isEmpty() || listSearch.size() == 0) {
                    idSearch = 1;
                } else {
                    ProductSearch productSearch = listSearch.get((listSearch.size()) - 1);
                    idSearch = productSearch.getId() + 1;
                }

                ProductSearch productSearch = new ProductSearch(idSearch, query, name);
                databaseReference.child(String.valueOf(productSearch.getId())).setValue(productSearch, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Intent i = new Intent(getActivity(), SearchProductActivity.class);
                        i.putExtra("text_search", query);
                        startActivity(i);
                        ed_fillter.clearFocus();
                        ed_fillter.setQuery("", false);
                        getDataFromRealtimeDatabase();
                        setData();
                    }
                });

                productFillterAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty() || newText.length() == 0 || newText == null) {
                    tv_history.setVisibility(View.VISIBLE);
                    recy_fillterProduct.setAdapter(historySearchAdapter);
                } else {
                    tv_history.setVisibility(View.GONE);
                    tv_titleHistory.setVisibility(View.GONE);
                    recy_fillterProduct.setAdapter(productFillterAdapter);
                }
                productFillterAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private List<photoBanner> getListPhoto() {
        List<photoBanner> list = new ArrayList<>();
        list.add(new photoBanner(R.drawable.txt1));
        list.add(new photoBanner(R.drawable.txt2));
        list.add(new photoBanner(R.drawable.txt3));

        return list;
    }

    private void autoSilde() {
        if (mlist == null || mlist.isEmpty() || pagerBanner == null) {
            return;
        }
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            int curentItem = pagerBanner.getCurrentItem();
                            int totalItem = mlist.size() - 1;
                            if (curentItem < totalItem) {
                                curentItem++;
                                pagerBanner.setCurrentItem(curentItem);
                            } else {
                                pagerBanner.setCurrentItem(0);
                            }
                        }
                    });
                }
            }, 500, 3000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}