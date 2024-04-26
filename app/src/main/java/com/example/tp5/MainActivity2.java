package com.example.tp5;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {

        private EditText edtName, edtJava, edtIOS, edtAndroid;
        private Button btnAddStudent;
        private FirebaseFirestore db;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);

            edtName = findViewById(R.id.edtName);
            edtJava = findViewById(R.id.edtJava);
            edtIOS = findViewById(R.id.edtIOS);
            edtAndroid = findViewById(R.id.edtAndroid);
            btnAddStudent = findViewById(R.id.btnAddStudent);
            db = FirebaseFirestore.getInstance();

            btnAddStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = edtName.getText().toString().trim();
                    String JAVA = edtJava.getText().toString().trim();
                    String IOS = edtIOS.getText().toString().trim();
                    String Android = edtAndroid.getText().toString().trim();


                     db.collection("GLSI").document(name).set(new HashMap<String, Object>() {{
                         put("JAVA", JAVA);
                         put("IOS", IOS);
                         put("Android", Android);
                     }}).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void aVoid) {
                             Toast.makeText(MainActivity2.this, "Étudiant ajouté avec succès", Toast.LENGTH_SHORT).show();
                             finish();
                         }
                     });

                }
            });
        }
    }
