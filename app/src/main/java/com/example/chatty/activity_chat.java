package com.example.chatty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_chat extends AppCompatActivity {
    String userName, otherName;
    TextView chatUserName;
    ImageView backImage;
    ImageView sendImage;
    EditText chatEditText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView chatRecyclerView;
    MessageAdapter messageAdapter;
    List<MesajModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tanimla();
        loadMessage();
    }


    public void tanimla() {
        list= new ArrayList<>();
        userName = getIntent().getExtras().getString("username");
        otherName = getIntent().getExtras().getString("othername");
        Log.i("alınan değerler:", userName + " " + otherName);
        chatUserName=(TextView)findViewById(R.id.chatUserName);
       backImage=(ImageView) findViewById(R.id.backImage);
       sendImage=(ImageView)findViewById(R.id.sendImage);
       chatEditText=(EditText)findViewById(R.id.chatEditText);
       chatUserName.setText(otherName);

       backImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent =new Intent(activity_chat.this,MainActivity.class);
               intent.putExtra("kadi",userName);
               startActivity(intent);



           }
       });

     firebaseDatabase=FirebaseDatabase.getInstance();
     databaseReference=firebaseDatabase.getReference();
     sendImage.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             String mesaj=userName+":"+chatEditText.getText().toString();
             chatEditText.setText("");
             mesajGonder(mesaj);
         }
     });
     chatRecyclerView=(RecyclerView)findViewById(R.id.chatRview);
     RecyclerView.LayoutManager layoutManager=new GridLayoutManager(activity_chat.this,1);
     chatRecyclerView.setLayoutManager(layoutManager);
  messageAdapter=new MessageAdapter(activity_chat.this,list,activity_chat.this,userName);
  chatRecyclerView.setAdapter(messageAdapter);


    }

    public  void  mesajGonder(String text){
        final String key=databaseReference.child("mesajlar").child(userName).child(otherName).push().getKey();
        final Map messageMap=new HashMap();
        messageMap.put("text",text);
        messageMap.put("from",userName);

       databaseReference.child("mesajlar").child(userName).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){

                   databaseReference.child("mesajlar").child(otherName).child(userName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                       }
                   });
               }
           }
       });


    }
    public  void  loadMessage(){
         databaseReference.child("mesajlar").child(userName).child(otherName).addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MesajModel mesajModel=snapshot.getValue(MesajModel.class);
                list.add(mesajModel);


                messageAdapter.notifyDataSetChanged();
                 chatRecyclerView.scrollToPosition(list.size()-100);
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