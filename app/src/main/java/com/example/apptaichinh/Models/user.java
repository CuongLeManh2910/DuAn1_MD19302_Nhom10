package com.example.apptaichinh.Models;

public class user {
    private String name;
    private int Sdt;
    private String mk;

    public user(String name, int sdt, String mk) {
        this.name = name;
        Sdt = sdt;
        this.mk = mk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSdt() {
        return Sdt;
    }

    public void setSdt(int sdt) {
        Sdt = sdt;
    }

    public String getMk() {
        return mk;
    }

    public void setMk(String mk) {
        this.mk = mk;
    }
}
