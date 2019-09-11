package com.example.frizty.Model;

public class Products {

    private String productName, description, price, image, catagoty, pid, time, date;

    public Products(){

    }

    public Products(String productName, String description, String price, String image, String catagoty, String pid, String time, String date) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.image = image;
        this.catagoty = catagoty;
        this.pid = pid;
        this.time = time;
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCatagoty() {
        return catagoty;
    }

    public void setCatagoty(String catagoty) {
        this.catagoty = catagoty;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
