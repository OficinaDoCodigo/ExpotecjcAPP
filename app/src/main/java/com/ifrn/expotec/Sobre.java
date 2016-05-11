package com.ifrn.expotec;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Sobre extends AppCompatActivity {
    private Toolbar tbSobre;
    private TextView txtF;
    private TextView txtH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        tbSobre = (Toolbar) findViewById(R.id.tbSobre);
        txtF = (TextView) findViewById(R.id.txtF);
        txtH = (TextView) findViewById(R.id.txtH);
        setSupportActionBar(tbSobre);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/fabiano-araujo"));
                startActivity(it);
            }
        });
        txtH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/fabiano-araujo"));
                startActivity(it);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
