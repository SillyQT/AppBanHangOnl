package com.example.manager.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manager.R;
import com.example.manager.model.CreateOrder;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClient;
import com.example.manager.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PayActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView textViewTotalPrice, textViewMobile, textViewEmail;
    EditText editTextAddress;
    AppCompatButton buttonPay, btn_zalopay;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long total;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        //zalo
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2554, Environment.SANDBOX);
        initView();
        countItem();
        initControll();
    }

    private void countItem() {
        totalItem = 0;
        for (int i = 0; i < Utils.CartList.size(); i++) {
            totalItem = totalItem + Utils.CartList.get(i).getQuality();
        }
    }

    private void initControll() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        total = getIntent().getLongExtra("totalprice", 0);
        textViewTotalPrice.setText(decimalFormat.format(total));
        textViewEmail.setText(Utils.user_current.getEmail());
        textViewMobile.setText(Utils.user_current.getMobile());

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_address = editTextAddress.getText().toString().trim();
                if (TextUtils.isEmpty(str_address)) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập địa chỉ giao hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    //post data
                    String str_email = Utils.user_current.getEmail();
                    String str_phone = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();
                    Log.d("test", new Gson().toJson(Utils.CartList));
                    compositeDisposable.add(apiBanHang.billAPI(str_email, String.valueOf(total), str_phone, str_address, totalItem, id, new Gson().toJson(Utils.CartList))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    user -> {
                                        Toast.makeText(getApplicationContext(), "Đặt hàng thành công!!!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });

        btn_zalopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_address = editTextAddress.getText().toString().trim();
                if (TextUtils.isEmpty(str_address)) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập địa chỉ giao hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    //post data
                    String str_email = Utils.user_current.getEmail();
                    String str_phone = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();
                    Log.d("test", new Gson().toJson(Utils.CartList));
                    compositeDisposable.add(apiBanHang.billAPI(str_email, String.valueOf(total), str_phone, str_address, totalItem, id, new Gson().toJson(Utils.CartList))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    user -> {
                                        requestZaloPay();
//                                        Toast.makeText(getApplicationContext(), "Đặt hàng thành " +
//                                                "công!!! " + user.getMessage(), Toast.LENGTH_SHORT).show();
                                        Utils.CartList.clear();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void requestZaloPay() {
        CreateOrder orderApi = new CreateOrder();

        try {
            JSONObject data = orderApi.createOrder(String.valueOf(total));
//            Log.d("Amount", txtAmount.getText().toString());
//            lblZpTransToken.setVisibility(View.VISIBLE);
            String code = data.getString("return_code");
//            Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();

            if (code.equals("1")) {
//                lblZpTransToken.setText("zptranstoken");
//                txtToken.setText(data.getString("zp_trans_token"));
//                IsDone();
                String token = data.getString("zp_trans_token");

                ZaloPaySDK.getInstance().payOrder(PayActivity.this, token, "demozpdk://app",
                        new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(PayActivity.this)
                                                .setTitle("Payment Success")
                                                .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Toast.makeText(getApplicationContext(), "Đặt hàng thành " +
                                                                "công!!! ", Toast.LENGTH_SHORT).show();
                                                    }
                                                })

                                                .setNegativeButton("Cancel", null).show();
                                    }

                                });
                            }

                            @Override
                            public void onPaymentCanceled(String zpTransToken, String s1) {
                                new AlertDialog.Builder(PayActivity.this)
                                        .setTitle("User Cancel Payment")
                                        .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setNegativeButton("Cancel", null).show();
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String s1) {
                                new AlertDialog.Builder(PayActivity.this)
                                        .setTitle("Payment Fail")
                                        .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setNegativeButton("Cancel", null).show();
                            }
                        });

                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolbar);
        textViewTotalPrice = findViewById(R.id.textToltalPrice);
        textViewMobile = findViewById(R.id.textMobile);
        textViewEmail = findViewById(R.id.textEmail);
        editTextAddress = findViewById(R.id.edittextAddress);
        buttonPay = findViewById(R.id.buttonPay);
        btn_zalopay = findViewById(R.id.btn_zalopay);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}