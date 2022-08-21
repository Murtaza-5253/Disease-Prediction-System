package com.igc.diseasepredictor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.ChipGroup;
import com.igc.diseasepredictor.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Corona_Tracer_Activity extends AppCompatActivity
{

    Toolbar toolbar;
    RecyclerView lstCorona;
    List<String> lstQuestions,lstSelChron;
    RequestQueue rq;
    Spinner spnChronic;
    ProgressDialog pd;
    int yes,chron=0;
    ChipGroup group;
    String[] ChDisease,ch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona__tracer);
        toolbar = findViewById(R.id.toolbar);
        lstCorona = findViewById(R.id.lstCorona);
        spnChronic  =findViewById(R.id.spnChronic);
        //group  =findViewById(R.id.group);
        rq = Volley.newRequestQueue(this);
        pd = new ProgressDialog(this);
        lstQuestions = new ArrayList<>();
        lstSelChron = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(Corona_Tracer_Activity.this);
                dlg.setTitle("Confirm to Back !!!");
                dlg.setMessage("Are you sure you want to Exit???");
                dlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(new Intent(Corona_Tracer_Activity.this,DashBoard_Activity.class));
                    }
                });
                dlg.setNegativeButton("NO", null);
                dlg.show();
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(lstCorona.getContext(),DividerItemDecoration.VERTICAL);
        lstCorona.addItemDecoration(dividerItemDecoration);

        lstCorona.setLayoutManager(new LinearLayoutManager(this));
        accQues();






    }
    private void  accChron()
    {
        String url = "https://diseasepredictor.horizonfoodie.com/quesCh.php";
        JsonObjectRequest jsorq = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try{

                    pd.dismiss();
                    JSONArray jsarray2 = response.getJSONArray("chron");
                    if(jsarray2.length()==0)
                    {
                        Toast.makeText(Corona_Tracer_Activity.this, "No Data Availabe", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        ChDisease = new String[jsarray2.length()];
                        for(int j=0;j<jsarray2.length();j++)
                        {
                            JSONObject jsonObject = jsarray2.getJSONObject(j);
                            ChDisease[j]=jsonObject.getString("chronic");
                        }
                        MyAdapter ma = new MyAdapter();
                        spnChronic.setAdapter(ma);
                    }

                }catch (Exception e)
                {
                    Log.i("sds",e.getMessage());
                    Toast.makeText(Corona_Tracer_Activity.this, "Error:- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        });
        rq.add(jsorq);
    }
    private void accQues()
    {
        pd.setTitle("Data Processing...");
        pd.setMessage("Please Wait We Are Processing Data from Database");
        pd.show();
        accChron();
        String url = "https://diseasepredictor.horizonfoodie.com/ques.php";
        JsonObjectRequest jsorq = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try{

                    pd.dismiss();
                    JSONArray jsarray1 = response.getJSONArray("ques");
                    if(jsarray1.length()==0)
                    {
                        Toast.makeText(Corona_Tracer_Activity.this, "No Data Availabe", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        for(int i=0;i<jsarray1.length();i++)
                        {
                            JSONObject jsonObject = jsarray1.getJSONObject(i);
                            lstQuestions.add(i,jsonObject.getString("Ques"));

                        }
                        MyClass MA = new MyClass();
                        lstCorona.setAdapter(MA);
                    }

                }catch (Exception e)
                {
                    Log.i("sds",e.getMessage());
                    Toast.makeText(Corona_Tracer_Activity.this, "Error:- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        });
        rq.add(jsorq);
    }


    public void Points(View view)
    {

    }
    class MyClass extends RecyclerView.Adapter<MyClass.NewHolder>
    {
        @NonNull
        @Override
        public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            View v = li.inflate(R.layout.corona_des,parent,false);

            return new NewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull NewHolder holder, int position)
        {

            holder.txtQuestion.setText(lstQuestions.get(position));
        }

        @Override
        public int getItemCount() {
            return lstQuestions.size();
        }

        class NewHolder extends RecyclerView.ViewHolder
        {
            TextView txtQuestion;
            RadioGroup rgbAns;
            public NewHolder(@NonNull View itemView) {
                super(itemView);
                txtQuestion = itemView.findViewById(R.id.txtQuestion);
                rgbAns = itemView.findViewById(R.id.rgbAns);
                rgbAns.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId)
                    {
                        switch (checkedId)
                        {
                            case R.id.rbYes:
                                yes++;
                                Toast.makeText(Corona_Tracer_Activity.this, "Count: "+yes, Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rbNo:
                                if(yes==0)
                                {
                                    yes=0;
                                }
                                else {
                                    yes--;
                                }

                                Toast.makeText(Corona_Tracer_Activity.this, "Count: "+yes, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return ChDisease.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            convertView = getLayoutInflater().inflate(R.layout.chroinic_layoput,null);
            TextView txtChronic;
            CheckBox chkChron;
            //txtChronic = convertView.findViewById(R.id.txtChronic);
            chkChron = convertView.findViewById(R.id.chkChron);
            //txtChronic.setText(ChDisease[position]);
            chkChron.setText(ChDisease[position]);
            chkChron.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isChecked)
                    {
                        chron++;
                        lstSelChron.add(buttonView.getText().toString());

                        Toast.makeText(Corona_Tracer_Activity.this, ""+lstSelChron.toString(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        chron--;
                        lstSelChron.remove(chron);
                        Toast.makeText(Corona_Tracer_Activity.this, ""+lstSelChron.toString(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(Corona_Tracer_Activity.this, ""+chron, Toast.LENGTH_SHORT).show();


                }
            });
            return convertView;
        }
    }
    public void Trace(View view)
    {
        String Output = lstSelChron.toString().replaceAll("\\[|,|]$","\n\n");
        Toast.makeText(this, Output.trim(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Corona_Tracer_Activity.this, Info.class);
        i.putExtra("Count",yes);
        i.putExtra("Chronic",Output);
        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder dlg = new AlertDialog.Builder(Corona_Tracer_Activity.this);
        dlg.setTitle("Confirm to Back !!!");
        dlg.setMessage("Are you sure you want to Go Back???");
        dlg.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(new Intent(Corona_Tracer_Activity.this,DashBoard_Activity.class));
            }
        });
        dlg.setNegativeButton("NO", null);
        dlg.show();
    }
}