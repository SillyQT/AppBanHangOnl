package com.example.manager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manager.R;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClient;
import com.example.manager.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    EditText textEmail, textPass;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Button buttonLogin;
    TextView textViewRegister, textViewForgotPass;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initControll();
    }

    private void initControll() {
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        textViewForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetPassActivity.class);
                startActivity(intent);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginApp();
            }
        });
    }

    private void LoginApp() {
        String str_email = textEmail.getText().toString().trim();
        String str_pass = textPass.getText().toString().trim();

        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập Email!!!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_pass)) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập mật khẩu!!!", Toast.LENGTH_SHORT).show();
        } else {
            Paper.book().write("email", str_email);
            Paper.book().write("pass", str_pass);

            if (user != null)
            {
                // User đã có đăng nhập firebase - 2001210289 - Huỳnh Công Huy - Bài 42: Get token cho app quản lí
                Login(str_email, str_pass);
            }
            else
            {
                // User đã đăng xuất - 2001210289 - Huỳnh Công Huy - Bài 42: Get token cho app quản lí
                firebaseAuth.signInWithEmailAndPassword(str_email, str_pass)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    Login(str_email, str_pass);
                                }
                            }
                        });
            }
        }
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        textEmail = findViewById(R.id.textEmail);
        textPass = findViewById(R.id.textPass);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);
        textViewForgotPass = findViewById(R.id.textForgotPass);
        Paper.init(this);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //read data - 2001210289 - Huỳnh Công Huy - Bài 42: Get token cho app quản lí
        if (Paper.book().read("email") != null && Paper.book().read("pass") != null) {
            textEmail.setText(Paper.book().read("email"));
            textPass.setText(Paper.book().read("pass"));
            if(Paper.book().read("isLogin")!=null){
                boolean flag = Paper.book().read("isLogin");
                if(flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Login(Paper.book().read("email"), Paper.book().read("pass"));
                        }
                    }, 1000);
                }
            }
        }
    }

    private void Login(String email, String pass) {
        compositeDisposable.add(apiBanHang.loginAPI(email, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            if (user.isSucces()) {
                                //save
                                isLogin = true;
                                Paper.book().write("isLogin", isLogin);
                                Utils.user_current = user.getResult().get(0);

                                // Lưu lại thông tin người dùng - 2001210289 - Huỳnh Công Huy - Bài 36

                                Paper.book().write("user", user.getResult().get(0));

                                Toast.makeText(getApplicationContext(), "Đăng nhập thành công!!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra lại tài khoản hoặc mật khẩu!!!", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println("- Lỗi: " + throwable.getMessage());
                            Log.d("Lỗi", "Lỗi: " + throwable.getMessage());
                        }
                ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail() != null && Utils.user_current.getPass() != null) {
            textEmail.setText(Utils.user_current.getEmail());
            textPass.setText(Utils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}