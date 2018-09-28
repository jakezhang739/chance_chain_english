package com.example.jake.chance_chain;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.sangcomz.fishbun.define.Define;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;


public class InformationActivity extends AppCompatActivity {
    DynamoDBMapper dynamoDBMapper;
    private EditText nameView,chanceView,walletView,genderView, careerView, resumeView;
    private ImageView toImage;
    TextView confirmText,tLimit;
    private Uri uri;
    private static int REQUEST_CODE_GALLERY=10;
    private static int REQUEST_CODE_CAMERA=5;
    private static int CROP_PIC_REQUEST_CODE = 15;
    private TransferUtility sTransferUtility;
    private TransferObserver observer;
    private PopupWindow popupWindow;
    private View rootview;
    private static  int STORAGE_PERMISSION_CODE = 1;
    String file_path = Environment.getExternalStorageDirectory()
            + "/recent.jpg";

    Context context;
    private String uId;
    AppHelper helper = new AppHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.changetopbar);
        ImageView back = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        TextView titlteText = (TextView) actionBar.getCustomView().findViewById(R.id.title);
        confirmText = (TextView) actionBar.getCustomView().findViewById(R.id.conf);
        titlteText.setText(R.string.title_activity_information);
        context=getApplication().getApplicationContext();
        sTransferUtility = helper.getTransferUtility(context);

        uId = helper.getCurrentUserName(context);
        Log.d("tagshit",uId);
        nameView =  findViewById(R.id.nameInp);
        chanceView =  findViewById(R.id.chanceInp);
        walletView =  findViewById(R.id.walletInp);
        genderView =  findViewById(R.id.genderInp);
        careerView =  findViewById(R.id.careerInp);
        resumeView =  findViewById(R.id.resumeInp);
        toImage =  findViewById(R.id.touxiang);
        tLimit = findViewById(R.id.limit);
        dynamoDBMapper=helper.getMapper(context);
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.picturepopup,null));
        rootview = LayoutInflater.from(InformationActivity.this).inflate(R.layout.activity_information, null);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        RelativeLayout toulay = findViewById(R.id.touLay);
        toulay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(rootview, Gravity.CENTER_VERTICAL,0,0);
            }
        });
        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                setlimitText(s,20);

            }
        });
        chanceView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setlimitText(s,50);
            }
        });
        walletView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setlimitText(s,100);
            }
        });
        genderView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setlimitText(s,15);
            }
        });
        careerView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                setlimitText(s,100);
            }
        });
        resumeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setlimitText(s,300);
            }
        });
        confirmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmText.setVisibility(View.INVISIBLE);
                new Thread(uploadRun).start();

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                finish();
            }
        });
        getAtr();
        toImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformationActivity.this,singleFpic.class);
                intent.putExtra("pic","https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+uId+".png");
                startActivity(intent);
            }
        });


        Log.d("wtf","uu"+dynamoDBMapper.toString());


    }

    public void dismiss(View v){
        popupWindow.dismiss();
    }

    public void uploadpic(View v){
        requstStoragePermission();
        popupWindow.dismiss();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_CODE_GALLERY);
    }

    public void takePic(View v){
        requstStoragePermission();
        popupWindow.dismiss();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    Runnable uppicRun = new Runnable() {
        @Override
        public void run() {
            try {
                String path = helper.getPath(uri, context);
                File file = new File(path);
                observer =
                        sTransferUtility.upload(helper.BUCKET_NAME, uId + ".png", file);
                observer.setTransferListener(new TransferListener() {
                    @Override
                    public void onError(int id, Exception e) {
                        Log.e("onError", "Error during upload: " + id, e);
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        Log.d("onProgress", String.format("onProgressChanged: %d, total: %d, current: %d",
                                id, bytesTotal, bytesCurrent));
                    }

                    @Override
                    public void onStateChanged(int id, TransferState newState) {
                        Log.d("onState", "onStateChanged: " + id + ", " + newState);
                        if(newState==TransferState.COMPLETED){
                            Message msg = new Message();
                            msg.what=1;
                            handler.sendMessage(msg);
                        }
                    }
                });
            } catch (URISyntaxException e) {
                Log.d("fck2", "Unable to upload file from the given uri", e);
            }

        }
    };


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY){
            if(resultCode == RESULT_OK){
                uri = data.getData();
                CropImage.activity(uri)
                        .start(this);
            }

        }
        else if(requestCode == REQUEST_CODE_CAMERA){
            if(resultCode == RESULT_OK){
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                uri = getImageUri(context,thumbnail);
                CropImage.activity(uri)
                        .start(this);
            }
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
                new Thread(uppicRun).start();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            String str;
            if(msg.obj!=null){
                str=msg.obj.toString();
            }
            else{
                str=null;
            }
            int index = msg.what;
            switch (index){
                case 1:Toast.makeText(context,R.string.upl,Toast.LENGTH_LONG).show();Picasso.get().invalidate("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+uId+".png");Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+uId+".png").memoryPolicy(MemoryPolicy.NO_CACHE).into(toImage);break;
                case 2:nameView.setHint(str);
                break;
                case 3:chanceView.setHint(str);
                break;
                case 4:walletView.setHint(str);
                break;
                case 5:genderView.setHint(str);
                break;
                case 6:careerView.setHint(str);
                break;
                case 7:resumeView.setHint(str);
                break;
                case 8: Log.d("try8 ",str);;Picasso.get().invalidate(str);Picasso.get().load(str).into(toImage);
                break;
            }
        }
    };

    Runnable uploadRun = new Runnable() {
        @Override
        public void run() {
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,uId);

            Log.d("uptag"," "+nameView.getText().toString());
            userPoolDO.setName(nameView.getText().toString());
            userPoolDO.setChanceId(chanceView.getText().toString());
            userPoolDO.setWalletAddress(walletView.getText().toString());
            userPoolDO.setGender(genderView.getText().toString());
            userPoolDO.setCareer(careerView.getText().toString());
            userPoolDO.setResume(resumeView.getText().toString());
            dynamoDBMapper.save(userPoolDO);
            }

    };





    public void getAtr(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,uId);
                Message msgName = Message.obtain();
                Message msgChance = Message.obtain();
                Message msgWallet = Message.obtain();
                Message msgGender = Message.obtain();
                Message msgCareer = Message.obtain();
                Message msgResume = Message.obtain();
                Message msgPro = Message.obtain();
                msgName.what = 2;
                msgChance.what = 3;
                msgWallet.what = 4;
                msgGender.what = 5;
                msgCareer.what = 6;
                msgResume.what = 7;
                msgPro.what=8;



                if(userPoolDO != null) {
                    Log.d("wtf", "sdf3" + userPoolDO.toString());
                    Log.d("wtf", "career" + userPoolDO.getChanceId());
                    msgName.obj=userPoolDO.getName();
                    msgChance.obj=userPoolDO.getChanceId();
                    msgWallet.obj=userPoolDO.getWalletAddress();
                    msgGender.obj = userPoolDO.getGender();
                    msgCareer.obj=userPoolDO.getCareer();
                    msgResume.obj=userPoolDO.getResume();
                    handler.sendMessage(msgName);
                    handler.sendMessage(msgChance);
                    handler.sendMessage(msgWallet);
                    handler.sendMessage(msgGender);
                    handler.sendMessage(msgCareer);
                    handler.sendMessage(msgResume);
                }
                if(userPoolDO.getProfilePic()!=null){
                    msgPro.obj=userPoolDO.getProfilePic();
                    handler.sendMessage(msgPro);

                }
            }
        }).start();

    }

    public void setlimitText(Editable s,int num){
        if(s.toString().trim().length()==0){
            tLimit.setVisibility(View.INVISIBLE);
            confirmText.setVisibility(View.INVISIBLE);
        }
        else {
            tLimit.setText(String.valueOf(s.length()) + "/"+String.valueOf(num));
            if (s.length() > num) {
                tLimit.setTextColor(getColor(R.color.alert));
            } else {
                tLimit.setTextColor(getColor(R.color.white));
            }
            tLimit.setVisibility(View.VISIBLE);
            confirmText.setVisibility(View.VISIBLE);
        }
        if(s.length()>=3) {
            if (s.charAt(s.length() - 1) == '\n' && s.charAt(s.length() - 2) == '\n'&&s.charAt(s.length()-3)=='\n') {
                s.delete(s.length() - 1, s.length() );
            }
        }
    }

    private void requstStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(InformationActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }






}
