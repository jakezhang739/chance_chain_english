package com.example.jake.chance_chain;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class exchange extends AppCompatActivity {
    Double candy,ccb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.chatbar);
        ImageView back = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.title);
        title.setText("Exhange Wallet");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RelativeLayout mainRel = (RelativeLayout) findViewById(R.id.exhange);
        EditText candyText = (EditText) findViewById(R.id.editText4);
        EditText ccbText = (EditText) findViewById(R.id.editText5);
        TextView ccbView = (TextView) findViewById(R.id.textView11);
        TextView ethView = (TextView) findViewById(R.id.textView12);
        ImageView candyImg = (ImageView) findViewById(R.id.imageView14);
        ImageView ccbImg = (ImageView) findViewById(R.id.imageView18);
        ImageView ccbNext = (ImageView) findViewById(R.id.imageView19);
        ImageView ethImg = (ImageView) findViewById(R.id.imageView21);
        Button excBtn = (Button) findViewById(R.id.button8);
        Button topBtn = (Button) findViewById(R.id.button9);
        candyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                candyImg.setVisibility(View.INVISIBLE);
                candyText.setVisibility(View.VISIBLE);
            }
        });
        ccbNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ccbImg.setVisibility(View.INVISIBLE);
                ccbText.setVisibility(View.VISIBLE);
            }
        });
        mainRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                candyImg.setVisibility(View.VISIBLE);
                candyText.setVisibility(View.INVISIBLE);
                ccbText.setVisibility(View.INVISIBLE);
                ccbImg.setVisibility(View.VISIBLE);
                ccbView.setVisibility(View.INVISIBLE);
                ccbNext.setVisibility(View.VISIBLE);
                ethView.setVisibility(View.INVISIBLE);
                ethImg.setVisibility(View.VISIBLE);
            }
        });
        candyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ccbView.setVisibility(View.VISIBLE);
                ccbView.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ccbText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ethView.setVisibility(View.VISIBLE);
                ethView.setText(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
