package com.example.chatty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GirisActivity extends AppCompatActivity {

    EditText KullaniciAdiEditText;
    Button KayıtOlButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
      tanimla();
    }

    public void tanimla() {
        KullaniciAdiEditText = (EditText) findViewById(R.id.KullaniciAdiEditText);
        KayıtOlButton = (Button) findViewById(R.id.KayıtOlButton);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    KayıtOlButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String userName=KullaniciAdiEditText.getText().toString();
            KullaniciAdiEditText.setText("");
            ekle(userName);
        }
    });
    }
              /*burada final kullandık çünkü aşırı yüklenmiş bir fonksiyonun
               içine dışarda bulunan bir değişken göndermek istiyorsan final kullanmalısın  */
    public void ekle(final String kadi) {
        databaseReference.child("kullanıcılar").child(kadi).child("kullaniciadi").setValue(kadi).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "başarı ile kayıt olundu", Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(GirisActivity.this,MainActivity.class);
                    intent.putExtra("kadi",kadi);
                    startActivity(intent);

                }
            }
        });


    }
}