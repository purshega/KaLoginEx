package com.tkddlf4209.kaloginex.login_buttons;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tkddlf4209.kaloginex.R;
import com.tkddlf4209.kaloginex.SuccessActivity;

import org.json.JSONObject;

import java.util.Arrays;


/**
 * Created by Home on 02.12.2016.
 */

public class FacebookLogin extends Fragment {

    private LoginButton loginButton;

    private CallbackManager callbackManager;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        System.out.println(" STSRT ---------------------------- ------------------------------ --------------------------");

        View v = inflater.inflate(R.layout.facebook_login, null);


        loginButton = (LoginButton)v.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                System.out.println(" ************************************************************************************************");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Intent intent = new Intent(getActivity(), SuccessActivity.class);
                                startActivity(intent);
                                System.out.println(" ---------------------------- ------------------------------ --------------------------");
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,birthday,email");
                request.setParameters(parameters);
                request.executeAsync();



            }

            @Override
            public void onCancel() {
                System.out.println(" ---------------------------- ------------------------------ --------------------------");
            }

            @Override
            public void onError(FacebookException e) {
                System.out.println(" ---------------------------- ------------------------------ --------------------------");
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
