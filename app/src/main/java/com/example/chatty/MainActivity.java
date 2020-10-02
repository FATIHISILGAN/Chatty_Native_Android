package com.example.chatty;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userName;
    List<String>list;
    RecyclerView userRecyclerView;


   UserAdapter userAdapter;

                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_main);
                    tanimla();
                    listele();
                }

                public  void  tanimla(){
                    userName=getIntent().getExtras().getString("kadi");
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    databaseReference=firebaseDatabase.getReference();
                    list=new ArrayList<>();

                    userRecyclerView=(RecyclerView)findViewById(R.id.userRview);
                    RecyclerView.LayoutManager layoutManager =new GridLayoutManager(MainActivity.this,2);
                    userRecyclerView.setLayoutManager(layoutManager);
                    userAdapter=new UserAdapter(MainActivity.this,list,MainActivity.this,userName);
                    userRecyclerView.setAdapter(userAdapter);
                }

                public  void listele(){

                    databaseReference.child("kullanıcılar").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            if (!snapshot.getKey().equals(userName)){

                                list.add(snapshot.getKey());

                                userAdapter.notifyDataSetChanged();

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
}