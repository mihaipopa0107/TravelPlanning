package com.mihai.core;

public class LocationViewModel {
    private Long id;
    private String name;
    private String logoImage;
    private String country;
    private String city;
    private double price;

    public LocationViewModel() { }

    /* object getters */
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getLogoImage() {
        return logoImage;
    }
    public String getCountry() {
        return country;
    }
    public String getCity() {
        return city;
    }
    public double getPrice() {
        return price;
    }

    /* class setters */
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
