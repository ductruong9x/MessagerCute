package com.appfree.messengercute;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.appfree.messengercute.adapter.AdapterMessager;
import com.appfree.messengercute.model.TinNhan;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;



public class MyActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,AdapterView.OnItemClickListener {
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private android.app.ActionBar actionBar;
    private SQLiteDatabase db;
    private AdapterMessager adapter;
    private ListView lvTinNhan;
    private ArrayList<TinNhan> listTinNhan=new ArrayList<TinNhan>();
    private String dev_id="108403113";
    private String app_id="108403113";
    private StartAppAd startAppAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.app_color);
            tintManager.setNavigationBarTintResource(R.color.app_color);
        }
        StartAppSDK.init(this,dev_id,app_id);

        setContentView(R.layout.activity_my);
        StartAppAd.showSplash(this, savedInstanceState);
        startAppAd=new StartAppAd(this);
        startAppAd.loadAd();
        actionBar=getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setTitle(Html
                .fromHtml("<font color='#ffffff' size='25'>" + getString(R.string.app_name) + "</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color
                .parseColor("#009688")));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        lvTinNhan=(ListView)findViewById(R.id.lvTinNhan);
        lvTinNhan.setOnItemClickListener(this);
        checkAndCreateDatabase();

    }


    public void checkAndCreateDatabase() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        boolean firstUse = sharedPreferences.getBoolean("firstUse", true);
        if (firstUse) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstUse", true);
            editor.commit();
            saveDataBase();

        } else {
            loadDuLieu();
        }
    }
    public void saveDataBase() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                progressDialog.cancel();
                loadDuLieu();
            }
        };
        new Thread() {
            public void run() {
                try {
                    copyDatabase(getApplicationContext());

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }.start();

    }
    public void loadDuLieu() {
        String pName =this.getClass().getPackage().getName();
        String folder = "/data/data/" + pName + "/databases/";
        String dbPath = folder + "database.db";
        Log.v("duong dan:", dbPath);
        db = SQLiteDatabase.openDatabase(dbPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        String sql = "SELECT * FROM TINNHAN ORDER BY ID DESC";
        Cursor cs = db.rawQuery(sql, null);
        while (cs.moveToNext()) {
            int id = cs.getInt(cs.getColumnIndexOrThrow("ID"));
            String noidung = cs.getString(cs.getColumnIndexOrThrow("NOIDUNG"));
            String loaitinnhan = cs.getString(cs
                    .getColumnIndexOrThrow("LOAITINNHAN"));
            TinNhan tn = new TinNhan(id, noidung, loaitinnhan);
            listTinNhan.add(tn);
        }
        cs.close();
        db.close();
        getActionBar().setTitle(
                Html.fromHtml("<font color='#ffffff' size='25'>" + getString(R.string.all)
                        + "(" + listTinNhan.size() + ")" + "</font>"));
        adapter = new AdapterMessager(this, R.layout.layout_list_tinnhan,
                listTinNhan);


        lvTinNhan.setAdapter(adapter);

    }
    private void copyDatabase(Context context) throws IOException {
        String pName = this.getClass().getPackage().getName();
        String folder = "/data/data/" + pName + "/databases/";
        File CheckDirectory;
        CheckDirectory = new File(folder);
        if (!CheckDirectory.exists()) {
            CheckDirectory.mkdir();
        }
        File file = new File(folder, "database.db");
        if (!file.exists()) {
            Log.w("dsads", " not exist");
            try {
                file.createNewFile();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else {
            Log.w("dsads", "exist");
            file.delete();
            file.createNewFile();
        }
        OutputStream databaseOutputStream = new FileOutputStream(folder
                + "database.db");
        InputStream databaseInputStream;

        byte[] buffer = new byte[1024];
        int length;

        databaseInputStream = context.getResources().openRawResource(
                R.raw.database);
        while ((length = databaseInputStream.read(buffer)) > 0) {
            databaseOutputStream.write(buffer);
        }
        databaseInputStream.close();

        databaseInputStream.close();
        databaseOutputStream.flush();
        databaseOutputStream.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    mDrawerLayout.closeDrawer(Gravity.START);
                } else {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
                if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
                    mDrawerLayout.closeDrawer(Gravity.END);
                }
                break;
            case R.id.moreApp:
                Intent goMoreApp = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri
                                .parse("https://play.google.com/store/apps/developer?id=App+Entertainment"));
                startActivity(goMoreApp);
                break;
            case R.id.danhgia:
                Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri
                        .parse("market://details?id=" + getPackageName()));
                startActivity(goToMarket);
                break;


            case R.id.chiase:
                String smsBody = "Phan mem gui tin nhan chuc mung va ky tu vui. moi nguoi tai ve nhe:\n market://details?id="+getPackageName();
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body", smsBody);
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
                break;
            case R.id.phanhoi:
                AlertDialog.Builder messageError = new AlertDialog.Builder(this);
                messageError
                        .setTitle("Send Report")
                        .setIcon(R.drawable.ic_launcher);
                messageError
                        .setMessage("Thank you send report error to me.");
                messageError.setNeutralButton("Send Report",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                // System.exit(0);
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");
                                i.putExtra(Intent.EXTRA_EMAIL,
                                        new String[] { "ductruongcntt@gmail.com" });
                                i.putExtra(Intent.EXTRA_SUBJECT, "Báo lỗi app "
                                        + getPackageName());
                                i.putExtra(Intent.EXTRA_TEXT, "Mô tả lỗi gặp phải");
                                try {
                                    startActivity(Intent.createChooser(i,
                                            "Send mail..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(MyActivity.this,
                                            "Không có phần mềm Mail hỗ trợ",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                );
                messageError.setPositiveButton("Cancel",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();

                                    }
                                });
                messageError.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void loadDuLieuSuKien(String sukien) {
        String pName = this.getClass().getPackage().getName();
        String folder = "/data/data/" + pName + "/databases/";
        String dbPath = folder + "database.db";
        db = SQLiteDatabase.openDatabase(dbPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        String sql = "SELECT * FROM TINNHAN WHERE LOAITINNHAN=" + "'" + sukien
                + "'" + " ORDER BY ID DESC";
        Cursor cs = db.rawQuery(sql, null);
        while (cs.moveToNext()) {
            int id = cs.getInt(cs.getColumnIndexOrThrow("ID"));
            String noidung = cs.getString(cs.getColumnIndexOrThrow("NOIDUNG"));
            String loaitinnhan = cs.getString(cs
                    .getColumnIndexOrThrow("LOAITINNHAN"));
            TinNhan tn = new TinNhan(id, noidung, loaitinnhan);
            listTinNhan.add(tn);
        }
        cs.close();
        db.close();
        lvTinNhan.setAdapter(new AdapterMessager(this,
                R.layout.layout_list_tinnhan, listTinNhan));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position, String name) {
        String ten = name;
        switch (position) {
            case 0:
                listTinNhan.removeAll(listTinNhan);
               loadDuLieu();

                break;
            case 1:
                listTinNhan.removeAll(listTinNhan);
                loadDuLieuSuKien("NAMMOI");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listTinNhan.size() + ")"
                                + "</font>"));

                break;
            case 2:
                listTinNhan.removeAll(listTinNhan);
                loadDuLieuSuKien("NHAGIAO");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listTinNhan.size() + ")"
                                + "</font>")
                );

                break;
            case 3:
                listTinNhan.removeAll(listTinNhan);
                loadDuLieuSuKien("NOEL");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listTinNhan.size() + ")"
                                + "</font>")
                );

                break;
            case 4:
                listTinNhan.removeAll(listTinNhan);
                loadDuLieuSuKien("TINHYEU");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listTinNhan.size() + ")"
                                + "</font>")
                );

                break;
            case 5:
                listTinNhan.removeAll(listTinNhan);
                loadDuLieuSuKien("NGUNGON");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listTinNhan.size() + ")"
                                + "</font>")
                );

                break;
            case 6:
                listTinNhan.removeAll(listTinNhan);
                loadDuLieuSuKien("SINHNHAT");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listTinNhan.size() + ")"
                                + "</font>")
                );

                break;
            case 7:
                listTinNhan.removeAll(listTinNhan);
                loadDuLieuSuKien("PHUNU");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listTinNhan.size() + ")"
                                + "</font>")
                );
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TinNhan tn;
        tn = (TinNhan) lvTinNhan.getItemAtPosition(position);
        String noidung = tn.getNoidung();
        Intent i = new Intent(MyActivity.this, DetailActivity.class);
        i.putExtra("NOIDUNG", noidung);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        startAppAd.onBackPressed();
        super.onBackPressed();
    }
}
