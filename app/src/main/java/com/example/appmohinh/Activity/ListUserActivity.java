package com.example.appmohinh.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.appmohinh.Adapter.UserAdapter;
import com.example.appmohinh.Model.User;
import com.example.appmohinh.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListUserActivity extends AppCompatActivity {
    private SearchView edFillterUser;
    private RecyclerView recyUser;
    private List<User> listUser;
    private UserAdapter adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        edFillterUser = (SearchView) findViewById(R.id.ed_fillterUser);
        recyUser = (RecyclerView) findViewById(R.id.recy_user);
        listUser = new ArrayList<>();
        DatabaseReference mRefUser = database.getReference("list_user");
        mRefUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User objUser = snapshot.getValue(User.class);
                if (objUser != null) {
                    listUser.add(objUser);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User objUser = snapshot.getValue(User.class);
                for (int i = 0; i < listUser.size(); i++) {
                    if (objUser.getId() == listUser.get(i).getId()) {
                        listUser.set(i, objUser);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User objUser = snapshot.getValue(User.class);
                for (int i = 0; i < listUser.size(); i++) {
                    if (objUser.getId() == listUser.get(i).getId()) {
                        listUser.remove(i);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new UserAdapter(this, listUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyUser.setAdapter(adapter);
        recyUser.setLayoutManager(linearLayoutManager);
        searchUser();

    }

    public void searchUser() {
        edFillterUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}