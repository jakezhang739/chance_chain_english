package com.example.jake.chance_chain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements AWSLoginHandler {

    private static final String TAG = "login";
    private Button loginBtn;
    private ImageView fbBtn;
    private LoginButton fbLog;
    private TextView signupText,forgotText;
    private Context context;
    private final String ATTR_EMAIL = "email";
    private static final String SHARED_PREFERENCE = "SavedValues";
    private static final String PREFERENCE_USER_NAME = "awsUserName";
    private static final String PREFERENCE_USER_EMAIL = "awsUserEmail";
    AWSLoginModel awsLoginModel;
    Boolean Show = false;
    CallbackManager callbackManager;
    private Bundle facebookData;
    private RelativeLayout allRel;
    PrefUtil prefUtil = new PrefUtil(LoginActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        context = getApplicationContext().getApplicationContext();
        ImageView toggleShow = (ImageView) findViewById(R.id.imageView7);
        loginBtn = (Button) findViewById(R.id.buttonLogIn);
        fbBtn = (ImageView) findViewById(R.id.imageView8);
        allRel = (RelativeLayout) findViewById(R.id.show);
        ProgressBar mybar = (ProgressBar) findViewById(R.id.progressBar);
        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {

                                allRel.setVisibility(View.INVISIBLE);
                                mybar.setVisibility(View.VISIBLE);

                                String accessToken = loginResult.getAccessToken().getToken();

                                // save accessToken to SharedPreference
                                prefUtil.saveAccessToken(accessToken);

                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject jsonObject,
                                                                    GraphResponse response) {

                                                // Getting FB User Data
                                                facebookData = getFacebookData(jsonObject);
                                                SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();
                                                editor.putString(PREFERENCE_USER_NAME, facebookData.get("first_name").toString());
                                                editor.apply();
                                                Log.d("fbname1",facebookData.get("first_name").toString());
                                                new Thread(facebookUser).start();

                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,first_name,last_name,email,gender");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }


                            @Override
                            public void onCancel () {
                                Log.d(TAG, "Login attempt cancelled.");
                            }

                            @Override
                            public void onError (FacebookException e){
                                e.printStackTrace();
                                Log.d(TAG, "Login attempt failed.");
                                deleteAccessToken();
                            }
                        }
                );
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,Arrays.asList(
                        "public_profile", "email"));

            }
        });
        signupText = (TextView) findViewById(R.id.textViewUserSignUp);
        forgotText = (TextView) findViewById(R.id.textViewUserForgotPassword);
        awsLoginModel = new AWSLoginModel(this, this);
        EditText password = (EditText) findViewById(R.id.editTextUserPassword);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAction();
            }
        });
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,signUpActivity.class);
                startActivity(intent);
            }
        });
        forgotText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,forgotPassword.class);
                startActivity(intent);
            }
        });
        ImageView gongkai = (ImageView) findViewById(R.id.imageView9);
        toggleShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Show == false){
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    gongkai.setVisibility(View.VISIBLE);
                    toggleShow.setVisibility(View.INVISIBLE);
                    Show = true;
                }

            }
        });

        gongkai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Show == true){
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.length());
                    gongkai.setVisibility(View.INVISIBLE);
                    toggleShow.setVisibility(View.VISIBLE);
                    Show = false;

                }
            }
        });




    }

    private void deleteAccessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    //User logged out
                    prefUtil.clearToken();
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }

    private void LoginAction(){
        EditText login = (EditText) findViewById(R.id.editTextUserId);
        EditText password = (EditText) findViewById(R.id.editTextUserPassword);

        awsLoginModel.signInUser(login.getText().toString(), password.getText().toString());


    }




    @Override
    public void onRegisterSuccess(boolean mustConfirmToComplete) {

    }

    @Override
    public void onRegisterConfirmed() {

    }

    @Override
    public void onSignInSuccess() {
        LoginActivity.this.startActivity(new Intent(LoginActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFailure(int process, Exception exception) {

        exception.printStackTrace();

        Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_LONG).show();

    }

    Runnable facebookUser = new Runnable() {
        @Override
        public void run() {

            SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE,context.MODE_PRIVATE);
            Log.d("fbname",preferences.getString(PREFERENCE_USER_NAME,"fuck"));

//            Log.d("wttttrf",facebookData.getString("first_name"));
            AppHelper helper = new AppHelper();
            DynamoDBMapper mapper = helper.getMapper(context);
             String userid="facebook_Name"+String.valueOf(helper.returnUser(mapper));
            String email = prefUtil.getEmail();
            Log.d("likeomg",prefUtil.getUsername());

            SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();
            //editor.putString(PREFERENCE_USER_EMAIL, email);
            editor.putString(PREFERENCE_USER_NAME, userid);
            editor.apply();

            try{
                UserPoolDO userPoolDO = mapper.load(UserPoolDO.class,userid);
                userPoolDO.setUserId(userid);
                userPoolDO.setMyEmail(email);
                mapper.save(userPoolDO);
            }catch (Exception e) {

                final UserPoolDO userPoolDO = new UserPoolDO();
                userPoolDO.setUserId(userid);
                userPoolDO.setMyEmail(email);
                mapper.save(userPoolDO);
            }
            Message msg = new Message();
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            context.startActivity(intent);
        }
    };

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");
            URL profile_pic;
            try {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name")) {
                bundle.putString("first_name", object.getString("first_name"));
            }
            else{
                bundle.putString("first_name", "first_name");
            }
            if (object.has("last_name")) {
                bundle.putString("last_name", object.getString("last_name"));
            }
            else{
                bundle.putString("last_name", "last_name");
            }
            if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
            }
            else {
                bundle.putString("email", "email");
            }
//            if (object.has("gender")) {
//                bundle.putString("gender", object.getString("gender"));
//            }
//            else{
//                bundle.putString("gender", "gender");
//            }


            prefUtil.saveFacebookUserInfo(object.getString("first_name"),
                    object.getString("last_name"),object.getString("email"), profile_pic.toString());

        } catch (Exception e) {
            Log.d(TAG, "BUNDLE Exception : "+e.toString());
        }

        return bundle;
    }

}
