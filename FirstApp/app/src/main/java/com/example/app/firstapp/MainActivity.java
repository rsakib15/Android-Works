package com.example.app.firstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Entry - Log", "Enter into onCreate() Section");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("Entry - Log" , "Enter into onStart() Section");
        this.display = findViewById(R.id.textView);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("Entry - Log" , "Enter into onResume() Section");
    }


    @Override
    protected void onPause(){
        super.onPause();
        Log.d("Entry - Log" , "Enter into onPause() Section");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("Entry - Log" , "Enter into onStop() Section");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("Entry - Log" , "Enter into onDestroy() Section");
    }

    public void upClick(View arg0){
        Log.d("Click - Event","UP Button Click");
        int n = Integer.parseInt(display.getText().toString());
        n++;
        this.display.setText("" + n+ "");
    }

    public void downClick(View arg0){
        Log.d("Click - Event","DOWN Button Click");
        int n = Integer.parseInt(display.getText().toString());
        if(n<=0){
            Toast.makeText(this,"Already in 0",Toast.LENGTH_SHORT).show();
        }
        else {
            n--;
        }
        Log.d("N : " , ""+n+"");
        this.display.setText("" + n+ "");
    }

    public void zeroClick(View arg0){
        Log.d("Click - Event","ZERO Button Click");

        int n = 0;
        this.display.setText("" + n + "");
    }

}


