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
import android.widget.ListView;

import com.mihai.core.ReservationAdapter;
import com.mihai.core.ReservationViewModel;
import com.mihai.utils.ReservationUtils;
import com.mihai.utils.Session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        LoadMyReservations();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().compareTo("My travels") == 0) {
            /* go to reservation list view page */
            Intent reservationPage = new Intent(this, ReservationActivity.class);
            startActivity(reservationPage);
        } else {
            /* sign out and go to the account signin page */
            Session.Logout();
            Intent signoutPage = new Intent(this, SigninActivity.class);
            startActivity(signoutPage);
        }

        return super.onOptionsItemSelected(item);
    }

    private void LoadMyReservations() {
        /* running new thread that fetch reservation list of specific user account */
        Thread thread = new Thread(() -> {
            ListView reservations = findViewById(R.id.reservations);
            List<ReservationViewModel> list = ReservationUtils.getReservations();

            // now everything is normal
            ReservationAdapter reservationAdapter = new ReservationAdapter(this, R.layout.reservation_list_layout, list);
            reservations.post(() -> reservations.setAdapter(reservationAdapter));
        });

        thread.setDaemon(true);
        thread.start();
    }
}