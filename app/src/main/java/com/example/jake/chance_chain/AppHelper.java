package com.example.jake.chance_chain;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.content.ContentUris;


import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.s3.AmazonS3Client;

import java.net.URISyntaxException;
import java.util.*;

import  static com.amazonaws.regions.Regions.US_EAST_1;


public class AppHelper {
    private  final String TAG = "AppHelper";

    public  final String fbId = "1007699982736676";

    private  AppHelper appHelper;
    private  CognitoUserPool userPool;
    private  CognitoUser user;
    private  AmazonS3Client sS3Client;
    private  TransferUtility sTransferUtility;
    public  final String BUCKET_NAME = "chance-userfiles-mobilehub-653619147";
    private  CognitoUserAttributes userAttributes;
    private  final String userPoolId = "us-east-1:8ea6ccf7-195c-4c3f-a228-dce153794dbd";

    private  final String clientId = "1topa7t6d5nspmikm8tpbdp7bt";

    private  final String clientSecret = "18ijf5nnejosukdfgu2u0208ko63opah0c804ef88thq89pusq58";

    private  final Regions cognitoRegion = US_EAST_1;
    public  final String BUCKET_REGION = "us-east-1";
    private  CognitoUserSession currSession;
    private  CognitoUserDetails userDetails;
    public int number=0;


    private  CognitoCachingCredentialsProvider sCredProvider;
    private  final Map userMap = new HashMap();

    private  CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if (sCredProvider == null) {
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    userPoolId,
                    cognitoRegion);
        }

        return sCredProvider;
    }

    public int increaseNum(){
        number++;
        return number;
    }

    public  String getCurrentUserName(Context context) {

        if (appHelper == null) {
            appHelper = new AppHelper();
        }

            userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret, cognitoRegion);
            user = userPool.getCurrentUser();
            return user.getUserId();

    }



    public  CognitoUserPool getCurrentUserPool(Context context) {

        if (appHelper == null) {
            appHelper = new AppHelper();
        }
        userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret, cognitoRegion);
        return userPool;

    }

    public  AmazonS3Client getS3Client(Context context) {
        if (sS3Client == null) {
            sS3Client = new AmazonS3Client(getCredProvider(context.getApplicationContext()));
            sS3Client.setRegion(Region.getRegion(Regions.fromName(BUCKET_REGION)));
        }
        return sS3Client;
    }

    public TransferUtility getTransferUtility(Context context) {
        if (sTransferUtility == null) {
            sTransferUtility = TransferUtility.builder()
                    .context(context.getApplicationContext())
                    .s3Client(getS3Client(context.getApplicationContext()))
                    .defaultBucket(BUCKET_NAME)
                    .build();
        }

        return sTransferUtility;
    }

    public  DynamoDBMapper getMapper(Context context){
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(context, userPoolId, cognitoRegion);
        AmazonDynamoDBClient dbclient = new AmazonDynamoDBClient(credentialsProvider);
        DynamoDBMapper mapper = DynamoDBMapper.builder()
                .dynamoDBClient(dbclient).awsConfiguration(
                        AWSMobileClient.getInstance().getConfiguration())
                .build();
        return mapper;
    }

    public  AmazonDynamoDBClient getClient(Context context){
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(context, userPoolId, cognitoRegion);
        AmazonDynamoDBClient dbclient = new AmazonDynamoDBClient(credentialsProvider);
        return dbclient;
    }



    public int returnChanceeSize(DynamoDBMapper mapper){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedList<ChanceWithValueDO> result = mapper.scan(ChanceWithValueDO.class,scanExpression);
        return result.size();

    }

    public int returnChatSize(DynamoDBMapper mapper){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedList<ChattingTableDO> result = mapper.scan(ChattingTableDO.class,scanExpression);
        return result.size();
    }



    @SuppressLint("NewApi")
    public  String getPath(Uri uri,Context context) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[] {
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public  boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public  boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public  boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public  String displayTime(String thatTime){
        Date currentTime = Calendar.getInstance().getTime();
        String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
        int hr1,hr2,min1,min2;
        String sameday1,sameday2;
        sameday1=thatTime.substring(0,8);
        sameday2=dateString.substring(0,8);
        hr1=Integer.parseInt(thatTime.substring(8,10));
        hr2=Integer.parseInt(dateString.substring(8,10));
        min1=Integer.parseInt(thatTime.substring(10,12));
        min2=Integer.parseInt(dateString.substring(10,12));
        if(!sameday1.equals(sameday2)){
            Log.d("same ",sameday1 + " " + sameday2);
            return sameday1.substring(0,4)+"/"+sameday1.substring(4,6)+'/'+sameday1.substring(6,8);
        }
        else if(hr1!=hr2){
            Log.d("hr ",hr1+" "+hr2);
            return String.valueOf(hr2-hr1)+"hours ago";
        }
        else if(min1!=min2){
            Log.d("min ", min1+" "+min2);
            return String.valueOf(min2-min1)+"minutes ago";
        }
        else{
            return "刚刚";
        }



    }





}
