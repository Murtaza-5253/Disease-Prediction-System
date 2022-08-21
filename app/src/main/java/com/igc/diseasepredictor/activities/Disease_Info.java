package com.igc.diseasepredictor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.igc.diseasepredictor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Disease_Info extends AppCompatActivity
{
    RequestQueue rq;
    ProgressDialog pd;
    List<String> PrecList,MedList;
    TextView txtDis,txtDisPrec,txtDisMed;
    String Disease,Out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease__info);
        rq = Volley.newRequestQueue(this);
        pd = new ProgressDialog(this);
        txtDis = findViewById(R.id.Dis);
        txtDisMed = findViewById(R.id.txtDisMed);
        txtDisPrec = findViewById(R.id.txtDisPrec);
        PrecList = new ArrayList<>();
        MedList = new ArrayList<>();
        Bundle b = getIntent().getExtras();
        Disease = b.getString("Disease");
        Out = Disease.replaceAll("\\[|,|]$","");
        txtDis.setText(Out);
        Toast.makeText(this,Out,Toast.LENGTH_SHORT).show();
        accessPrecautions(Out);
    }
    public void accessPrecautions(String Disease)
    {
        String url = "https://diseasepredictor.horizonfoodie.com/accPrec.php?Disease="+Disease;
        JsonObjectRequest jsrqPD = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            pd.dismiss();
                            JSONArray jsarray = response.getJSONArray("result");
                            Toast.makeText(Disease_Info.this, ""+jsarray.length(), Toast.LENGTH_SHORT).show();
                            for(int i = 0;i<jsarray.length();i++)
                            {
                                JSONObject jsobj = jsarray.getJSONObject(i);
                                // Toast.makeText(DiseasePredictor_Activity.this,""+jsobj.getString("DiseaseName"),Toast.LENGTH_LONG).show();
                                PrecList.add(i,jsobj.getString("Precaution"));
                                MedList.add(i,jsobj.getString("Medication"));

                                Toast.makeText(Disease_Info.this, PrecList.get(i), Toast.LENGTH_SHORT).show();
                                Toast.makeText(Disease_Info.this, MedList.get(i),Toast.LENGTH_SHORT).show();
                            }
                            String OutputPrec = PrecList.toString().replaceAll("\\[|,|]$","\n\n");
                            String OutputMed = MedList.toString().replaceAll("\\[|,|]$","\n\n");
                            txtDisPrec.setText(OutputPrec);
                            txtDisMed.setText(OutputMed);
                        } catch (JSONException e) {
                           // e.printStackTrace();
                            Toast.makeText(Disease_Info.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(Disease_Info.this, "error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        rq.add(jsrqPD);
    }
}