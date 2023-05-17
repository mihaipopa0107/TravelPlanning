package com.mihai.travelplanning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.mihai.core.CommentAdapter;
import com.mihai.core.CommentViewModel;
import com.mihai.core.LocationObjectModel;
import com.mihai.utils.LocationUtils;
import com.mihai.utils.PostRatingUtils;
import com.mihai.utils.ReservationUtils;
import com.mihai.utils.Session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.List;

public class DescriptionActivity extends AppCompatActivity {
    private LocationObjectModel model = null;
    private TextView totalPrice, maxStock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        UploadTravelLocation();

        totalPrice = findViewById(R.id.totalPrice);
        ImageButton commit = findViewById(R.id.commit);

        commit.setOnClickListener(ev -> {
            /* send post message and rating stars */
            sendPostRating();
        });

        maxStock = findViewById(R.id.maxStock);
        SeekBar seekBar = findViewById(R.id.seekBar);

        Button buy = findViewById(R.id.buy);
        buy.setOnClickListener(e -> {
            /* add new reservation */
            int availableRooms = Integer.parseInt(maxStock.getText().toString());
            ReservationUtils.createReservation(availableRooms);
            goToReservationPage();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                OnProgressChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ImageButton goHome = findViewById(R.id.goHome);
        goHome.setOnClickListener(e -> {
            Session.setTravelLocationId(-1L);
            goToHomePage();
        });
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

    private void sendPostRating() {
        Thread thread = new Thread(() -> {
            EditText message = findViewById(R.id.message);
            RatingBar rating = findViewById(R.id.rating);

            /* now disable user post rating form */
            if(PostRatingUtils.canPostMessage()) {
                TableRow showPostRating = findViewById(R.id.showPostRating);
                TableRow showWarning = findViewById(R.id.showWarning);

                // just refresh main page after creating post rating
                PostRatingUtils.createPostRating(message.getText().toString(), (int) rating.getRating());
                refreshPage();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void OnProgressChanged(int progress) {
        if(model != null) {
            try {
                DecimalFormat df = new DecimalFormat("0.00");
                totalPrice.setText(df.format(model.getPrice() * progress) + " $");
                maxStock.setText(progress + "");
            }
            catch (Exception e)
            {
                e.fillInStackTrace();
            }
        }
    }
    private Bitmap getPosterImage(String filePath) throws IOException {
        String folder = this.getDataDir().getPath() + "/products/poster/";
        File file = new File(folder + filePath);
        byte[] buffer = Files.readAllBytes(file.toPath());
        Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

        // set account logo image to option menu
        Bitmap src = Bitmap.createScaledBitmap(bm, 256, 384, false);
        return src;
    }

    private void UploadTravelLocation() {
        Thread thread = new Thread(() -> {
            Long index = Session.getTravelLocationId();
            model = LocationUtils.getTravelLocationById(index);

            TextView travelName = findViewById(R.id.TravelName);
            TextView travelDescription = findViewById(R.id.TravelDescription);
            travelDescription.setMovementMethod(new ScrollingMovementMethod());

            TextView objectives = findViewById(R.id.objectives);
            objectives.setMovementMethod(new ScrollingMovementMethod());
            TextView rating = findViewById(R.id.stars);

            /* show rating */
            rating.post(() ->  {
                DecimalFormat df = new DecimalFormat("0.00");
                rating.setText(df.format(PostRatingUtils.getCurrentTravelRating()));
            });

            StringBuilder sb = new StringBuilder();
            String[] lines = model.getObjectives();

            /* creates objectives list */
            for(int i = 0; i < lines.length; i++) {
                sb.append("    • " + lines[i]);
                sb.append(System.getProperty("line.separator"));
            }

            objectives.setText(sb.toString());
            TextView travelMore = findViewById(R.id.More);
            ImageView travelPoster = findViewById(R.id.poster);

            travelName.post(() -> travelName.setText(model.getName()));
            travelDescription.post(() -> travelDescription.setText(model.getDescription()));

            travelMore.post(() -> travelMore.setText(model.getCountry() + ", " + model.getCity()));
            TextView totalPrice = findViewById(R.id.totalPrice);
            SeekBar seekBar = findViewById(R.id.seekBar);

            totalPrice.post(() -> {
                DecimalFormat df = new DecimalFormat("0.00");
                totalPrice.setText(df.format(model.getPrice()) + " $");
            });

            seekBar.post(() -> {
                seekBar.setMin(1);
                seekBar.setMax(model.getAvailableRooms());
            });

            travelPoster.post(() -> {
                try {
                    travelPoster.setImageBitmap(getPosterImage(model.getPosterImage()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // disable post rating form
            if(!PostRatingUtils.canPostMessage()) {
                TableRow showPostRating = findViewById(R.id.showPostRating);
                TableRow showWarning = findViewById(R.id.showWarning);

                // activate user warning dialog
                showPostRating.post(() -> showPostRating.setVisibility(View.GONE));
                showWarning.post(() -> showWarning.setVisibility(View.VISIBLE));
            }

            ListView postRatingAll = findViewById(R.id.postRatingAll);
            List<CommentViewModel> posts = PostRatingUtils.getPostRatingsByLocation();

            CommentAdapter commentAdapter = new CommentAdapter(this, R.layout.comment_view_layout, posts);
            postRatingAll.post(() -> postRatingAll.setAdapter(commentAdapter));
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void goToReservationPage() {
        Intent reservationPage = new Intent(this, ReservationActivity.class);
        startActivity(reservationPage);
    }

    private void refreshPage() {
        Intent description = new Intent(this, DescriptionActivity.class);
        startActivity(description);
    }
    private void goToHomePage() {
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
    }
}