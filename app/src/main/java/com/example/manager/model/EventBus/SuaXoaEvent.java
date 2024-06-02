package com.example.manager.model.EventBus;

import com.example.manager.model.ProductModel;

public class SuaXoaEvent {
    ProductModel sanPhamMoi;

    public SuaXoaEvent(ProductModel sanPhamMoi) {
        this.sanPhamMoi = sanPhamMoi;
    }

    public ProductModel getSanPhamMoi() {
        return sanPhamMoi;
    }

    public void setSanPhamMoi(ProductModel sanPhamMoi) {
        this.sanPhamMoi = sanPhamMoi;
    }
}
