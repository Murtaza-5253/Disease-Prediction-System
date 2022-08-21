package com.igc.diseasepredictor.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.igc.diseasepredictor.R;

public class Info extends AppCompatActivity
{
    TextView txtIChronic,txtITrace,txtIPrec,txtIMed;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        init();
        Bundle b = getIntent().getExtras();
        int count = b.getInt("Count",0);
        Toast.makeText(this, ""+count, Toast.LENGTH_SHORT).show();

        String Chronic = b.getString("Chronic","No Chronic Disease Selected");
        Toast.makeText(this, Chronic, Toast.LENGTH_SHORT).show();
        if(Chronic.trim().equals(""))
        {
            txtIChronic.setText("No Chronic Disease Selected");
        }
        else {
            txtIChronic.setText(Chronic);
        }

        if(count>7 && !txtIChronic.getText().toString().equals("No Chronic Disease Selected"))
        {
            txtITrace.setText("You Need to Seek Immediete Meddical Help!\n\nPlease Get yourself Tested");
        }
        else if(count<7 && txtIChronic.getText().toString().equals("No Chronic Disease Selected"))
        {
            txtITrace.setText("Please Get yourself Tested and just take \n\nnecessary precautions");
        }
        else if(count<7 && count>4)
        {
            txtITrace.setText("Please Get yourself Tested and just take \n\nnecessary precautions periood");
        }
        else{
            txtITrace.setText("Don't Worry you are safe\n\n Just Don't go out");
        }

    }
    private void init()
    {
        txtIChronic = findViewById(R.id.txtIChronic);
        txtITrace = findViewById(R.id.txtItrace);
        txtIPrec = findViewById(R.id.txtIPrec);
        txtIMed = findViewById(R.id.txtIMed);

    }
}
