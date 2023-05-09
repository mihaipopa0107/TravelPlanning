package com.mihai.core;

public class ReservationViewModel {
    private Long id;
    private int AvailableRooms;
    private String PlannedAt;
    private Long AccountId;
    private Long LocationId;
    private String LocationName;
    private String LocationLogo;
    public double TotalPrice;

    public ReservationViewModel() { }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getLocationLogo() {
        return LocationLogo;
    }

    public void setLocationLogo(String locationLogo) {
        LocationLogo = locationLogo;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAvailableRooms() {
        return AvailableRooms;
    }

    public void setAvailableRooms(int availableRooms) {
        AvailableRooms = availableRooms;
    }

    public String getPlannedAt() {
        return PlannedAt;
    }

    public void setPlannedAt(String plannedAt) {
        PlannedAt = plannedAt;
    }

    public Long getLocationId() {
        return LocationId;
    }

    public void setLocationId(Long locationId) {
        LocationId = locationId;
    }

    public Long getAccountId() {
        return AccountId;
    }

    public void setAccountId(Long accountId) {
        AccountId = accountId;
    }
}
