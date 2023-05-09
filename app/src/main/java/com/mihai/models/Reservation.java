package com.mihai.models;
import com.orm.SugarRecord;

public class Reservation extends SugarRecord<Reservation> {
    private int AvailableRooms;
    private String PlannedAt;
    private Long AccountId;
    private Long LocationId;

    public Reservation() { }

    public Long getId() {
        return id;
    }

    public int getAvailableRooms() {
        return AvailableRooms;
    }

    public String getPlannedAt() {
        return PlannedAt;
    }

    public Long getAccountId() {
        return AccountId;
    }

    public Long getLocationId() {
        return LocationId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountId(Long accountId) {
        AccountId = accountId;
    }

    public void setLocationId(Long locationId) {
        LocationId = locationId;
    }

    public void setAvailableRooms(int availableRooms) {
        AvailableRooms = availableRooms;
    }

    public void setPlannedAt(String plannedAt) {
        PlannedAt = plannedAt;
    }
}
