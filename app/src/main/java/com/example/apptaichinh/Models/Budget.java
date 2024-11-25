package com.example.apptaichinh.Models;

public class Budget {
    int id;
    String tenDanhMuc;
    Double soTienDeRa;
    String ngayKetThuc;

    public Budget() {
    }

    public Budget( String tenDanhMuc, Double soTienDeRa, String ngayKetThuc) {
        this.tenDanhMuc = tenDanhMuc;
        this.soTienDeRa = soTienDeRa;
        this.ngayKetThuc = ngayKetThuc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public Double getSoTienDeRa() {
        return soTienDeRa;
    }

    public void setSoTienDeRa(Double soTienDeRa) {
        this.soTienDeRa = soTienDeRa;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }
}
