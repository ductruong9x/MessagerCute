package com.appfree.messengercute;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appfree.messengercute.adapter.AdapterDanhBa;
import com.appfree.messengercute.model.DanhBaDetail;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.startapp.android.publish.StartAppAd;

import java.util.ArrayList;

public class ContactActivity extends Activity {
    private android.app.ActionBar actionBar;
    private AdapterDanhBa adapter;
    private ListView lvDanhBa;
    private ArrayList<DanhBaDetail> listDanhBa=new ArrayList<DanhBaDetail>();

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
        setContentView(R.layout.activity_contact);
        StartAppAd.showSlider(this);
        actionBar=getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setTitle(Html
                .fromHtml("<font color='#ffffff' size='25'>" + getString(R.string.title_activity_gui_tin_nhan) + "</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color
                .parseColor("#009688")));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        lvDanhBa=(ListView)findViewById(R.id.lvDanhba);
        loadDanhBa();
        lvDanhBa.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                DanhBaDetail contact;
                contact=(DanhBaDetail)lvDanhBa.getItemAtPosition(pos);
                String phone=contact.getPhone();
                Intent iDanhBa=new Intent();
                iDanhBa.putExtra("PHONE", phone+"");
                setResult(1, iDanhBa);
                finish();
            }
        });

    }

    public void loadDanhBa() {

        Cursor contact = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1",
                null, "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC");
        while (contact.moveToNext()) {
            String name = contact
                    .getString(contact
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = contact
                    .getString(contact
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            DanhBaDetail con=new DanhBaDetail(name, phone);
            listDanhBa.add(con);
        }
        contact.close();
        adapter=new AdapterDanhBa(this, R.layout.danhba, listDanhBa);

        lvDanhBa.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
