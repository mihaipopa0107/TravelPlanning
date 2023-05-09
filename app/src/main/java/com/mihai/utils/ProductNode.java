package com.mihai.utils;

public class ProductNode {
    private String Name;
    private String Description;
    private String LogoImage;
    private String PosterImage;
    private String Country;
    private String City;
    private double Price;
    private String[] Objectives;
    private int AvailableRooms;

    public ProductNode() { }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getDescription() {
        return Description;
    }

    public void setLogoImage(String LogoImage) {
        this.LogoImage = LogoImage;
    }

    public String getLogoImage() {
        return LogoImage;
    }

    public void setPosterImage(String PosterImage) {
        this.PosterImage = PosterImage;
    }

    public String getPosterImage() {
        return  PosterImage;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getCountry() {
        return Country;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getCity() {
        return City;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public double getPrice() {
        return Price;
    }

    public void setObjectives(String[] Objectives) {
        this.Objectives = new String[Objectives.length];

        for(int i = 0; i < Objectives.length; i++)
            this.Objectives[i] = Objectives[i];
    }

    public String[] getObjectives() {
        return Objectives;
    }

    public void setAvailableRooms(int AvailableRooms) {
        this.AvailableRooms = AvailableRooms;
    }

    public int getAvailableRooms() {
        return AvailableRooms;
    }
}
