package com.exemple;


public class Product {
    private String url;
    private String imageUrl;
    private String price;
    private String name;
    private String description;

    public Product(String url, String imageUrl, String price, String name, String description) {
        this.url = url;
        this.imageUrl = imageUrl;
        this.price = price.split(" ")[0];
        this.name = name;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Url товара: " + url + "\n" +
                "Url картинки: " + imageUrl + "\n" +
                "Цена: " + price + "\n" +
                "Название: " + name + "\n" +
                "Описание: " + description + "\n";
    }
}
