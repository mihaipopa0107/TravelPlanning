package com.mihai.utils;

import com.mihai.core.ReservationViewModel;
import com.mihai.models.Location;
import com.mihai.models.Reservation;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationUtils {
    public static List<ReservationViewModel> getReservations() {
        if(Session.IsAuthenticated()) {
            Long accountId = Session.GetAccountId();
            List<ReservationViewModel> list = new ArrayList<>();

            List<Reservation> reservationList = Select.from(Reservation.class).list().stream()
                    .filter(e -> e.getAccountId() == accountId).collect(Collectors.toList());

            for(int k = 0; k < reservationList.size(); k++) {
                Reservation reservation = reservationList.get(k);
                ReservationViewModel model = new ReservationViewModel();
                model.setAvailableRooms(reservationList.get(k).getAvailableRooms());

                model.setId(reservationList.get(k).getId());
                model.setPlannedAt(reservationList.get(k).getPlannedAt());

                model.setAccountId(reservationList.get(k).getAccountId());
                model.setLocationId(reservationList.get(k).getLocationId());

                List<Location> locations = Select.from(Location.class).list().stream()
                        .filter(e -> e.getId() == reservation.getLocationId()).collect(Collectors.toList());

                model.setLocationLogo(locations.get(0).getLogoImage());
                model.setLocationName(locations.get(0).getName());

                /* add reservation model to list */
                double totalPrice = reservationList.get(k).getAvailableRooms() * locations.get(0).getPrice();
                model.setTotalPrice(totalPrice);
                list.add(model);
            }

            return list;
        }

        return null;
    }
    public static boolean createReservation(int availableRooms) {
        if(Session.IsAuthenticated()) {
            Long accountId = Session.GetAccountId();
            Long locationId = Session.getTravelLocationId();

            List<Reservation> reservationList = Select.from(Reservation.class).list().stream()
                    .filter(e -> e.getAccountId() == accountId && e.getLocationId() == locationId)
                    .collect(Collectors.toList());

            /* update existing reservation */
            if(reservationList.size() == 1) {
                Reservation reservation = reservationList.get(0);
                int newAvailableRooms = reservation.getAvailableRooms() + availableRooms;
                reservation.setAvailableRooms(newAvailableRooms);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c = Calendar.getInstance();

                String date = sdf.format(c.getTime());
                reservation.setPlannedAt(date);
                reservation.save();
            }
            else {
                // no reservation exists
                Reservation reservation = new Reservation();
                reservation.setAvailableRooms(availableRooms);

                reservation.setAccountId(accountId);
                reservation.setLocationId(locationId);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c = Calendar.getInstance();

                /* save new reservation entry */
                String date = sdf.format(c.getTime());
                reservation.setPlannedAt(date);
                reservation.save();
            }
        }

        return false;
    }
}
