package com.farmers.underground.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.BuildConfig;
import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.models.UserProfile;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.ui.base.BaseActivity;

/**
 * Created by tZpace
 * on 24-Sep-15.
 */
public class TestActivity extends BaseActivity {

    Intent intent;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_test;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.PRODUCTION) {

            if(FarmersApp.showTutorial()){
                intent = new Intent(this, TutorialActivity.class);
            } else /*if (true)*/{
                //todo autologin
                intent = new Intent(this, LoginSignUpActivity.class);
            }

            startActivity(intent);
            finish();
            return;
        }

        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_tutorialAct)
    void startTutorialActivity() {
        intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_loginAct)
    void startLoginActivity() {
        intent = new Intent(this, LoginSignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_mainAct)
    void startMainActivity() {
        MainActivity.start(this);
    }

    @OnClick(R.id.btn_api_call_test)
    void testApiCallsReg() {
        showProgressDialog();
        RetrofitSingleton.getInstance().getUserProfileBySession(new ACallback<UserProfile, ErrorMsg>() {
            @Override
            public void onSuccess(UserProfile result) {
                showToast("OK", Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
            }

            @Override
            public void anyway() {
                hideProgressDialog();
            }
        });
    }

    @OnClick(R.id.btn_wipe_app_prefs)
    void testWipeAppPrefs() {
        FarmersApp.wipeAppPreferences();
    }

    @OnClick(R.id.btn_wipe_usr_prefs)
    void testWipeUsrPrefs() {
        FarmersApp.wipeUsrPreferences();
    }

}
