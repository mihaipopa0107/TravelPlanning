package com.mihai.core;

import com.mihai.utils.LocationUtils;

public class LocationObjectModel {
    private Long id;
    private String Name;
    private String Description;
    private String PosterImage;
    private String Country;
    private String City;
    private double Price;
    private String[] Objectives;
    private int AvailableRooms;

    public LocationObjectModel() { }

    public Long getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getPosterImage() {
        return PosterImage;
    }

    public String getCountry() {
        return Country;
    }

    public String getCity() {
        return City;
    }

    public double getPrice() {
        return Price;
    }

    public String[] getObjectives() {
        return Objectives;
    }

    public int getAvailableRooms() {
        return AvailableRooms;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setPosterImage(String posterImage) {
        PosterImage = posterImage;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public void setObjectives(String objectives) {
        Objectives = LocationUtils.decodeObjectives(objectives);
    }

    public void setAvailableRooms(int availableRooms) {
        AvailableRooms = availableRooms;
    }
}
