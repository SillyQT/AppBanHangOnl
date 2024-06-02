package com.example.manager.model;

public class CategoryModel {
    int MaSP;

    String TenSP;
    String HinhAnh;

    public CategoryModel(String tenSP, String hinhAnh) {
        TenSP = tenSP;
        HinhAnh = hinhAnh;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }


    public int getMaSP() {
        return MaSP;
    }

    public void setMaSP(int maSP) {
        MaSP = maSP;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }
}
