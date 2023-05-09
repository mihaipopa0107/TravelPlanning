package com.mihai.utils;
import com.google.gson.Gson;
import com.mihai.core.LocationObjectModel;
import com.mihai.core.LocationViewModel;
import com.mihai.core.TravelSearchFilter;
import com.mihai.models.Location;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocationUtils {
    public static LocationObjectModel getTravelLocationById(Long id) {
        Location location = Location.findById(Location.class, id);

        if(location != null) {
            /* create travel location layer around location db entity */
            LocationObjectModel travel = new LocationObjectModel();
            travel.setName(location.getName());

            travel.setDescription(location.getDescription());
            travel.setAvailableRooms(location.getAvailableRooms());

            travel.setCountry(location.getCountry());
            travel.setCity(location.getCity());

            travel.setPosterImage(location.getPosterImage());
            travel.setObjectives(location.getObjectives());

            travel.setPrice(location.getPrice());
            travel.setId(location.getId());
            return travel;
        }

        /* not exists such entry */
        return null;
    }
    public static LocationObjectModel getTravelLocation(String name) {
        Location location = Select.from(Location.class)
                .where(Condition.prop("Name").eq(name)).first();

        if(location != null) {
            /* create travel location layer around location db entity */
            LocationObjectModel travel = new LocationObjectModel();
            travel.setName(location.getName());

            travel.setDescription(location.getDescription());
            travel.setAvailableRooms(location.getAvailableRooms());

            travel.setCountry(location.getCountry());
            travel.setCity(location.getCity());

            travel.setPosterImage(location.getPosterImage());
            travel.setObjectives(location.getObjectives());

            travel.setPrice(location.getPrice());
            travel.setId(location.getId());
            return travel;
        }

        /* not exists such entry */
        return null;
    }
    public static List<LocationViewModel> getSearchTravels(TravelSearchFilter filter) {
        List<LocationViewModel> travels = new ArrayList<>();

        List<Location> list = Select.from(Location.class).list().stream()
                .filter(e -> e.getAvailableRooms() >= 1).collect(Collectors.toList());

        /* removes travel location that does not match name */
        if (!filter.getName().isEmpty()) {
            list = list.stream().filter(e -> e.getName().contains(filter.getName()))
                    .collect(Collectors.toList());
        }

        /* removes travel location that cannot be filtered by country */
        if (!filter.getCountry().isEmpty()) {
            list = list.stream().filter(e -> e.getCountry().compareTo(filter.getCountry()) == 0)
                    .collect(Collectors.toList());
        }

        // last check if city is matched
        if (!filter.getCity().isEmpty()) {
            list = list.stream().filter(e -> e.getCity().compareTo(filter.getCity()) == 0)
                    .collect(Collectors.toList());
        }

        long counter = list.stream().count();
        int pages = (int) (counter >>> 3);

        /* count location list total pages */
        if ((counter & 0x7) != 0) pages++;

        /* get first page limit 8 */
        List<Location> locations = list.stream()
                .collect(Collectors.toList());

        /* get travel location layer */
        for (Location location : locations) {
            LocationViewModel model = new LocationViewModel();
            model.setId(location.getId());

            model.setName(location.getName());
            model.setCountry(location.getCountry());

            model.setCity(location.getCity());
            model.setLogoImage(location.getLogoImage());

            model.setPrice(location.getPrice());
            travels.add(model);
        }

        /* returns the results */
        return travels;
    }

    public static List<LocationViewModel> getTravelsPage() {
        List<LocationViewModel> travels = new ArrayList<>();

        List<Location> list = Select.from(Location.class).list();
        long counter = list.stream().count();
        int pages = (int) (counter >>> 3);

        /* count location list total pages */
        if ((counter & 0x7) != 0) pages++;

        /* get first page limit 8 */
        List<Location> locations = list.stream().filter(e -> e.getAvailableRooms() >= 1)
                .collect(Collectors.toList());

        /* copy travel location models */
        for (Location location : locations) {
            LocationViewModel model = new LocationViewModel();
            model.setId(location.getId());

            model.setName(location.getName());
            model.setCountry(location.getCountry());

            model.setCity(location.getCity());
            model.setLogoImage(location.getLogoImage());

            model.setPrice(location.getPrice());
            travels.add(model);
        }

        /* returns the results */
        return travels;
    }

    public static String encodeObjectives(String[] objectives) {
        Gson gson = new Gson();
        return gson.toJson(objectives);
    }

    public static String[] decodeObjectives(String objectives) {
        Gson gson = new Gson();
        return gson.fromJson(objectives, String[].class);
    }
}
