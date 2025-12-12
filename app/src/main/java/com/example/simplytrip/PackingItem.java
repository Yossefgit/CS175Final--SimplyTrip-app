package com.example.simplytrip;

public class PackingItem {

    private String name;
    private boolean packed;
    private String category;

    public PackingItem(String name, boolean packed, String category) {
        this.name = name;
        this.packed = packed;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public boolean isPacked() {
        return packed;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}