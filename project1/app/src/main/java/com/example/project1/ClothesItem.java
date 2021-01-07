package com.example.project1;

public class ClothesItem {
    private int resId; // 옷 이미지의 id
    private String explain;   // 이 옷 이름이나 설명
    private String memo;    // 개인메모

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getMemo() {return memo;}

    public void setMemo(String memo) { this.memo = memo;}
}

