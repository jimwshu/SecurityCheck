package security.zw.com.securitycheck.widget;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.Constans;
import security.zw.com.securitycheck.LoginActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.bean.EquipmentDetail;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;

public class MyDialog extends Dialog implements View.OnClickListener {
    EditText mOldPwdText;
    EditText mNewPwdText;
    Button mSubmitButton;

    String mOldPwd;
    String mNewPwd;

    Context context;

    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }

    Retrofit mRetrofit;
    Constans.Equipment addCheck;
    Call<String> mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_reset_pwd);
        mOldPwdText = (EditText) findViewById(R.id.old_pwd);
        mNewPwdText = (EditText) findViewById(R.id.new_pwd);
        mSubmitButton = (Button) findViewById(R.id.submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassword();
            }
        });
    }

    private void resetpassword() {

        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.Equipment.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);
        jsonObject.addProperty("newPassword", mNewPwdText.getText().toString());
        jsonObject.addProperty("oldPassword", mOldPwdText.getText().toString());

        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.resetPassword(requestBody);

        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    ToastUtil.Short("修改密码成功");
                    dismiss();

                    if (context instanceof Activity) {
                        SecurityApplication.getInstance().removeUserInfo();
                        SecurityApplication.mUser = null;
                        Activity activity = (Activity) context;
                        activity.finish();
                        LoginActivity.launch(activity);
                    }


                } else {
                    ToastUtil.Short("修改失败，请重试");
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                ToastUtil.Short("修改失败，请重试");
                dismiss();

            }
        });



    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub


    }

}

