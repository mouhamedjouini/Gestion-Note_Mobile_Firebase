package com.example.tp5;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tp5.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> students = new ArrayList<>();
    private FloatingActionButton fabAddd;
    List<String> grades = new ArrayList<>();
    private HashMap<String, List<String>> studentGrades = new HashMap<>(); // Garder une référence aux notes des étudiants

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

fabAddd=findViewById(R.id.fabAdd);
        fabAddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);

            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("GLSI").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.v("Docs", document.getId() + " => " + document.getData());
                        students.add(document.getId());


                    }

                    // Mettre à jour l'AutoCompleteTextView avec la liste des noms
                    updateAutoCompleteTextView();
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        AutoCompleteTextView textView = findViewById(R.id.a1);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = (String) parent.getItemAtPosition(position);


                db.collection("GLSI").document(selectedName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        String javaGrade = document.getString("JAVA");
                        String iosGrade = document.getString("IOS");
                        String androidGrade = document.getString("Android");
grades.clear();
                         grades.add(iosGrade);
                         grades.add(androidGrade);
                         grades.add(javaGrade);
                        studentGrades.put(selectedName, grades);


                    }
                });
                displayStudentGrades(selectedName);
            }
        });
    }


    private void updateAutoCompleteTextView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, students);
        AutoCompleteTextView textView = findViewById(R.id.a1);
        textView.setAdapter(adapter);
    }


    private void displayStudentGrades(String selectedName) {


        if (grades != null) {
            List<String> gradeWithSmile = new ArrayList<>();
            for (String grade : grades) {
                String smileIcon = getSmileIcon(grade);
                gradeWithSmile.add(" " + smileIcon + " " + grade);
            }

            ArrayAdapter<String> gradesAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, gradeWithSmile);
            ListView listView = findViewById(R.id.listView);
            listView.setAdapter(gradesAdapter);
        } else {
            Toast.makeText(this, "Pas de notes disponibles pour cet étudiant.", Toast.LENGTH_SHORT).show();
        }
    }

    // Obtenir l'icône de sourire en fonction de la note
    private String getSmileIcon(String grade) {
        int gradeInt = Integer.parseInt(grade);
        if (gradeInt >= 15) {
            return "\uD83D\uDE00"; // Smiley heureux
        } else if (gradeInt >= 10) {
            return "\uD83D\uDE10"; // Smiley neutre
        } else {
            return "\uD83D\uDE41"; // Smiley triste
        }

    }

}
