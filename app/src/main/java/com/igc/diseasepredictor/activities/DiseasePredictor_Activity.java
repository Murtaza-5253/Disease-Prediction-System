package com.igc.diseasepredictor.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.igc.diseasepredictor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class DiseasePredictor_Activity extends AppCompatActivity
{
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drwrDisease;
    NavigationView nav;
    Animation anim;
    TabHost tabHost;
    ArrayList<String> DirectSearch=new ArrayList<>();
    SpinnerDialog spinnerDialog;
    RequestQueue rq;
    ProgressDialog pd;
    ArrayList<String> SymptomA,SymptomB,SymptomC,SymptomD,SymptomE,DiseaseList;
    String SymptA,SymptB,SymptC,SymptD,SymptE;
    TextView txtSymp1,txtSymp2,txtSymp3,txtSymp4,txtSymp5,txtOtherSymps;
    TextView txtDisease,txtDirDiseas;
    LinearLayout lnr;
    Switch aSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_predictor);
        tabHost = findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec sp1 = tabHost.newTabSpec("tab 1");
        sp1.setContent(R.id.tab1);
        sp1.setIndicator("Predict Disease");
        tabHost.addTab(sp1);

        /*TabHost.TabSpec sp2 = tabHost.newTabSpec("tab 2");
        sp2.setContent(R.id.tab2);
        sp2.setIndicator("Direct Predict");
        tabHost.addTab(sp2);*/
        init();


        Access();
    }
    private void init()
    {
        drwrDisease = findViewById(R.id.drwrDisease);
        nav = findViewById(R.id.nav);
        toolbar = findViewById(R.id.toolbar);
        txtSymp1 = findViewById(R.id.txtSymp1);
        txtSymp2 = findViewById(R.id.txtSymp2);
        txtSymp3 = findViewById(R.id.txtSymp3);
        txtSymp4 = findViewById(R.id.txtSymp4);
        txtSymp5  =findViewById(R.id.txtSymp5);
        txtDisease = findViewById(R.id.txtDisease);
        txtDirDiseas = findViewById(R.id.txtDirDisease);
        txtOtherSymps = findViewById(R.id.txtOtherSym);
        lnr = findViewById(R.id.lnr);
        aSwitch = findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    lnr.setVisibility(View.VISIBLE);
                }
                else {
                    lnr.setVisibility(View.GONE);
                }
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(DiseasePredictor_Activity.this);
                dlg.setTitle("Confirm to Back !!!");
                dlg.setMessage("Are you sure you want to Exit???");
                dlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(new Intent(DiseasePredictor_Activity.this,DashBoard_Activity.class));
                    }
                });
                dlg.setNegativeButton("NO", null);
                dlg.show();

            }
        });

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_format_align_center_24);
       // toggle = new ActionBarDrawerToggle(this,drwrDisease,toolbar,R.string.open,R.string.close);
        //toggle.syncState();
        rq = Volley.newRequestQueue(DiseasePredictor_Activity.this);
        pd = new ProgressDialog(DiseasePredictor_Activity.this);

        SymptomA = new ArrayList<String>();
        SymptomB= new ArrayList<String>();
        SymptomC= new ArrayList<String>();
        SymptomD= new ArrayList<String>();
        SymptomE = new ArrayList<String>();
        DiseaseList= new ArrayList<String>();



    }
    public void Symp1(View v)
    {
        Collections.sort(SymptomA);
        spinnerDialog = new SpinnerDialog(DiseasePredictor_Activity.this,SymptomA,"Select Symptoms","Close Items");
        spinnerDialog.setCancellable(false);
        spinnerDialog.setShowKeyboard(false);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                txtSymp1.setText(item);
                SymptA = txtSymp1.getText().toString();
                symB(SymptA);
                if(!txtSymp2.getText().toString().equalsIgnoreCase("select") || !txtSymp3.getText().toString().equalsIgnoreCase("select") || !txtSymp4.getText().toString().equalsIgnoreCase("select") || !txtSymp5.getText().toString().equalsIgnoreCase("select"))
                {
                    txtSymp2.setText("Select");
                    txtSymp3.setText("Select");
                    txtSymp4.setText("Select");
                    txtSymp5.setText("Select");
                }

                /*txtSymp1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tvA = (TextView) v;
                        SymptA = tvA.getText().toString();
                        //symB(tvA.getText().toString());
                    }
                });*/

            }
        });
        //final EditText txtTemp = (EditText) v;


        spinnerDialog.showSpinerDialog();
        

        //Toast.makeText(this, ""+txtTemp.getText(), Toast.LENGTH_SHORT).show();
    }
    public void Symp2(View v)
    {
        Collections.sort(SymptomB);
        //final EditText txtTemp = (EditText) v;
        spinnerDialog = new SpinnerDialog(DiseasePredictor_Activity.this,SymptomB,"Select Symptoms","Close Items");
        spinnerDialog.setCancellable(false);
        spinnerDialog.setShowKeyboard(false);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                txtSymp2.setText(item);
                SymptB = txtSymp2.getText().toString();
                symC(SymptB);
                /*txtSymp1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tvA = (TextView) v;
                        SymptA = tvA.getText().toString();
                        //symB(tvA.getText().toString());
                    }
                });*/

            }
        });
        //symA();

        spinnerDialog.showSpinerDialog();


        //Toast.makeText(this, ""+txtTemp.getText(), Toast.LENGTH_SHORT).show();
    }
    public void Symp3(View v)
    {
        Collections.sort(SymptomC);
        //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        spinnerDialog = new SpinnerDialog(DiseasePredictor_Activity.this,SymptomC,"Select Symptoms","Close Items");
        spinnerDialog.setCancellable(false);
        spinnerDialog.setShowKeyboard(false);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                txtSymp3.setText(item);
                SymptC = txtSymp3.getText().toString();
                symD(SymptC);
                /*txtSymp1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tvA = (TextView) v;
                        SymptA = tvA.getText().toString();
                        //symB(tvA.getText().toString());
                    }
                });*/

            }
        });
        spinnerDialog.showSpinerDialog();


        //Toast.makeText(this, ""+txtTemp.getText(), Toast.LENGTH_SHORT).show();
    }
    public void Symp4(View v)
    {
        Collections.sort(SymptomD);

        spinnerDialog = new SpinnerDialog(DiseasePredictor_Activity.this,SymptomD,"Select Symptoms","Close Items");
        spinnerDialog.setCancellable(false);
        spinnerDialog.setShowKeyboard(false);
        spinnerDialog.bindOnSpinerListener((item, position) -> {
            txtSymp4.setText(item);
            SymptD = txtSymp4.getText().toString();
            symE(SymptD);


            /*txtSymp1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tvA = (TextView) v;
                    SymptA = tvA.getText().toString();
                    //symB(tvA.getText().toString());
                }
            });*/

        });
        spinnerDialog.showSpinerDialog();
    }
    public void Symp5(View view)
    {
        Collections.sort(SymptomE);

        spinnerDialog = new SpinnerDialog(DiseasePredictor_Activity.this,SymptomE,"Select Symptoms","Close Items");
        spinnerDialog.setCancellable(false);
        spinnerDialog.setShowKeyboard(false);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                txtSymp5.setText(item);
                SymptE = txtSymp5.getText().toString();



                /*txtSymp1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tvA = (TextView) v;
                        SymptA = tvA.getText().toString();
                        //symB(tvA.getText().toString());
                    }
                });*/

            }
        });
        spinnerDialog.showSpinerDialog();
    }



    public void Predict(View v)
    {
        if(txtSymp1.getText().toString().equals("Select"))
        {

            Toast.makeText(this, "Please Select all The Symptoms", Toast.LENGTH_SHORT).show();
        }
        else if(txtSymp2.getText().toString().equals("Select"))
        {
            //Toast.makeText(this, "Please Select all The Symptoms", Toast.LENGTH_SHORT).show();
            SearchDisease(SymptA);
        }
        else if(txtSymp3.getText().toString().equals("Select"))
        {
           // Toast.makeText(this, "Please Select all The Symptoms", Toast.LENGTH_SHORT).show();
            SearchDisease(SymptA,SymptB);
        }
        else if(txtSymp4.getText().toString().equals("Select"))
        {
            // Toast.makeText(this, "Please Select all The Symptoms", Toast.LENGTH_SHORT).show();
            SearchDisease(SymptA,SymptB,SymptC);
        }
        else if(txtSymp5.getText().toString().equals("Select"))
        {
            //Toast.makeText(this, "Please Select all The Symptoms", Toast.LENGTH_SHORT).show();
            SearchDisease(SymptA,SymptB,SymptC,SymptD);
        }
        else
        {
            SearchDisease(SymptA,SymptB,SymptC,SymptD,SymptE);
        }


    }

    private void SearchDisease(String symptA, String symptB, String symptC, String symptD)
    {
        pd.show();
        txtDisease.setText("");
        String url = "https://diseasepredictor.horizonfoodie.com/diseaseDemo.php?SymptA="+symptA+"&SymptB="+symptB+"&SymptC="+symptC+"&SymptD="+symptD;
        JsonObjectRequest jsrqPD = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    try
                    {
                        pd.dismiss();
                        JSONArray jsarray = response.getJSONArray("result");
                        // Toast.makeText(DiseasePredictor_Activity.this, ""+jsarray.length(), Toast.LENGTH_SHORT).show();
                        for(int i = 0;i<jsarray.length();i++)
                        {
                            JSONObject jsobj = jsarray.getJSONObject(i);
                            // Toast.makeText(DiseasePredictor_Activity.this,""+jsobj.getString("DiseaseName"),Toast.LENGTH_LONG).show();
                            DiseaseList.add(i,jsobj.getString("DiseaseName"));
                            txtDisease.append(jsobj.getString("DiseaseName")+", ");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(DiseasePredictor_Activity.this, "error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        rq.add(jsrqPD);
    }

    private void SearchDisease(String symptA, String symptB, String symptC)
    {
        pd.show();
        txtDisease.setText("");
        String url = "https://diseasepredictor.horizonfoodie.com/diseaseDemo.php?SymptA="+symptA+"&SymptB="+symptB+"&SymptC="+symptC;
        JsonObjectRequest jsrqPD = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            pd.dismiss();
                            JSONArray jsarray = response.getJSONArray("result");
                            // Toast.makeText(DiseasePredictor_Activity.this, ""+jsarray.length(), Toast.LENGTH_SHORT).show();
                            for(int i = 0;i<jsarray.length();i++)
                            {
                                JSONObject jsobj = jsarray.getJSONObject(i);
                                // Toast.makeText(DiseasePredictor_Activity.this,""+jsobj.getString("DiseaseName"),Toast.LENGTH_LONG).show();
                                DiseaseList.add(i,jsobj.getString("DiseaseName"));
                                txtDisease.append(jsobj.getString("DiseaseName")+",");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(DiseasePredictor_Activity.this, "error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        rq.add(jsrqPD);
    }

    private void SearchDisease(String symptA, String symptB)
    {
        pd.show();
        txtDisease.setText("");
        String url = "https://diseasepredictor.horizonfoodie.com/diseaseDemo.php?SymptA="+symptA+"&SymptB="+symptB;
        JsonObjectRequest jsrqPD = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            pd.dismiss();
                            JSONArray jsarray = response.getJSONArray("result");
                            // Toast.makeText(DiseasePredictor_Activity.this, ""+jsarray.length(), Toast.LENGTH_SHORT).show();
                            for(int i = 0;i<jsarray.length();i++)
                            {
                                JSONObject jsobj = jsarray.getJSONObject(i);
                                // Toast.makeText(DiseasePredictor_Activity.this,""+jsobj.getString("DiseaseName"),Toast.LENGTH_LONG).show();
                                DiseaseList.add(i,jsobj.getString("DiseaseName"));
                                txtDisease.append(jsobj.getString("DiseaseName")+", ");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(DiseasePredictor_Activity.this, "error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        rq.add(jsrqPD);
    }

    private void SearchDisease(String symptA)
    {
        pd.show();
        txtDisease.setText("");
        String url = "https://diseasepredictor.horizonfoodie.com/diseaseDemo.php?SymptA="+symptA;
        JsonObjectRequest jsrqPD = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            pd.dismiss();
                            JSONArray jsarray = response.getJSONArray("result");
                            // Toast.makeText(DiseasePredictor_Activity.this, ""+jsarray.length(), Toast.LENGTH_SHORT).show();
                            for(int i = 0;i<jsarray.length();i++)
                            {
                                JSONObject jsobj = jsarray.getJSONObject(i);
                                // Toast.makeText(DiseasePredictor_Activity.this,""+jsobj.getString("DiseaseName"),Toast.LENGTH_LONG).show();
                                DiseaseList.add(i,jsobj.getString("DiseaseName"));
                                txtDisease.append(jsobj.getString("DiseaseName")+",");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(DiseasePredictor_Activity.this, "error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        rq.add(jsrqPD);
    }

    public void DSearch(View v)
    {
        Toast.makeText(this, "Direct Disease Search", Toast.LENGTH_SHORT).show();
    }
    private void Access()
    {
        pd.setMessage("Data Processing...");
        pd.show();
        try {
            symA();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "SOme Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
        //DirectSearch();

    }
    public void symA()
    {
        SymptomA.clear();
        String url = "https://diseasepredictor.horizonfoodie.com/PreSymA.php";
        JsonObjectRequest jsrqA = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {

                            pd.dismiss();
                            JSONArray jsarray = response.getJSONArray("result");
                            if(jsarray.length()==0)
                            {
                                Toast.makeText(DiseasePredictor_Activity.this, "No Symptoms Available Right Now", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {


                            //Toast.makeText(DiseasePredictor_Activity.this, ""+jsarray.getString(0), Toast.LENGTH_SHORT).show();
                            //SymptomA.add(0,"Select");
                            for(int i = 0;i<jsarray.length()+1;i++)
                            {
                                JSONObject jsobj = jsarray.getJSONObject(i);
                                SymptomA.add(i,jsobj.getString("SymptomA"));
                                //Toast.makeText(DiseasePredictor_Activity.this, ""+SymptomA.get(i), Toast.LENGTH_SHORT).show();
                            }
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Error",""+ e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(DiseasePredictor_Activity.this, "Error Data Not Retrive"+error.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("Error",""+error.getMessage().toString());
                        pd.dismiss();
                    }
                });
        rq.add(jsrqA);
    }
    public void symB(String SympA)
    {
        SymptomB.clear();
        pd.setCancelable(false);
        pd.show();

        String url = "https://diseasepredictor.horizonfoodie.com/PreSymB.php?SymptomA="+SympA;
        JsonObjectRequest jsrqB = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            pd.dismiss();
                            JSONArray jsarray = response.getJSONArray("result");
                            //SymptomB.add(0,"Select");
                            for(int i = 0;i<jsarray.length()+1;i++)
                            {
                                JSONObject jsobj = jsarray.getJSONObject(i);
                                SymptomB.add(i, jsobj.getString("SymptomB"));
                                Collections.sort(SymptomB);
                                String Out = SymptomB.toString().replaceAll("\\[|,|]$"," \n");
                                txtOtherSymps.setText(Out);
                                //Toast.makeText(DiseasePredictor_Activity.this, SymptomB.toString(), Toast.LENGTH_SHORT).show();
                               // Toast.makeText(DiseasePredictor_Activity.this,""+SymptomB.get(i),Toast.LENGTH_LONG).show();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        rq.add(jsrqB);
    }
    public void symC(String SympB)
    {
        SymptomC.clear();
        pd.show();
        String url = "https://diseasepredictor.horizonfoodie.com/PreSymC.php?SymptomB="+SympB;
        JsonObjectRequest jsrqC = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            pd.dismiss();
                            JSONArray jsarray = response.getJSONArray("result");

                            //SymptomC.add(0,"Select");
                            for(int i = 0;i<jsarray.length()+1;i++)
                            {
                                JSONObject jsobj = jsarray.getJSONObject(i);
                                SymptomC.add(i,jsobj.getString("SymptomC"));
                                //Toast.makeText(DiseasePredictor_Activity.this,""+SymptomC.get(i),Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        rq.add(jsrqC);
    }
    public void symD(String SympC)
    {
        SymptomD.clear();
        pd.show();
        String url = "https://diseasepredictor.horizonfoodie.com/PreSymD.php?SymptomC="+SympC;
        JsonObjectRequest jsrqD = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            pd.dismiss();
                            JSONArray jsarray = response.getJSONArray("result");

                            //SymptomD.add(0,"Select");
                            for(int i = 0;i<jsarray.length()+1;i++)
                            {
                                JSONObject jsobj = jsarray.getJSONObject(i);
                                SymptomD.add(i,jsobj.getString("SymptomD"));
                                //Toast.makeText(DiseasePredictor_Activity.this,""+SymptomD.get(i),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        rq.add(jsrqD);
    }
    public void symE(String SympD)
    {
        SymptomE.clear();
        pd.show();
        String url = "https://diseasepredictor.horizonfoodie.com/PreSymE.php?SymptomD="+SympD;
        JsonObjectRequest jsrqE = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            pd.dismiss();
                            JSONArray jsarray = response.getJSONArray("result");

                            //SymptomD.add(0,"Select");
                            for(int i = 0;i<jsarray.length()+1;i++)
                            {
                                JSONObject jsobj = jsarray.getJSONObject(i);
                                SymptomE.add(i,jsobj.getString("SymptomE"));
                                //Toast.makeText(DiseasePredictor_Activity.this,""+SymptomE.get(i),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        rq.add(jsrqE);
    }

    private void SearchDisease(String symptA, String symptB,String symptC,String symptD,String symptE)
    {
        pd.show();
        txtDisease.setText("");
        String url = "https://diseasepredictor.horizonfoodie.com/diseaseDemo.php?SymptA="+symptA+"&SymptB="+symptB+"&SymptC="+symptC+"&SymptD="+symptD+"&SymptE="+symptE;
        JsonObjectRequest jsrqPD = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            pd.dismiss();
                            JSONArray jsarray = response.getJSONArray("result");
                           // Toast.makeText(DiseasePredictor_Activity.this, ""+jsarray.length(), Toast.LENGTH_SHORT).show();
                            for(int i = 0;i<jsarray.length();i++)
                            {
                                JSONObject jsobj = jsarray.getJSONObject(i);
                               // Toast.makeText(DiseasePredictor_Activity.this,""+jsobj.getString("DiseaseName"),Toast.LENGTH_LONG).show();
                                DiseaseList.add(i,jsobj.getString("DiseaseName"));
                                txtDisease.append(jsobj.getString("DiseaseName")+", ");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(DiseasePredictor_Activity.this, "error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        rq.add(jsrqPD);
    }
    /*public void DirectSearch()
    {
        //String url = "https://diseasepredictor.000webhostapp.com/demo1.php";
        JsonObjectRequest jsrqDD = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            pd.dismiss();
                            JSONArray jsarray = response.getJSONArray("result");
                            //Toast.makeText(DiseasePredictor_Activity.this, ""+jsarray.getString(0), Toast.LENGTH_SHORT).show();
                            //SymptomA.add(0,"Select");
                            for(int i = 0;i<jsarray.length()+1;i++)
                            {
                                JSONObject jsobj = jsarray.getJSONObject(i);
                                DirectSearch.add(i,jsobj.getString("DiseaseName"));
                                //Toast.makeText(DiseasePredictor_Activity.this, ""+SymptomA.get(i), Toast.LENGTH_SHORT).show();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Error",""+e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(DiseasePredictor_Activity.this, "Error Data Not Retrive"+error.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("Error",""+error.getMessage().toString());
                        pd.dismiss();
                    }
                });
        rq.add(jsrqDD);
    }*/

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder dlg = new AlertDialog.Builder(DiseasePredictor_Activity.this);
        dlg.setTitle("Confirm to Back !!!");
        dlg.setMessage("Are you sure you want to Go Back???");
        dlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(new Intent(DiseasePredictor_Activity.this,DashBoard_Activity.class));
            }
        });
        dlg.setNegativeButton("NO", null);
        dlg.show();
    }

    public void Disease(View view)
    {
        spinnerDialog = new SpinnerDialog(DiseasePredictor_Activity.this,DirectSearch,"Select Symptoms","Close Items");
        spinnerDialog.setCancellable(false);
        spinnerDialog.setShowKeyboard(false);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                txtDirDiseas.setText(item);

            }
        });


        spinnerDialog.showSpinerDialog();
    }

    public void diseaseInfo(View view)
    {
        Intent i = new Intent(DiseasePredictor_Activity.this,Disease_Info.class);
        i.putExtra("Disease",txtDisease.getText().toString());
        startActivity(i);
    }


    public void switchSymps(View view) {
    }
}