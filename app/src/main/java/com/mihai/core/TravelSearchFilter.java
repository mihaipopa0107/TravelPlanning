package com.mihai.core;

public class TravelSearchFilter {
    private String name;
    private String country;
    private String city;

    public TravelSearchFilter() {
        name = "";
        country = "";
        city = "";
    }
    public TravelSearchFilter(String name, String country, String city) {
        this.name = name;
        this.country = country;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }
}
