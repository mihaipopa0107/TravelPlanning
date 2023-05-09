package com.mihai.travelplanning;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mihai.core.LocationObjectModel;
import com.mihai.core.LocationViewModel;
import com.mihai.core.TravelAdapter;
import com.mihai.core.TravelSearchFilter;
import com.mihai.models.Location;
import com.mihai.utils.LocationUtils;
import com.mihai.utils.Session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TravelSearchFilter filter;
    private ListView travels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TravelsCloudSync();
        filter = null;


        travels = findViewById(R.id.travelsList);
        ImageButton search = findViewById(R.id.search);

        travels.setOnItemClickListener((parent, view, position, id) -> {
            TextView textName = view.findViewById(R.id.textName);
            LocationObjectModel model = LocationUtils.getTravelLocation(textName.getText().toString());

            // save travel location id to app session.
            Session.setTravelLocationId(model.getId());
            goToDescriptionPage();
        });

        search.setOnClickListener(v -> {
            /* search for specific travel locations */
            EditText name = findViewById(R.id.name);

            EditText country = findViewById(R.id.country);
            EditText city = findViewById(R.id.city);

            filter = new TravelSearchFilter();
            if(!name.getText().toString().isEmpty()) filter.setName(name.getText().toString());
            if(!country.getText().toString().isEmpty()) filter.setCountry(country.getText().toString());

            if(!city.getText().toString().isEmpty()) filter.setCity(city.getText().toString());
            SearchTravels(filter);
        });
    }

    private void goToDescriptionPage() {
        Intent description = new Intent(this, DescriptionActivity.class);
        startActivity(description);
    }

    private void SearchTravels(TravelSearchFilter filter) {
        /* start new thread to filter travel location */
        Thread thread = new Thread(() -> {
            List<LocationViewModel> models = LocationUtils.getSearchTravels(filter);
            TravelAdapter travelAdapter = new TravelAdapter(this, R.layout.travel_view_layout, models);
            travels.post(() -> travels.setAdapter(travelAdapter));
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void TravelsCloudSync() {
        /* start new thread to sync server data */
        Thread thread = new Thread(() -> {
            List<LocationViewModel> results = LocationUtils.getTravelsPage();
            TravelAdapter travelAdapter = new TravelAdapter(this, R.layout.travel_view_layout, results);
            travels.post(() -> travels.setAdapter(travelAdapter));
        });

        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!Session.IsAuthenticated()) return false;
        MenuItem account = menu.add(Session.getUsername());
        account.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        try {
            File file = new File(Session.getAvatar());
            byte[] buffer = Files.readAllBytes(file.toPath());
            Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

            // set account logo image to option menu
            Bitmap src = Bitmap.createScaledBitmap(bm, 192, 192, false);
            BitmapDrawable icon = new BitmapDrawable(src);
            account.setIcon(icon);
        } catch (IOException e) {
            String cause = e.getCause().toString();
            Log.w("AccountManager", cause);
        }

        MenuItem travels = menu.add("My travels");
        MenuItem signOutMenu = menu.add("Sign out");
        return true;
    }
}