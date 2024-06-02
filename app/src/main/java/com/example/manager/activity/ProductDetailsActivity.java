package com.example.manager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.manager.R;
import com.example.manager.model.CartModel;
import com.example.manager.model.ProductModel;
import com.example.manager.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView textViewName, textViewPrice, textViewDescription;
    Button buttonAddCart;
    ImageView imageViewImg;
    Spinner spinner;
    Toolbar toolbar;
    FrameLayout frameLayout;
    ProductModel productModel;
    NotificationBadge notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        buttonAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCart();
            }
        });
    }

    private void AddCart() {
        if (Utils.CartList.size() > 0) {
            boolean flag = false;
            int num = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Utils.CartList.size(); i++) {
                if (Utils.CartList.get(i).getCartid() == productModel.getMaSPMoi()) {
                    Utils.CartList.get(i).setQuality(num + Utils.CartList.get(i).getQuality());
                    long price = Long.parseLong(productModel.getGiaSP()) * Utils.CartList.get(i).getQuality();
                    Utils.CartList.get(i).setPrice(price);
                    flag = true;
                }
            }
            if (!flag) {
                long price = Long.parseLong(productModel.getGiaSP()) * num;
                CartModel cartModel = new CartModel();
                cartModel.setPrice(price);
                cartModel.setQuality(num);
                cartModel.setCartid(productModel.getMaSPMoi());
                cartModel.setProductName(productModel.getTenSP());
                cartModel.setProductImg(productModel.getHinhAnh());
                Utils.CartList.add(cartModel);
            }
        } else {
            int num = Integer.parseInt(spinner.getSelectedItem().toString());
            long price = Long.parseLong(productModel.getGiaSP()) * num;
            CartModel cartModel = new CartModel();
            cartModel.setPrice(price);
            cartModel.setQuality(num);
            cartModel.setCartid(productModel.getMaSPMoi());
            cartModel.setProductName(productModel.getTenSP());
            cartModel.setProductImg(productModel.getHinhAnh());
            Utils.CartList.add(cartModel);
        }
        int totalItem = 0;
        for (int i = 0; i < Utils.CartList.size(); i++) {
            totalItem = totalItem + Utils.CartList.get(i).getQuality();
        }
        notificationBadge.setText(String.valueOf(totalItem));
    }


    private void initData() {
        productModel = (ProductModel) getIntent().getSerializableExtra("data");
        textViewName.setText(productModel.getTenSP());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        textViewPrice.setText("Giá: " + decimalFormat.format(Double.parseDouble(productModel.getGiaSP())) + " VND");
        textViewDescription.setText(productModel.getMoTa());

        if (productModel.getHinhAnh().contains("http")) {
            Glide.with(getApplicationContext()).load(productModel.getHinhAnh()).into(imageViewImg);
        }
        else
        {
            String Img = Utils.BASE_URL + "images/" + productModel.getHinhAnh();
            Glide.with(getApplicationContext()).load(Img).into(imageViewImg);
        }

        // Glide.with(getApplicationContext()).load(productModel.getHinhAnh()).into(imageViewImg);

        Integer[] num = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, num);
        spinner.setAdapter(arrayAdapter);
    }

    private void initView() {
        textViewName = findViewById(R.id.txbName);
        textViewPrice = findViewById(R.id.txbPrice);
        textViewDescription = findViewById(R.id.txbDetailsDescription);
        buttonAddCart = findViewById(R.id.btnAddCart);
        spinner = findViewById(R.id.spinner);
        imageViewImg = findViewById(R.id.item_productdetails_img);
        toolbar = findViewById(R.id.toolbarDetails);
        notificationBadge = (NotificationBadge) findViewById(R.id.menu_quanlity);
        frameLayout = findViewById(R.id.frameCart);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });

        if (Utils.CartList != null) {
            int totalItem = 0;
            for (int i = 0; i < Utils.CartList.size(); i++) {
                totalItem = totalItem + Utils.CartList.get(i).getQuality();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }
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
}