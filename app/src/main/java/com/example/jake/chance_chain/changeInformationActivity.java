package com.example.jake.chance_chain;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.internal.Constants;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;


public class changeInformationActivity extends AppCompatActivity {
    String mCurrentPhotoPath;
    EditText nickNameView,nameView,chanceView,walletView,genderView,careerView,resumeView;
    private int STORAGE_PERMISSION_CODE = 10;
    private int CAMERA_CODE = 3;
    private TransferUtility sTransferUtility;
    Context context;
    Button cameraBtn,picBtn;
    private static final int GALLERY_REQUEST= 5;
    private static final int CAMERA_REQUEST=0;
    Uri galUri;
    String path;
    String pFlag="";
    TransferObserver observer;
    TransferListener listener;
    AppHelper helper=new AppHelper();
    private String uId,nickName,name,chance,wallet,gender,career,resume;
    DynamoDBMapper dynamoDBMapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=getApplication().getApplicationContext();
        sTransferUtility = helper.getTransferUtility(context);
        uId = helper.getCurrentUserName(context);
        Button finishBtn = (Button) findViewById(R.id.finishBtn);
        cameraBtn = (Button) findViewById(R.id.touBtn);
        picBtn = (Button) findViewById(R.id.touBtn2);
        nickNameView = (EditText) findViewById(R.id.inpUid);
        nameView = (EditText) findViewById(R.id.inpName);
        chanceView = (EditText) findViewById(R.id.inpChance);
        walletView = (EditText) findViewById(R.id.inpWallet);
        genderView = (EditText) findViewById(R.id.inpSex);
        careerView = (EditText) findViewById(R.id.inpCareer);
        resumeView = (EditText) findViewById(R.id.inpRes);
        dynamoDBMapper=helper.getMapper(context);
        Log.d("see text",""+nickName);
        finishBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                nickName=nickNameView.getText().toString();
                name=nameView.getText().toString();
                chance=chanceView.getText().toString();
                wallet=walletView.getText().toString();
                gender=genderView.getText().toString();
                career=careerView.getText().toString();
                resume=resumeView.getText().toString();
                UpdateAtr();
                Intent intent = new Intent(changeInformationActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });


        cameraBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View v){
                if (ContextCompat.checkSelfPermission(changeInformationActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)
                            changeInformationActivity.this, Manifest.permission.CAMERA)) {


                    } else {
                        ActivityCompat.requestPermissions((Activity) changeInformationActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                CAMERA_CODE);
                    }

                }
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

            }
        });

        picBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                requstStoragePermission();
                Intent imageIntent = new Intent();
                imageIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                imageIntent.addCategory(Intent.CATEGORY_OPENABLE);
                imageIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                imageIntent.setType("image/*");
                startActivityForResult(imageIntent.createChooser(imageIntent,"选取图片"),GALLERY_REQUEST);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST){
            Log.d("do","shiit");
            try {
                //path = AppHelper.getPath(camUri,context);

                Bundle bundle = data.getExtras();

                Bitmap  file = (Bitmap) bundle.get("data");
                Log.d("camera","uri"+file.toString());
                /*observer =
                        sTransferUtility.upload(helper.BUCKET_NAME,"www",);
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
                    }
                });*/
                //beginUpload(path);
            } catch (Exception e) {
                Toast.makeText(this,
                        "Unable to get the file from the given URI.  See error log for details",
                        Toast.LENGTH_LONG).show();
                Log.d("fck", "Unable to upload file from the given uri", e);
            }

        }
        else if(requestCode == GALLERY_REQUEST){
            galUri=data.getData();
            pFlag="yes";

        }
    }

    private void beginUpload(String filePath) {
        if (filePath == null) {
            Toast.makeText(this, "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);
        TransferObserver observer = sTransferUtility.upload(helper.BUCKET_NAME, helper.getCurrentUserName(context)+".png",
                file);
        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        // observer.setTransferListener(new UploadListener());
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
                            ActivityCompat.requestPermissions(changeInformationActivity.this,
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void UpdateAtr(){
        final UserPoolDO userInf = new UserPoolDO();
        if(pFlag=="yes") {
            try {
                path = helper.getPath(galUri, context);
                File file = new File(path);
                observer =
                        sTransferUtility.upload(helper.BUCKET_NAME, helper.getCurrentUserName(context) + ".png", file);
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
                    }
                });
                userInf.setProfilePic("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+helper.getCurrentUserName(context)+".png");
                //beginUpload(path);
                Log.d("gaodshit", "upload" + file.getName());
            } catch (URISyntaxException e) {
                Toast.makeText(this,
                        "Unable to get the file from the given URI.  See error log for details",
                        Toast.LENGTH_LONG).show();
                Log.d("fck2", "Unable to upload file from the given uri", e);
            }
        }
        userInf.setUserId(uId);
        userInf.setName(name);
        userInf.setCareer(career);
        userInf.setChanceId(chance);
        userInf.setNickName(nickName);
        userInf.setWalletAddress(wallet);
        userInf.setResume(resume);
        userInf.setGender(gender);
        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(userInf);
                // Item saved
            }
        }).start();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
