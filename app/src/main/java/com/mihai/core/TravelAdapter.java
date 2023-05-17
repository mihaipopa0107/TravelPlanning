package com.mihai.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mihai.travelplanning.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.List;

public class TravelAdapter extends ArrayAdapter<LocationViewModel> {
    private List<LocationViewModel> list;
    private Context context;
    private int resource;

    public TravelAdapter(@NonNull Context context, int resource, @NonNull List<LocationViewModel> list) {
        super(context, resource, list);
        this.list = list;

        this.context = context;
        this.resource = resource;
    }

    private Bitmap getImage(String filePath) throws IOException {
        String folder = context.getDataDir().getPath() + "/products/logo/";
        File file = new File(folder + filePath);
        byte[] buffer = Files.readAllBytes(file.toPath());
        Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

        // set account logo image to option menu
        Bitmap src = Bitmap.createScaledBitmap(bm, 192, 192, false);
        return src;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(convertView == null) row = inflater.inflate(resource, parent, false);

        TextView textName = (TextView) row.findViewById(R.id.textName);
        TextView textCountryCity = (TextView) row.findViewById(R.id.travelTotalPrice);
        ImageView imageFlag = (ImageView) row.findViewById(R.id.logo);

        try {
            Bitmap bmp = getImage(list.get(position).getLogoImage());
            imageFlag.setImageBitmap(bmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TextView textPrice = (TextView) row.findViewById(R.id.textRating);
        textName.setText(list.get(position).getName());

        DecimalFormat df = new DecimalFormat("0.00");
        textPrice.setText(df.format(list.get(position).getPrice()) + " $");

        String data = list.get(position).getCity() + ", " + list.get(position).getCountry();
        textCountryCity.setText(data);
        return row;
    }
}
