package com.mihai.core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.mihai.travelplanning.R;
import com.mihai.travelplanning.ReservationActivity;
import com.mihai.utils.ReservationUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.List;

public class ReservationAdapter extends ArrayAdapter<ReservationViewModel> {
    private List<ReservationViewModel> list;
    private Context context;
    private int resource;

    public ReservationAdapter(@NonNull Context context, int resource, @NonNull List<ReservationViewModel> list) {
        super(context, resource, list);
        this.list = list;

        this.context = context;
        this.resource = resource;
    }

    private Bitmap getLogoImage(String filePath) throws IOException {
        String folder = context.getDataDir().getPath() + "/products/logo/";
        File file = new File(folder + filePath);
        byte[] buffer = Files.readAllBytes(file.toPath());
        Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

        // set account logo image to option menu
        Bitmap src = Bitmap.createScaledBitmap(bm, 192, 192, false);
        return src;
    }

    private void cancelTravel(int position) {
        // cancel specified travel
        ReservationViewModel reservation = list.get(position);
        ReservationUtils.cancelReservation(reservation.getId());

        /* refresh description page */
        Intent reservationListView = new Intent(context, ReservationActivity.class);
        context.startActivity(reservationListView);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(convertView == null) row = inflater.inflate(resource, parent, false);

        try {
            /* try to load and show travel location logo image */
            ImageView travelLogo = row.findViewById(R.id.travelLogo);
            Bitmap bmp = getLogoImage(list.get(position).getLocationLogo());
            travelLogo.setImageBitmap(bmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /* set location name */
        TextView travelName = row.findViewById(R.id.travelName);
        travelName.setText(list.get(position).getLocationName());

        // location country and city
        TextView travelLocation = row.findViewById(R.id.travelLocation);
        travelLocation.setText(list.get(position).getPosition());

        // location total price
        TextView travelPrice = row.findViewById(R.id.travelTotalPrice);
        DecimalFormat df = new DecimalFormat("0.00");
        travelPrice.setText(df.format(list.get(position).getTotalPrice()) + " $");

        /* now, in finally print the created date */
        TextView plannedAt = row.findViewById(R.id.plannedAt);
        plannedAt.setText(list.get(position).getPlannedAt());

        // set cancel button callback
        ImageButton cancel = row.findViewById(R.id.cancelTravel);
        cancel.setOnClickListener(ev -> cancelTravel(position));
        return row;
    }
}
