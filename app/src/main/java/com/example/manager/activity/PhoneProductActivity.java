package com.example.manager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.manager.R;
import com.example.manager.adapter.PhoneAdapter;
import com.example.manager.model.ProductModel;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClient;
import com.example.manager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PhoneProductActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1, info;
    PhoneAdapter phoneAdapter;
    List<ProductModel> productModelList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_product);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        info = getIntent().getIntExtra("Loai", 1);
        Mapping();
        ActionToolBar();
        getData(page);
        addEventLoad();
        ActionBar a = getSupportActionBar();
        LoadTitle();
    }

    private void LoadTitle() {
        ActionBar actionBarTilte = getSupportActionBar();
        if (info == 1) {
            actionBarTilte.setTitle("Điện thoại");
        } else if (info == 2) {
            actionBarTilte.setTitle("Lap Top");
        }
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == productModelList.size() - 1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                productModelList.add(null);
                phoneAdapter.notifyItemInserted(productModelList.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //remove null
                productModelList.remove(productModelList.size() - 1);
                phoneAdapter.notifyItemRemoved(productModelList.size());
                page = page + 1;
                getData(page);
                phoneAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getAPIProduct(page, info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        product -> {
                            if (product.isSucces()) {
                                if (phoneAdapter == null) {
                                    productModelList = product.getResult();
                                    phoneAdapter = new PhoneAdapter(getApplicationContext(), productModelList);
                                    recyclerView.setAdapter(phoneAdapter);
                                } else {
                                    int pos = productModelList.size() - 1;
                                    int num = product.getResult().size();
                                    for (int i = 0; i < num; i++) {
                                        productModelList.add(product.getResult().get(i));
                                    }
                                    phoneAdapter.notifyItemRangeChanged(pos, num);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Hết hàng", Toast.LENGTH_SHORT).show();
                                isLoading = true;
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối Server", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Mapping() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView_Phone);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        productModelList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}