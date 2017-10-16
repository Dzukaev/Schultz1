package com.dzukaev.admin.schultz1;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Admin on 08.02.2016.
 */
public class Records extends TabActivity  {

    TextView timeLast;
    TextView recordsTv;
    ArrayList<String> times = new ArrayList<>();
    String recordSt = "Пока что нет рекорда";
    private final static String DIR_SD = "/Android/data/ru.schultz.DzukaeVA";
    private final static String FILENAME = "record";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.records);

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        timeLast = (TextView) findViewById(R.id.tvTab1);
        recordsTv = (TextView) findViewById(R.id.tvTab2);

        times = getIntent().getStringArrayListExtra("times");
        putToTv();

      //recordSt = getIntent().getStringExtra("record");
      //  readRecord(FILENAME);


        readRecordSD();

        tabHost.setup();

        TabHost.TabSpec tabSpec;


        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Последнее время");
        tabSpec.setContent(R.id.tvTab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Рекорды");
        tabSpec.setContent(R.id.tvTab2);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tag1");

    }

    public void putToTv(){

        if (times.size() != 0) {

            timeLast.setText("");
            for (int i = 0; i < times.size(); i++) {

                timeLast.append((i + 1) + "." + "  " + times.get(i) + "\n");

            }

        }
        else{

            timeLast.setText("Здесь пока нет результатов");

        }

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        times = savedInstanceState.getStringArrayList("timeLast");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList("timesLast", times);

    }

    private void readRecord(String fileName) {
/**
        try {
            FileReader fRd = new FileReader(FILENAME);
            BufferedReader reader = new BufferedReader(fRd);
            String str;
            StringBuffer buffer = new StringBuffer();

            while ((str = reader.readLine()) != null)
            {
                buffer.append(str + "\n");
            }
            fRd.close();
            recordsTv.setText("#1 - " + buffer.toString());

        }catch (Throwable t)
        {
            Toast.makeText(getApplicationContext(),"Exception: " + t.toString(),Toast.LENGTH_LONG).show();
        }
*/
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
            String str = "";

            while ((str = br.readLine()) != null) {
                recordsTv.append("#1 - " + br.readLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readRecordSD(){

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }

        File sdPath = Environment.getExternalStorageDirectory();

        sdPath = new File(sdPath.getAbsolutePath() + DIR_SD);

        File sdFile = new File(sdPath, FILENAME);
        try {

            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";

            while ((str = br.readLine()) != null) {

                recordsTv.setText(str);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

