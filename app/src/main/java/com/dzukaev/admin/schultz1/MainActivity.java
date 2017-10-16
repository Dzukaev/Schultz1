package com.dzukaev.admin.schultz1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity implements
        AdapterView.OnItemSelectedListener {

    Integer[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25};

    Integer[] dataHoriz = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30
    };
    final boolean[] statusSettings = {false, false};
    boolean[] checkedItems = new boolean[2];

    private final int IDD_CHECK = 3;
    private final static String DIR_SD = "/Android/data/ru.schultz.DzukaeVA";
    private final static String FILENAME = "record";
    private final static String FILE_ONLY_FOR_EVEREST = "DVASystem";
    private final static String STR_ONLY_FOR_EVEREST = "TheBiggestSecret";
    public static int BOOL_ONLY_FOR_EVEREST = 0;
    private SharedPreferences FOR_EVEREST;
    private long RECORD = 99999999;
    private String RECORD_CHANGE = "";
    private String RECORDSt = "";
    boolean once, onceTimer = true;
    boolean twoPlayers = false, changeColor = false, orientation, gameOver = false;
    int nextNums = 1, secret1 = 0, secret2 = 0;
    long time1, time2 = 0;
    protected ArrayList<String> items;
    protected ArrayList<String> selectedItems;
    Random random = new Random();
    private int IDENTICAL_NUMBER;
    private String ID = "IDENTICAL_NUMBER";

    String answer = "", timeToActivity = "";
    ArrayList<String> timesToActivity = new ArrayList<>();
    CharSequence howFast;
    Animation shakeAnimation;
    View linear;
    ImageView secTwo;

    GridView gvMain;
    ArrayAdapter<Integer> adapter;
    ArrayAdapter<Integer> adapterHorizontal;
    ImageButton refresh, settings, records;
    TextView nextId;
    Chronometer mChronometer, chronometerP1, chronometerP2;
    TextView tvText, tvTab2;
    Context mainContext;
    public SharedPreferences sPref;

    String title, text, from, where, attach;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<Integer>(this, R.layout.item, R.id.tvText, data);
        adapterHorizontal = new ArrayAdapter<Integer>(this, R.layout.item, R.id.tvText, dataHoriz){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View root = super.getView(position, convertView, parent);
                TextView textView = (TextView)root.findViewById(R.id.tvText);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeTextFromScreen());
                return root;
            }
        };
        gvMain = (GridView) findViewById(R.id.gvMain);
        gvMain.setAdapter(adapter);
        refresh = (ImageButton) findViewById(R.id.refresh);
        settings = (ImageButton) findViewById(R.id.settings);
        records = (ImageButton) findViewById(R.id.records);
        tvText = (TextView) findViewById(R.id.tvText);
        nextId = (TextView) findViewById(R.id.nextTV);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometerP1 = (Chronometer) findViewById(R.id.chronometerP1);
        chronometerP2 = (Chronometer) findViewById(R.id.chronometerP2);

        tvTab2 = (TextView) findViewById(R.id.tvTab2);
        secTwo = (ImageView) findViewById(R.id.secTwo);
        mainContext = this;
        attach = "";
        IDENTICAL_NUMBER = random.nextInt(1000);

        refreshGvMain();

        chronometerP1.setVisibility(View.INVISIBLE);
        chronometerP2.setVisibility(View.INVISIBLE);
        FOR_EVEREST = getSharedPreferences(FILE_ONLY_FOR_EVEREST, Context.MODE_PRIVATE);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orientation) {
                    butRefreshOptions(adapterHorizontal, v);
                } else {
                    butRefreshOptions(adapter, v);
                }

            }
        });

        records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.records) {

                    secTwo.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(MainActivity.this, Records.class);

                    intent.putStringArrayListExtra("times", timesToActivity);
                    intent.putExtra("record", RECORDSt);
                    startActivity(intent);
                    overridePendingTransition(R.anim.diagonaltranslate,R.anim.alpha);
                }
            }
        });

        //       items.add("Играть вдвоем");
        //     items.add("Облегченная версия");

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.settings) {

                    secTwo.setVisibility(View.INVISIBLE);
                    showDialog(IDD_CHECK);

                }
            }
        });


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            refreshGvMainHorizontal();
            adapterHorizontal.notifyDataSetChanged();
            gvMain.setAdapter(adapterHorizontal);
            orientation = true;

        } else {
            orientation = false;
        }


        gvMain.setOnItemSelectedListener(this);
        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub


                if (orientation) {

                    GridViewOptionsOrientation(dataHoriz, id, position, 31);

                } else {

                    GridViewOptionsOrientation(data, id, position, 26);

                }

            }
        });

        adjustGridView();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {


/**
 if (data[(int) id] == 25) {
 nextId.setText("You Win!");
 }
 else {
 nextId.setText("Следующий элемент: " + (data[(int) id] + 1));
 }
 */
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        nextId.setText("Следующий элемент: 1");
    }


    private void adjustGridView() {

        //     gvMain.setNumColumns(5);
        gvMain.setVerticalSpacing(10);
        gvMain.setHorizontalSpacing(10);
    }

    private void refreshGvMain() {

        Random rand = new Random();
        once = true;
        nextNums = 1;

        for (int i = data.length - 1; i > 0; i--) {
            int j = rand.nextInt(i);
            int temp = data[i];
            data[i] = data[j];
            data[j] = temp;
        }

    }

    private void refreshGvMainHorizontal() {

        Random rand = new Random();
        once = true;
        nextNums = 1;

        for (int i = dataHoriz.length - 1; i > 0; i--) {
            int j = rand.nextInt(i);
            int temp = dataHoriz[i];
            dataHoriz[i] = dataHoriz[j];
            dataHoriz[j] = temp;
        }

        adapterHorizontal.notifyDataSetChanged();
        gvMain.setAdapter(adapterHorizontal);


    }

    private void getRotateOrientation() {

        int rotate = getWindowManager().getDefaultDisplay().getRotation();
        Toast toastR;

        if (statusSettings[0]) {

            switch (rotate) {

                case Surface.ROTATION_90:
                    answer = "Чтобы вернуться в вертикальный режим - снимите галочку";

                    toastR = Toast.makeText(getApplicationContext(),
                            answer, Toast.LENGTH_SHORT);
                    toastR.setGravity(Gravity.CENTER, 0, 0);
                    toastR.show();

                case Surface.ROTATION_270:
                    answer = "Чтобы вернуться в вертикальный режим - снимите галочку";

                    toastR = Toast.makeText(getApplicationContext(),
                            answer, Toast.LENGTH_SHORT);
                    toastR.setGravity(Gravity.CENTER, 0, 0);
                    toastR.show();
            }
        }

    }

    private void timerForTwoPlayers() {


        if (statusSettings[0]) {

            if (nextNums % 2 == 1) {

                chronometerP2.stop();
                time2 += ((SystemClock.elapsedRealtime() - chronometerP2.getBase()) / 1000 % 60);

                chronometerP1.setBase(SystemClock.elapsedRealtime());
                chronometerP1.start();
            } else {

                chronometerP1.stop();
                time1 += ((SystemClock.elapsedRealtime() - chronometerP1.getBase()) / 1000 % 60);

                chronometerP2.setBase(SystemClock.elapsedRealtime());
                chronometerP2.start();

            }
        }

    }

    public void showEnOfGame() {

//        long different = ( (SystemClock.elapsedRealtime() - mChronometer.getBase())/1000%60 ) / 2;


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Время участников:")
                .setMessage("Игрок #1  -  " + time1 + " sec" + '\n' + "Игрок #2  -  " + time2 + " sec")
                .setIcon(R.drawable.btn_radio_on)
                .setCancelable(false)
                .setNegativeButton("ОK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    protected Dialog onCreateDialog(int id) {

        final AlertDialog.Builder builderSet = new AlertDialog.Builder(MainActivity.this);

        final String[] textSettings = {"Играть вдвоем", "Облегченная версия"};


        builderSet.setTitle("Настройки")
                .setMultiChoiceItems(textSettings, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {

                                statusSettings[which] = isChecked;

                                if (statusSettings[0]) {

                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Включен режим игры вдвоем",
                                            Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    getRotateOrientation();
                                } else {
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                                }

                            }

                        })
                .setPositiveButton("Готово",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                time1 = 0;
                                time2 = 0;
                                dialog.cancel();
                            }
                        })

                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();

                            }
                        });


        return builderSet.create();
    }

    protected void showMyDialog() {

        for (int i = 0; i < 2; i++)
            checkedItems[i] = selectedItems.contains(items.get(i));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Настройки");

        builder.setMultiChoiceItems(items.toArray(new String[items.size()]),
                null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {

                        ListView list = ((AlertDialog) dialog).getListView();
                        if (isChecked) {
                            if (which == 0) {
                                for (int i = 0; i < list.getCount(); ++i) {
                                    list.setItemChecked(i, true);
                                    checkedItems[i] = true;
                                }
                                selectedItems.clear();
                                selectedItems.addAll(items);
                            } else
                                selectedItems.add(items.get(which));
                        } else {
                            if (which == 0) {
                                for (int i = 0; i < list.getCount(); ++i) {
                                    list.setItemChecked(i, false);
                                    checkedItems[i] = false;
                                }
                                selectedItems.clear();
                            } else
                                selectedItems.remove(items.get(which));
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        statusSettings[0] = savedInstanceState.getBoolean("twoPlayers");
        statusSettings[1] = savedInstanceState.getBoolean("changeColor");
        timesToActivity = savedInstanceState.getStringArrayList("lastTimes");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("twoPlayers", statusSettings[0]);
        outState.putBoolean("changeColor", statusSettings[1]);
        outState.putStringArrayList("lastTimes", timesToActivity);

    }
/**
    protected void saveResult() {

        String FILE_NAME = "schultz-records";

        ArrayList<String> recTime = new ArrayList<>();

        recTime.add(mChronometer.toString());
        String whatTime = mChronometer.toString();

        try {

            FileInputStream fis = openFileInput(FILE_NAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            recTime = (ArrayList<String>) is.readObject();
            is.close();

        } catch (Exception e) {

            Toast.makeText(this, "Произошла ошибка чтения таблицы рекордов", Toast.LENGTH_LONG).show();

        }

        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            os.writeObject(recTime);
            os.close();
        } catch (Exception e) {
            Toast.makeText(this, "Произошла ошибка записи в таблицу рекордов", Toast.LENGTH_LONG).show();
        }
        Toast toast = Toast.makeText(getApplicationContext(), whatTime, Toast.LENGTH_SHORT);
        toast.show();

        return;


    }
*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            adapter.notifyDataSetChanged();
            refreshGvMainHorizontal();
            adapterHorizontal.notifyDataSetChanged();
            gvMain.setAdapter(adapterHorizontal);
        }

    }

    private void GridViewOptionsOrientation(Integer[] dataToAdapter, long id, int position, int howAll) {

        if (dataToAdapter[(int) id] == 1 && (once)) {

            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            nextId.setText("Следующий элемент: 1");

            once = false;
            gameOver = false;

            if (twoPlayers || !statusSettings[0]) {

                timerForTwoPlayers();

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Режим одиночной игры", Toast.LENGTH_SHORT);
                toast.show();

            }

            BOOL_ONLY_FOR_EVEREST++;

        }

        makeSecretOne(dataToAdapter, id);
        makeSecretTwo(dataToAdapter, id);
        checkErrors(dataToAdapter, id);


        if (dataToAdapter[(int) id] == nextNums) {

            nextNums++;
            nextId.setText("Следующий элемент: "
                    + nextNums);

            timerForTwoPlayers();

            //gvMain.setBackgroundDrawable(getResources().getDrawable(R.drawable.color_table_true));

            if (statusSettings[1]) {
                gvMain.getChildAt(position).setBackgroundColor(Color.GREEN);
            }


        }else{
            shakeAnimation =  AnimationUtils.loadAnimation(this, R.anim.shake);
            gvMain.startAnimation(shakeAnimation);
            linear.startAnimation(shakeAnimation);
        }

// time1 += ((SystemClock.elapsedRealtime() - chronometerP1.getBase()) / 1000 % 60);

        if ((nextNums == howAll) && (!statusSettings[0])) {

            mChronometer.stop();
            chronometerP1.stop();
            chronometerP2.stop();

            howFast = mChronometer.getText();

            nextId.setText("Победа! Ваше время " + howFast);
            makeAPresent();

            if (orientation) {
                timesToActivity.add(mChronometer.getText().toString() + " ( 6x5 )");

            }else {
                timesToActivity.add(mChronometer.getText().toString() + " ( 5x5 )");
            }

            timeToActivity = mChronometer.getText().toString();  // Передача в функцию, затем в другую Activity

            /**
             * ЗАПИСЬ В РЕКОРД
            */


            if ( ( (SystemClock.elapsedRealtime() - mChronometer.getBase()) /1000 % 60 ) < RECORD ){

                RECORD = ( SystemClock.elapsedRealtime() - mChronometer.getBase() ) / 1000 % 60;
                RECORDSt = mChronometer.getText().toString();
                writeRecordSD(RECORDSt);
                Toast.makeText(getApplicationContext(), "Новый рекорд!", Toast.LENGTH_LONG).show();
               // createRecord(FILENAME, RECORDSt);

            }

            mChronometer.setBase(SystemClock.elapsedRealtime());

            nextNums++;

            /**      Toast toastTime = Toast.makeText(getApplicationContext(),
             "Игрок#1 - " + time1, Toast.LENGTH_LONG);
             toastTime.show();
             toastTime = Toast.makeText(getApplicationContext(),
             "Игрок#2 - " + time2, Toast.LENGTH_LONG);
             toastTime.show();
             */

        } else if ((nextNums == howAll) && (statusSettings[0])) {

            mChronometer.stop();
            chronometerP1.stop();
            chronometerP2.stop();

            howFast = mChronometer.getText();

            timesToActivity.add(mChronometer.getText().toString() + " - 2 игрока");


            //     mChronometer.setBase(SystemClock.elapsedRealtime());

            if (time1 < time2) {
                nextId.setText("Победа Игрока#1!");
            } else if (time2 < time1) {
                nextId.setText("Победа Игрока#2!");
            } else {
                nextId.setText("Ничья! Общее время " + howFast);
            }

            nextNums++;

            showEnOfGame();


        }

    }

    private void butRefreshOptions(ArrayAdapter<Integer> adapterSet, View v) {

        if (v.getId() == R.id.refresh) {

            if (orientation) {
                refreshGvMainHorizontal();
            } else {
                refreshGvMain();
            }

            adapterSet.notifyDataSetChanged();
            gvMain.setAdapter(adapterSet);

            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.stop();

            chronometerP1.setBase(SystemClock.elapsedRealtime());
            chronometerP1.stop();
            chronometerP2.setBase(SystemClock.elapsedRealtime());
            chronometerP2.stop();
            time1 = 0;
            time2 = 0;

            secret1 = 0;
            secret2 = 0;
            secTwo.setVisibility(View.INVISIBLE);

            nextId.setText("Следующий элемент: 1");
        }

    }

    public String putToTime(){

        if (timeToActivity != "") {
            return timeToActivity;
        }
        else
            return "Здесь пока нет результатов!";

    }



    public void makeSecretOne(Integer[] dataToAdapter, long id){

        shakeAnimation =  AnimationUtils.loadAnimation(this, R.anim.shake);

        linear = (View) findViewById(R.id.linear);
        if (nextNums == 5){

            if (dataToAdapter[(int) id] == 10){
                secret1++;
            }
            if (secret1 == 7) {

                gvMain.startAnimation(shakeAnimation);
                linear.startAnimation(shakeAnimation);
            }


        }

    }

    public void makeSecretTwo(Integer[] dataToAdapter, long id){

        long endTime = System.currentTimeMillis() + 5 * 1000;

        if (nextNums == 10){

            if (dataToAdapter[(int) id] == 4){
                secret2++;
            }
            if (secret2 == 9){

                secTwo.setVisibility(View.VISIBLE);

            }

        }

    }

    public void checkErrors(Integer[] dataToAdapter, long id){

        if (dataToAdapter[(int)id] - nextNums == 2){

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Вы пропустили", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }


    }

    private void createRecord(String fileName, String newRecord){

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));

            bw.write(newRecord);
            Toast.makeText(this, "Новый рекорд!", Toast.LENGTH_SHORT).show();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readRecord(String toFile) {

        File file = new File(getFilesDir(), FILENAME);
        FileOutputStream outputStream;

        try
        {
            outputStream = openFileOutput(FILENAME, MODE_PRIVATE);
            outputStream.write(toFile.getBytes());
            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }




        /**
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
    }

    private void writeRecordSD(String toFile){


        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            return;
        }
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + DIR_SD);
        sdPath.mkdirs();
        File sdFile = new File(sdPath, FILENAME);
        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            bw.append(toFile);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private long readRecordSD(){

        long changing = 0;
        char c[];

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            return 0;
        }

        File sdPath = Environment.getExternalStorageDirectory();

        sdPath = new File(sdPath.getAbsolutePath() + DIR_SD);

        File sdFile = new File(sdPath, FILENAME);
        try {

            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";

            while ((str = br.readLine()) != null) {

                RECORD_CHANGE = str;

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        c = RECORD_CHANGE.toCharArray();

        for (int i = 0; i < c.length; i++){

            switch (i){

                case 0:
                    changing += Long.parseLong(Character.toString(c[i]))*600;

                case 1:
                    changing += Long.parseLong(Character.toString(c[i]))*60;

                case 3:
                    changing += Long.parseLong(Character.toString(c[i]))*10;

                case 4:
                    changing += Long.parseLong(Character.toString(c[i]));

            }

        }

        Toast.makeText(this, Long.toString(changing), Toast.LENGTH_SHORT).show();

        return changing;
    }

    public float SizeTextFromScreen(){

        float pxWidth, pxHeight;
        int PXHorSize;

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
/**
        Toast.makeText(this,
                "Width: " + metricsB.widthPixels + "\n" + " Height: " + metricsB.heightPixels,
                Toast.LENGTH_SHORT).show();
*/

        pxHeight = (metricsB.heightPixels - settings.getHeight() - 6*10 - 9*16 )/6;
        pxWidth = metricsB.widthPixels;

        return pxHeight;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = FOR_EVEREST.edit();
        editor.putInt(STR_ONLY_FOR_EVEREST, BOOL_ONLY_FOR_EVEREST);
        editor.putInt(ID, IDENTICAL_NUMBER);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (FOR_EVEREST.contains(STR_ONLY_FOR_EVEREST)) {
            // Получаем из настроек
            BOOL_ONLY_FOR_EVEREST = FOR_EVEREST.getInt(STR_ONLY_FOR_EVEREST, 0);
            IDENTICAL_NUMBER = FOR_EVEREST.getInt(ID, 0);
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        }
    }



    private void makeAPresent(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        if (BOOL_ONLY_FOR_EVEREST == 10 || BOOL_ONLY_FOR_EVEREST == 20){

            builder.setTitle("Спасибо, что Вы с нами!")
                    .setMessage("Это Ваша " + BOOL_ONLY_FOR_EVEREST + "-ая игра\nCтатистика медленно, но верно улучшается\n" +
                            "Продолжайте играть, чтобы почувствовать результат тренировок")
                    .setIcon(R.drawable.cat)
                    .setCancelable(false)
                    .setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

        }
        if (BOOL_ONLY_FOR_EVEREST % 30 == 0){
            builder.setTitle("Спасибо, что Вы с нами!")
                    .setMessage("Ого! А Вы не так слабы, как казались.\nЭто уже "+ BOOL_ONLY_FOR_EVEREST + "-ая игра.\n" +
                            "Улучшения в статистике, безусловно, видны.\nПродолжайте играть и станете ГУРУ")
                    .setIcon(R.drawable.cat)
                    .setCancelable(false)
                    .setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        if (BOOL_ONLY_FOR_EVEREST % 7 == 0){
            builder.setTitle("А Вы знали....")
                    .setMessage("Мировой рекорд по скорочтению - 163 333 слова в минуту с полным усвоением прочитанного, " +
                            "принадлежит 16-летней киевлянке Ире Иваченко")
                    .setIcon(R.drawable.doyou)
                    .setCancelable(false)
                    .setNegativeButton("Ничего себе!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        if (BOOL_ONLY_FOR_EVEREST % 9 == 0){
            builder.setTitle("А Вы знали....")
                    .setMessage("Google подсчитал количество всех художественных, публицистических и научных " +
                            "трудов в мире. Оказалось, что общее количество книг на Земле составляет 129 864 880.")
                    .setIcon(R.drawable.doyou)
                    .setCancelable(false)
                    .setNegativeButton("Ничего себе!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        if (BOOL_ONLY_FOR_EVEREST % 11 == 0){
            builder.setTitle("А Вы знали....")
                    .setMessage("Среди самых читаемых книг в мире, первое место, бесспорно, принадлежит Библии. " +
                            "Ее общий тираж — шесть миллиардов экземпляров. На втором месте — цитатник Мао Цзэдуна, " +
                            "а третье место досталось «Властелину колец».")
                    .setIcon(R.drawable.doyou)
                    .setCancelable(false)
                    .setNegativeButton("Ничего себе!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }


    }

}