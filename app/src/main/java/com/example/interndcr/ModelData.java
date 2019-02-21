package com.example.interndcr;

public class ModelData {
    private String product_group, literature, sample, gift;

    public String getgift(){
        return gift;
    }

    public void setgift(String gift){
        this.gift = gift;
    }

    public String getproduct_group() {
        return product_group;
    }

    public void setproduct_group(String product_group) {
        this.product_group = product_group;
    }

    public String getliterature() {
        return literature;
    }

    public void setliterature(String literature) {
        this.literature =literature;
    }

    public String getsample() {
        return sample;
    }

    public void setsample(String sample) {
        this.sample = sample;
    }
}
