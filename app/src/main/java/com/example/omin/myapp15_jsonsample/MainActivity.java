package com.example.omin.myapp15_jsonsample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.button1)).setOnClickListener(this);
        ((Button)findViewById(R.id.button2)).setOnClickListener(this);
        ((Button)findViewById(R.id.button3)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        DataDTO dto = null;
        OpenDataTask task = new OpenDataTask();
        String did = "";
        switch (v.getId()) {
            case R.id.button1: did = "DID00001"; break;
            case R.id.button2: did = "DID00002"; break;
            case R.id.button3: did = "DID00003"; break;
        }

        try {
            dto = task.execute(did).get();
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(dto == null) return;

        ((TextView)findViewById(R.id.dustView)).setText("미세먼지 : "+dto.dust);
        ((TextView)findViewById(R.id.temperView)).setText("온도 : "+dto.temper);
        ((TextView)findViewById(R.id.humView)).setText("습도 : "+dto.humil);
    }

    class OpenDataTask extends AsyncTask<String, Void, DataDTO> {

        @Override
        protected DataDTO doInBackground(String... strings) {
            DataDTO dto = null;
            String urlString = "http://121.137.235.67:8080/json_test.jsp?device_id="+strings[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                byte[] buffer = new byte[1000];
                in.read(buffer);
                String tempString = new String(buffer);

                JSONObject json = new JSONObject(new String(buffer));
                String dust = json.getJSONObject("divice_data").getString("d_num");
                String temper = json.getJSONObject("divice_data").getString("t_num");
                String humil = json.getJSONObject("divice_data").getString("h_num");
                dto = new DataDTO(dust, temper, humil);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return dto;
        }
    }

    class DataDTO {
        public String dust;
        public String temper;
        public String humil;

        public DataDTO(String dust, String temper, String humil) {
            this.dust = dust;
            this.temper = temper;
            this.humil = humil;
        }

        public String toString() {
            return "dust=["+dust+"], temper=["+temper+"], humil=["+humil+"]";
        }
    }
}
