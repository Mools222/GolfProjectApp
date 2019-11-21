package com.example.golfprojectapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class ScoreActivity extends AppCompatActivity {
    private TextView tw_info, tw_scores;
    private GregorianCalendar chosenDateTime;
    private String lane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent startIntent = getIntent();
        lane = startIntent.getStringExtra("Lane");
        chosenDateTime = (GregorianCalendar) startIntent.getSerializableExtra("DateTime");
        tw_info = findViewById(R.id.info_tw);
        tw_info.append("\nValgt bane: " + lane);
        tw_info.append("\nValgt starttid: " + chosenDateTime.getTime().toString());

        tw_scores = findViewById(R.id.list_tw);
        tw_scores.setText("Scores:\n\n");

        firebase();
    }

    private GolfScoreTable golfScoreTable = new GolfScoreTable();

    private void firebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("golf");

//        Date startTime = new GregorianCalendar(2019, 10, 14, 10, 0, 0).getTime();
        Date startTime = chosenDateTime.getTime();

        myRef.orderByChild("time").startAt(startTime.getTime()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
//                System.out.println(dataSnapshot.getKey());

                GolfScore golfScore = dataSnapshot.getValue(GolfScore.class);
//                textView.append("Time: " + new Date(golfScore.getTime()) + "\nColor: " + golfScore.getColor() + "\n\n");
//                tw_scores.append("Time: " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(golfScore.getTime())) + "\nColor: " + golfScore.getColor() + "\n\n");

//                System.out.println("Time: " + golfScore.getTime());
//                System.out.println("Color: " + golfScore.getColor());
//                System.out.println("Previous Post ID: " + prevChildKey);

                golfScoreTable.addScore(golfScore.getColor());
                printData();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void printData() {
        HashMap<String, Integer> scores = golfScoreTable.getScores();
        String data = "Scores:";
        for (String color : scores.keySet()) {
            System.out.println(color + " xasd " + scores.get(color));
            data += "\n" + color + ": " + scores.get(color);
        }
        tw_scores.setText(data);
    }
}
