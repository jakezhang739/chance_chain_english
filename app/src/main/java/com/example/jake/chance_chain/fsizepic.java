package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.OnSingleFlingListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class fsizepic extends AppCompatActivity {
    private int pos;
    private ArrayList<String> uriList = new ArrayList<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fsizepic);
        context = getApplicationContext().getApplicationContext();
        PhotoView fpic =  findViewById(R.id.fSpic);
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
        fpic.setOnSingleFlingListener(new OnSingleFlingListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d("onfling",e1.toString()+e2.toString()+String.valueOf(velocityX)+String.valueOf(velocityY));
                if(e1.getAction()==MotionEvent.ACTION_DOWN&&e2.getAction()==MotionEvent.ACTION_UP){
                    if(velocityX>100&&velocityX>velocityY&&velocityX>-velocityY){
                        pos--;
                        if(pos<0){
                        Toast.makeText(fsizepic.this, R.string.fpic, Toast.LENGTH_SHORT).show();
                        pos=0;
                        }
                    else {
                        Picasso.get().load(uriList.get(pos)).into(fpic);
                     }
                     return true;
                    }
                    else if(velocityX<-100&&velocityX<velocityY&&velocityX<-velocityY){
                        pos++;
                        if(pos>=uriList.size()){
                         Toast.makeText(fsizepic.this, R.string.lpic, Toast.LENGTH_SHORT).show();
                         pos=uriList.size()-1;
                        }
                        else {
                        Picasso.get().load(uriList.get(pos)).into(fpic);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
//        fpic.setOnPhotoTapListener(new OnPhotoTapListener() {
//            @Override
//            public void onPhotoTap(ImageView view, float x, float y) {
//                if(x>10){
//                    pos--;
//                    Picasso.get().load(uriList.get(pos)).into(fpic);
//
//                }
//                else if(x<-10){
//                    pos++;
//                    Picasso.get().load(uriList.get(pos)).into(fpic);
//
//                }
//            }
//        });
//        fpic.setOnTouchListener(new OnSwipeTouchListener(){
//            public boolean onSwipeRight() {
//                pos--;
//                Log.d("pos1 ",String.valueOf(pos));
//
//                return true;
//
//            }
//            public boolean onSwipeLeft() {
//                pos++;
//                Log.d("pos2 ",String.valueOf(pos));
//                if(pos>=uriList.size()){
//                    Toast.makeText(fsizepic.this, R.string.lpic, Toast.LENGTH_SHORT).show();
//                    pos=uriList.size()-1;
//                }
//                else {
//                    Picasso.get().load(uriList.get(pos)).into(fpic);
//                }
//                return true;
//            }
//        });
    }

}
