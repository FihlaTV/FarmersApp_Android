package com.farmers.underground.ui.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.facebook.login.LoginManager;
import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.models.UserCredentials;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.ValidationUtil;

import java.util.Arrays;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class SignUpFragment extends BaseFragment<LoginSignUpActivity>  {


    @Bind(R.id.etName_FSU)
    protected EditText etName;

    @Bind(R.id.etEmail_FSU)
    protected EditText etEmail;

    @Bind(R.id.etPassword_FSU)
    protected EditText etPassword;

    @Bind(R.id.etConfirm_FSU)
    protected EditText etConfirm;

    private boolean isVisiblePass = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_sign_up;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        hidePasswords();
    }

    private void hidePasswords(){
        etPassword.setTransformationMethod(new PasswordTransformationMethod());
        etConfirm.setTransformationMethod(new PasswordTransformationMethod());
    }


    @SuppressWarnings("unused")
    @OnClick({R.id.ivBack_FSU, R.id.tvLogin_FSU})
    protected void back() {
        getHostActivity().hideSoftKeyboard();
        getHostActivity().getFragmentManager().popBackStack();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnSignUpFB_FSU)
    protected void signUpFB() {
        LoginManager.getInstance().logInWithReadPermissions(getHostActivity(), Arrays.asList("public_profile", "email"));
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.ivShowPass_FSU)
    protected void showHidePass() {
        if (isVisiblePass) {
            etPassword.setTransformationMethod(new PasswordTransformationMethod());
        } else {
            etPassword.setTransformationMethod(new TransformationMethod() {
                @Override
                public CharSequence getTransformation(CharSequence source, View view) {
                    return source;
                }

                @Override
                public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

                }
            });
        }
        isVisiblePass = !isVisiblePass;
        etPassword.setSelection(etPassword.getText().length());
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnSignUp_FSU)
    protected void signUp() {
        String name = etName.getText().toString();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString();
        if (isEmpty(name, "name") || isEmpty(email, "email") || isEmpty(password, "password")) {
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            getHostActivity().showToast("Email is incorrect", Toast.LENGTH_SHORT);
            return;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            getHostActivity().showToast("Password is incorrect", Toast.LENGTH_SHORT);
            return;
        }
        if(!password.equals(etConfirm.getText().toString())){
            getHostActivity().showToast("Password is not confirmed", Toast.LENGTH_SHORT);
            return;
        }
        getHostActivity().showProgressDialog();
        RetrofitSingleton.getInstance().registerViaEmail(name, email, password, new ACallback<SuccessMsg, ErrorMsg>() {
            @Override
            public void onSuccess(SuccessMsg result) {

                FarmersApp.getInstance().saveUserCredentials(new UserCredentials(email,password));
                //getHostActivity().showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);
                getHostActivity().showDialogConfirm();
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                getHostActivity().showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
            }

            @Override
            public void anyway() {
                getHostActivity().hideProgressDialog();
            }
        });
    }

    private boolean isEmpty(String field, String nameField) {
        if (field.length() == 0) {
            getHostActivity().showToast("Please enter " + nameField, Toast.LENGTH_SHORT);
            return true;
        }
        return false;
    }
}