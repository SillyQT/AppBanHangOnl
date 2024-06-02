package com.example.manager.model;

import java.io.Serializable;

public class ProductModel implements Serializable {
    int MaSPMoi;
    String TenSP;
    String GiaSP;
    String MoTa;
    String HinhAnh;
    int Loai;

    public int getMaSPMoi() {
        return MaSPMoi;
    }

    public void setMaSPMoi(int maSPMoi) {
        MaSPMoi = maSPMoi;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public String getGiaSP() {
        return GiaSP;
    }

    public void setGiaSP(String giaSP) {
        GiaSP = giaSP;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public int getLoai() {
        return Loai;
    }

    public void setLoai(int loai) {
        Loai = loai;
    }
}
