package com.mihai.models;
import com.orm.SugarRecord;

public class Location extends SugarRecord<Location> {
    private String Name;
    private String Description;
    private String LogoImage;
    private String PosterImage;
    private String Country;
    private String City;
    private double Price;
    private String Objectives;
    private int AvailableRooms;

    public Location() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getLogoImage() {
        return LogoImage;
    }

    public void setLogoImage(String LogoImage) {
        this.LogoImage = LogoImage;
    }

    public String getPosterImage() {
        return PosterImage;
    }

    public void setPosterImage(String PosterImage) {
        this.PosterImage = PosterImage;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public String getObjectives() {
        return Objectives;
    }

    public void setObjectives(String Objectives) {
        this.Objectives = Objectives;
    }

    public int getAvailableRooms() {
        return AvailableRooms;
    }

    public void setAvailableRooms(int availableRooms) {
        AvailableRooms = availableRooms;
    }
}
