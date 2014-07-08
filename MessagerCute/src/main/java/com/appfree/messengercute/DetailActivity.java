package com.appfree.messengercute;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class DetailActivity extends Activity {

    private android.app.ActionBar actionBar;
    private EditText edNoiDung, edSDT;
    private TextView btnDanhBa, btnGui,btnCopy;
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
        setContentView(R.layout.activity_detail);
        actionBar=getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setTitle(Html
                .fromHtml("<font color='#ffffff' size='25'>" + getString(R.string.title_activity_gui_tin_nhan) + "</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color
                .parseColor("#009688")));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        edNoiDung = (EditText) findViewById(R.id.edNoiDung);
        edSDT = (EditText) findViewById(R.id.edSDT);
        btnDanhBa = (TextView) findViewById(R.id.btnDanhBa);
        btnGui = (TextView) findViewById(R.id.btnSend);
        btnCopy=(TextView)findViewById(R.id.btnCopy);
        kiemTraRong();
        edNoiDung.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                kiemTraRong();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        edSDT.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                kiemTraRong();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        Intent i = getIntent();
        String noidung = i.getStringExtra("NOIDUNG");
        edNoiDung.setText(noidung);
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edSDT.getText().toString();
                String noidung = edNoiDung.getText().toString();
                Uri uri = Uri.parse("smsto:" + phone.toString());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", noidung);
                startActivity(it);
                Toast.makeText(getApplicationContext(), "Send",
                        Toast.LENGTH_LONG).show();
            }
        });
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(edNoiDung.getText().toString());
                    Toast.makeText(getApplication(), "Copy",
                            Toast.LENGTH_LONG).show();
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData
                            .newPlainText("text label", edNoiDung.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplication(), "Copy",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        btnDanhBa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    Toast.makeText(getApplication(),getString(R.string.low_android),
                            Toast.LENGTH_LONG).show();
                    btnDanhBa.setEnabled(false);
                } else {
                    Intent iDanhBa = new Intent(DetailActivity.this, ContactActivity.class);
                    startActivityForResult(iDanhBa, 1);
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1) {
            String phone = data.getStringExtra("PHONE");
            if (phone == null) {

            } else {
                edSDT.setText(phone.toString());
            }
        }
    }


    private void kiemTraRong() {
        if (edNoiDung.getText().toString().equals("")) {
            btnGui.setEnabled(false);
        } else {
            btnGui.setEnabled(true);
        }
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
