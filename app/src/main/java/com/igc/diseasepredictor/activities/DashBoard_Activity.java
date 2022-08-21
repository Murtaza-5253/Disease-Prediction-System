package com.igc.diseasepredictor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.igc.diseasepredictor.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


public class DashBoard_Activity extends AppCompatActivity
{
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drwlLayout;
    NavigationView nav;
    Animation anim;
    Button btnDiseasePredict,btnCoronaTracer,btnHpFinder;
    CircularImageView imgProfile;
    SharedPreferences sp;
    String name;


    FirebaseAuth fbAuth;
    TextView txtLName;
    StorageReference fbStore;
    FirebaseDatabase fbDatabase;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        btnDiseasePredict = findViewById(R.id.btnDiseasePredict);
        btnCoronaTracer = findViewById(R.id.btnCoronaTracer);
        btnHpFinder = findViewById(R.id.btnHpFinder);
        drwlLayout = findViewById(R.id.drwlLayout);
        //txtLName = findViewById(R.id.txtLName);
        fbAuth = FirebaseAuth.getInstance();
        //imgProfile = findViewById(R.id.imgProfile);
        nav = findViewById(R.id.nav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_format_align_center_24);

        toggle = new ActionBarDrawerToggle(this,drwlLayout,toolbar,R.string.open,R.string.close);

        drwrNavigate();

                sp = getSharedPreferences("UserInfo",MODE_PRIVATE);
                name = sp.getString("NAME","Guest");
                Toast.makeText(DashBoard_Activity.this, ""+name, Toast.LENGTH_SHORT).show();
                txtLName.setText(name);
                Uri img = Uri.parse(sp.getString("IMAGEURL","Guest"));
                uri = img;
                Picasso.with(this).load(uri).into(imgProfile);
                //imgProfile.setImageURI(uri);
                Toast.makeText(DashBoard_Activity.this, ""+img, Toast.LENGTH_SHORT).show();






    }
    private void drwrNavigate()
    {
        View v = getLayoutInflater().inflate(R.layout.header_dash,null);
        txtLName = v.findViewById(R.id.txtLName);
        imgProfile = v.findViewById(R.id.imgProfile);

        nav.addHeaderView(v);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.mnuPredict:
                        Toast.makeText(DashBoard_Activity.this, "Predict", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DashBoard_Activity.this,DiseasePredictor_Activity.class));
                        finish();
                        break;
                    case R.id.mnuTrace:
                        startActivity(new Intent(DashBoard_Activity.this,Corona_Tracer_Activity.class));
                        finish();
                        break;
                    case R.id.mnuHpFind:
                        startActivity(new Intent(DashBoard_Activity.this,Hospital_List_Activity.class));
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    public void DPredictor(View view)
    {
        Button btnTemp = (Button) view;

        switch (btnTemp.getId())
        {
            case R.id.btnDiseasePredict:
                startActivity(new Intent(DashBoard_Activity.this,DiseasePredictor_Activity.class));
                finish();
                break;
            case R.id.btnCoronaTracer:
                startActivity(new Intent(DashBoard_Activity.this,Corona_Tracer_Activity.class));
                finish();
                break;
            case R.id.btnHpFinder:
                startActivity(new Intent(DashBoard_Activity.this,Hospital_List_Activity.class));
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder dlg = new AlertDialog.Builder(DashBoard_Activity.this);
        dlg.setTitle("Confirm to Back !!!");
        dlg.setMessage("Are you sure you want to Exit???");
        dlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                //startActivity(new Intent(DashBoard_Activity.this,DashBoard_Activity.class));
            }
        });
        dlg.setNegativeButton("NO", null);
        dlg.show();
    }

    public void HpFinder(View view) {
    }
}