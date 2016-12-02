package com.tkddlf4209.kaloginex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends Activity {
    SessionCallback callback;

    private TextView info;
    private LoginButton loginButton;

    private CallbackManager callbackManager;


    private static final String TAG = "OAuthSampleActivity";

    /**
     * client Он связывает информацию.
     */
    private static String OAUTH_CLIENT_ID = "hlBpcpSm7bB0TIiT_L2b";
    private static String OAUTH_CLIENT_SECRET = "x9kdjD2caM";
    private static String OAUTH_CLIENT_NAME = "Войти с Naver ID";

    static private OAuthLoginHandler mOAuthLoginHandler;
    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

    /** UI элемент */
    private TextView mApiResultText;
    private static TextView mOauthAT;
    private static TextView mOauthRT;
    private static TextView mOauthExpires;
    private static TextView mOauthTokenType;
    private static TextView mOAuthState;

    private OAuthLoginButton mOAuthLoginButton;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        OAuthLoginDefine.DEVELOPER_VERSION = true;

        mContext = this;




        mOAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {



//                    String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
//                    String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
//                    long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
//                    String tokenType = mOAuthLoginInstance.getTokenType(mContext);
//                    mOauthAT.setText(accessToken);
//                    mOauthRT.setText(refreshToken);
//                    mOauthExpires.setText(String.valueOf(expiresAt));
//                    mOauthTokenType.setText(tokenType);
//                    mOAuthState.setText(mOAuthLoginInstance.getState(mContext).toString());

                    Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                    String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                    Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            };
        };

        initData();
        initView();

        this.setTitle("OAuthLoginSample Ver." + OAuthLogin.getVersion());

        setContentView(R.layout.activity_main);

        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,birthday,email");
                request.setParameters(parameters);
                request.executeAsync();


                Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });



        /**카카오톡 로그아웃 요청**/
        //한번 로그인이 성공하면 세션 정보가 남아있어서 로그인창이 뜨지 않고 바로 onSuccess()메서드를 호출합니다.
        //테스트 하시기 편하라고 매번 로그아웃 요청을 수행하도록 코드를 넣었습니다 ^^
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
            }
        });

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
                    Log.e("MyUserProfile", userProfile.toString());
                    System.out.println("USER PROPERTIES" + "Email" + userProfile.getRemainingGroupMsgCount()+
                            userProfile.getRemainingInviteCount()+userProfile.getServiceUserId()+userProfile.getClass()
                    );

                    System.out.println(userProfile.toString());

                    Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                    intent.putExtra("UserProfile", userProfile);
                    startActivity(intent);
                    finish();
                }
            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때
            // 어쩔때 실패되는지는 테스트를 안해보았음 ㅜㅜ
        }
    }

    private void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();

        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
		/*
		* Если вы зарегистрированы до августа 2015 года и нечистым приложение обновленная информация возникает не проблема, чтобы поставить знак в существующий набор обратного вызова намерения URL-адрес haejun.
		Или * если вы регистрируетесь после августа 2015 года, который поставил имя пакета обновлений Информация о приложении после этого можно не указывать обратного вызова намерения URL.
		 */
        //mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
    }

    private void initView() {

//        mApiResultText = (TextView) findViewById(R.id.api_result_text);
//
//        mOauthAT = (TextView) findViewById(R.id.oauth_access_token);
//        mOauthRT = (TextView) findViewById(R.id.oauth_refresh_token);
//        mOauthExpires = (TextView) findViewById(R.id.oauth_expires);
//        mOauthTokenType = (TextView) findViewById(R.id.oauth_type);
//        mOAuthState = (TextView) findViewById(R.id.oauth_state);


        mOAuthLoginButton =  (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);


        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);



        updateView();
    }


    private void updateView() {
//        mOauthAT.setText(mOAuthLoginInstance.getAccessToken(mContext));
//        mOauthRT.setText(mOAuthLoginInstance.getRefreshToken(mContext));
//        mOauthExpires.setText(String.valueOf(mOAuthLoginInstance.getExpiresAt(mContext)));
//        mOauthTokenType.setText(mOAuthLoginInstance.getTokenType(mContext));
//        mOAuthState.setText(mOAuthLoginInstance.getState(mContext).toString());
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();

    }

    /**
     * Прокрутка startOAuthLoginActivity () Призываем факторов или могут знать haejumyeon OAuthLoginButton зарегистрирован в аутентификации завершается.
     */


    public void onButtonClick(View v) throws Throwable {


//        switch (v.getId()) {
//            case R.id.buttonOAuth: {
        mOAuthLoginInstance.startOauthLoginActivity(MainActivity.this, mOAuthLoginHandler);
//                new RequestApiTask().execute();
//                break;
//            }
//            case R.id.buttonVerifier: {
//                new RequestApiTask().execute();
//                break;
//            }
//            case R.id.buttonRefresh: {
//                new RefreshTokenTask().execute();
//                break;
//            }
//            case R.id.buttonOAuthLogout: {
//                mOAuthLoginInstance.logout(mContext);
//                updateView();
//                break;
//            }
//            case R.id.buttonOAuthDeleteToken: {
//                new DeleteTokenTask().execute();
//                break;
//            }
//            default:
//                break;
//        }

    }


    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                // Токен от сервера к клиенту, даже если вы не в состоянии удалить маркер удаляется логаут
                // Нет, даже если это не удалось, так как маркер, который может дать дополнительную информацию о клиенте
                Log.d(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
                Log.d(TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
            }

            return null;
        }
        protected void onPostExecute(Void v) {
            updateView();
        }
    }

    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            mApiResultText.setText((String) "");
        }
        @Override
        protected String doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = mOAuthLoginInstance.getAccessToken(mContext);
            return mOAuthLoginInstance.requestApi(mContext, at, url);
        }
        protected void onPostExecute(String content) {
            mApiResultText.setText((String) content);
        }
    }

    private class RefreshTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return mOAuthLoginInstance.refreshAccessToken(mContext);
        }
        protected void onPostExecute(String res) {
            updateView();
        }
    }
}





