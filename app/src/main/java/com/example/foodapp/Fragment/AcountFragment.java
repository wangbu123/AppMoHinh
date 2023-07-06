package com.example.foodapp.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Activity.ChangeInforUserActivity;
import com.example.foodapp.Activity.HistoryActivity;
import com.example.foodapp.Activity.ListUserActivity;
import com.example.foodapp.Activity.LogInActivity;
import com.example.foodapp.Model.User;
import com.example.foodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcountFragment extends Fragment {
    private Button btn_logout;
    private CircleImageView imgAvtUser;
    private TextView tvNameUser;
    private TextView tvEmailUser;
    private Button btnChangeInforUser;
    private Button btnHistoryBuy, btn_listUser;
    private String idUser;
    private List<User> listUser;
    private User objUser;


    public AcountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AcountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcountFragment newInstance(String param1, String param2) {
        AcountFragment fragment = new AcountFragment();

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
        return inflater.inflate(R.layout.fragment_acount, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_logout = view.findViewById(R.id.btn_logout);
        imgAvtUser = (CircleImageView) view.findViewById(R.id.img_avtUser);
        tvNameUser = (TextView) view.findViewById(R.id.tv_nameUser);
        tvEmailUser = (TextView) view.findViewById(R.id.tv_emailUser);
        btnChangeInforUser = (Button) view.findViewById(R.id.btn_changeInforUser);
        btnHistoryBuy = (Button) view.findViewById(R.id.btn_historyBuy);
        btn_listUser = view.findViewById(R.id.btn_goToListUser);
        listUser = new ArrayList<>();
        SharedPreferences preferences = getActivity().getSharedPreferences("user_login", getActivity().MODE_PRIVATE);
        String email = preferences.getString("email", "");
        if (email.equals("admin@gmail.com")) {
            btnHistoryBuy.setVisibility(View.GONE);
        } else {
            btn_listUser.setVisibility(View.GONE);
        }
        getInforUser();
        btn_listUser.setOnClickListener(view1 -> {
            Intent i = new Intent(getActivity(), ListUserActivity.class);
            startActivity(i);
        });
        btnChangeInforUser.setOnClickListener(view1 -> {
            Intent i = new Intent(getActivity(), ChangeInforUserActivity.class);
            startActivity(i);
        });
        btnHistoryBuy.setOnClickListener(view1 -> {
            Intent i = new Intent(getActivity(), HistoryActivity.class);
            startActivity(i);
        });
        btn_logout.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getActivity(), LogInActivity.class);
            startActivity(i);
            getActivity().finishAffinity();
        });

    }

    private void getInforUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getUid();
        SharedPreferences preferences = getActivity().getSharedPreferences("user_login", getActivity().MODE_PRIVATE);
        String email = preferences.getString("email", "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRefUser = database.getReference("list_user");
        mRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User objUser = dataSnapshot.getValue(User.class);
                    if (objUser.getPhoneNumber().equals(email)) {
                        listUser.add(objUser);
                    }
                }
                objUser = listUser.get((listUser.size()) - 1);
                Glide.with(getActivity()).load(objUser.getImg()).error(R.drawable.avatar).into(imgAvtUser);
                tvEmailUser.setText(objUser.getPhoneNumber());
                tvNameUser.setText(objUser.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getInforUser();
    }
}