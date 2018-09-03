package com.example.jake.chance_chain;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
public class AuthenticatorActivity extends Activity {
    public static PinpointManager pinpointManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {

                //Make a network call to retrieve the identity ID
                // using IdentityManager. onIdentityId happens UPon success.
                IdentityManager.getDefaultIdentityManager().getUserID(new IdentityHandler() {

                    @Override
                    public void onIdentityId(String s) {

                        //The network call to fetch AWS credentials succeeded, the cached
                        // user ID is available from IdentityManager throughout your app
                        Log.d("MainActivity", "Identity ID is: " + s);
                        Log.d("MainActivity", "Cached Identity ID: " + IdentityManager.getDefaultIdentityManager().getCachedUserID());
                    }

                    @Override
                    public void handleError(Exception e) {
                        Log.e("MainActivity", "Error in retrieving Identity ID: " + e.getMessage());
                    }
                });
                SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(AuthenticatorActivity.this, SignInUI.class);
                signin.login(AuthenticatorActivity.this, HomeActivity.class).execute();
            }
        }).execute();

        PinpointConfiguration config = new PinpointConfiguration(
                AuthenticatorActivity.this,
                AWSMobileClient.getInstance().getCredentialsProvider(),
                AWSMobileClient.getInstance().getConfiguration()
        );
        pinpointManager = new PinpointManager(config);
        pinpointManager.getSessionClient().startSession();
        pinpointManager.getAnalyticsClient().submitEvents();
    }
}
