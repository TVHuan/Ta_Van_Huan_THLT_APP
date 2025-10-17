package com.example.bai4thapp;

import java.io.Serializable;

public class Task implements Serializable {
    private String ten;
    private String moTa;
    private String han;
    private boolean hoanThanh;

    public Task(String ten, String moTa, String han, boolean hoanThanh) {
        this.ten = ten;
        this.moTa = moTa;
        this.han = han;
        this.hoanThanh = hoanThanh;
    }

    public String getTen() { return ten; }
    public String getMoTa() { return moTa; }
    public String getHan() { return han; }
    public boolean isHoanThanh() { return hoanThanh; }

    public void setTen(String ten) { this.ten = ten; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public void setHan(String han) { this.han = han; }
    public void setHoanThanh(boolean hoanThanh) { this.hoanThanh = hoanThanh; }
}
