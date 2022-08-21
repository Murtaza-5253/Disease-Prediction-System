package com.igc.diseasepredictor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.igc.diseasepredictor.R;

public class Welcome_Activity extends AppCompatActivity
{
    SharedPreferences sp;
    String status;
    int SPLASH_SCREEN = 5000;
    RelativeLayout lyt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sp=getSharedPreferences("UserInfo",MODE_PRIVATE);
        status=sp.getString("STATUS","Guest");
        lyt = findViewById(R.id.lyt);


    }

    public void getStarted(View view)
        {
            if(status.equalsIgnoreCase("SUCCESS"))
            {
                Intent i =new Intent(Welcome_Activity.this,DashBoard_Activity.class);
                ActivityOptions options1 = null;

                    //options1 = ActivityOptions.makeClipRevealAnimation(view,0,0,view.getWidth(),view.getHeight());
                startActivity(i);
                    finish();


            }
            else
            {
                Intent i =new Intent(Welcome_Activity.this,Login_Activity.class);


                startActivity(i);
                    finish();
            }
        }

}