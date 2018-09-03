package com.example.jake.chance_chain;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class fsizepic extends AppCompatActivity {
    private int pos;
    private ArrayList<String> uriList = new ArrayList<>();
    ImageView fpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fsizepic);
        fpic = (ImageView) findViewById(R.id.fSpic);
        uriList = getIntent().getStringArrayListExtra("uri");
        pos = getIntent().getIntExtra("pos",0);
        Log.d("pos ",String.valueOf(pos));
        Picasso.get().load(uriList.get(pos)).into(fpic);
        fpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fpic.setOnTouchListener(new OnSwipeTouchListener(){
            public boolean onSwipeRight() {
                pos--;
                Log.d("pos1 ",String.valueOf(pos));
                if(pos<0){
                    Toast.makeText(fsizepic.this, "这是第一张图", Toast.LENGTH_SHORT).show();
                    pos=0;
                }
                else {
                    Picasso.get().load(uriList.get(pos)).into(fpic);
                }
                return true;

            }
            public boolean onSwipeLeft() {
                pos++;
                Log.d("pos2 ",String.valueOf(pos));
                if(pos>=uriList.size()){
                    Toast.makeText(fsizepic.this, "这是最后一张图", Toast.LENGTH_SHORT).show();
                    pos=uriList.size()-1;
                }
                else {
                    Picasso.get().load(uriList.get(pos)).into(fpic);
                }
                return true;
            }
        });
    }
}
