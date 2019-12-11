package com.example.golfprojectapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class ScoreActivity extends AppCompatActivity {
    private String courseNumber;
    private GregorianCalendar chosenDateTime;
    private TableLayout tableLayoutHeader;
    private TableLayout tableLayoutData;
    private String[] headerColumns = new String[3];
    private int numberOfColumns = headerColumns.length;
    private GolfScoreTable golfScoreTable = new GolfScoreTable();
    private TextView twNoScoresFound;
    private int id;
    private HashMap<String, String> translateColorsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent startIntent = getIntent();
        courseNumber = startIntent.getStringExtra("Course");
        chosenDateTime = (GregorianCalendar) startIntent.getSerializableExtra("DateTime");

        String patternTime = "HH:mm";
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat(patternTime, new Locale("da", "DK"));

        TextView twInfo = findViewById(R.id.tw_info);
        twInfo.append("Valgt bane: " + courseNumber);
        twInfo.append("\nValgt starttid: " + simpleDateFormatTime.format(chosenDateTime.getTime()));

        twNoScoresFound = findViewById(R.id.tw_no_scores);

        tableLayoutHeader = findViewById(R.id.tableLayout_header);
        tableLayoutData = findViewById(R.id.tableLayout_data);

        headerColumns = new String[]{"Id", "Farve", "Score"};

        translateColorsMap = new HashMap<>();
        translateColorsMap.put("red", "Rød");
        translateColorsMap.put("green", "Grøn");
        translateColorsMap.put("blue", "Blå");
        translateColorsMap.put("yellow", "Gul");

        firebase();
    }

    private void firebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("golf/" + courseNumber);

//        Date startTime = new GregorianCalendar(2019, 10, 14, 10, 0, 0).getTime();
        Date startTime = chosenDateTime.getTime();

        myRef.orderByChild("time").startAt(startTime.getTime()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                GolfScore golfScore = dataSnapshot.getValue(GolfScore.class);
                golfScoreTable.addScore(golfScore.getColor());
                printTable();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                printTable();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void printTable() {
        twNoScoresFound.setVisibility(View.GONE);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        layoutParams.setMargins(2, 2, 2, 2);

        id = 1;
        printHeader(layoutParams);
        printData(layoutParams);
    }

    private void printHeader(TableRow.LayoutParams layoutParams) {
        tableLayoutHeader.removeAllViews();

        TableRow tableRowHeader = new TableRow(this);

        for (int i = 0; i < numberOfColumns; i++) {
            addTextViewToTableRow(tableRowHeader, "header", layoutParams, headerColumns[i]);
        }

        tableLayoutHeader.addView(tableRowHeader);
    }

    private void printData(TableRow.LayoutParams layoutParams) {
        HashMap<String, Integer> scores = golfScoreTable.getScores();

        tableLayoutData.removeAllViews();

        for (String color : scores.keySet()) {
            TableRow tableRowData = new TableRow(this);
            addTextViewToTableRow(tableRowData, "data", layoutParams, String.valueOf(id));
            addTextViewToTableRow(tableRowData, "data", layoutParams, translateColorsMap.get(color));
            addTextViewToTableRow(tableRowData, "data", layoutParams, String.valueOf(scores.get(color)));
            tableLayoutData.addView(tableRowData);
            ++id;
        }
    }

    private void addTextViewToTableRow(TableRow tableRow, String rowType, TableRow.LayoutParams layoutParams, String text) {
        TextView textView = new TextView(this);
//        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whitebackground));

        if (rowType.equals("header"))
            textView.setBackgroundResource(R.color.gray);
        else
            textView.setBackgroundResource(id % 2 == 0 ? R.color.lightGray : R.color.white);

        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);

        tableRow.addView(textView);
    }
}
