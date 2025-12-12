package com.example.simplytrip;

public class PackingItem {

    private String name;
    private boolean packed;

    public PackingItem(String name, boolean packed) {
        this.name = name;
        this.packed = packed;
    }

    public String getName() {
        return name;
    }

    public boolean isPacked() {
        return packed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }
}