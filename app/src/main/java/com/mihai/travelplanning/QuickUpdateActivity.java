package com.mihai.travelplanning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;;
import com.google.gson.Gson;
import com.mihai.models.Account;
import com.mihai.models.Location;
import com.mihai.utils.LocationUtils;
import com.mihai.utils.ProductNode;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QuickUpdateActivity extends AppCompatActivity {
    private ProgressBar bar;
    private TextView percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_update);
        bar = (ProgressBar) findViewById(R.id.bar);
        percent = (TextView)findViewById(R.id.percent);
        setWorkerServiceAsync();
    }

    private void setWorkerServiceAsync() {
        Thread thread = new Thread(() -> {
            try {
                ByteArrayOutputStream ms = new ByteArrayOutputStream();
                InputStream inputStream = this.getResources().openRawResource(R.raw.travels);

                // large buffer size of
                byte[] buffer = new byte[65536];
                int count = 0;

                while((count = inputStream.read(buffer, 0, buffer.length)) != -1)
                    ms.write(buffer, 0, count);

                // sync product list from server
                Gson gson = new Gson();
                String str = new String(ms.toByteArray());

                ProductNode[] productNodes = gson.fromJson(str, ProductNode[].class);
                boolean[] exists = new boolean[productNodes.length];
                for(int i = 0; i < exists.length; i++) exists[i] = true;

                String folder = getDataDir().getPath() + "/products/";
                File fileFolder = new File(folder);
                int steps = 0, total = 0;

                // create product base directory if not exists
                if(!fileFolder.exists())
                    fileFolder.mkdir();

                // check up first
                for(int j = 0; j < productNodes.length; j++) {
                    List<Location> list = Select.from(Location.class).where(Condition.prop("Name").eq(productNodes[j].getName())).list();

                    if(list.isEmpty())
                    {
                        exists[j] = false;
                        total++;
                    }
                }

                // prepares progress bar to cloud sync
                bar.setProgress(0, true);
                bar.setMax(total);

                for(int k = 0; k < productNodes.length; k++) {
                    try {
                        if (!exists[k]) {
                            Location location = new Location();
                            location.setName(productNodes[k].getName());

                            location.setDescription(productNodes[k].getDescription());
                            location.setAvailableRooms(productNodes[k].getAvailableRooms());

                            location.setCountry(productNodes[k].getCountry());
                            location.setCity(productNodes[k].getCity());

                            location.setPrice(productNodes[k].getPrice());
                            String objectives = LocationUtils.encodeObjectives(productNodes[k].getObjectives());
                            location.setObjectives(objectives);

                            URL url = new URL(productNodes[k].getLogoImage());
                            String filePath = url.getPath().toString();

                            int index = filePath.lastIndexOf("/");
                            filePath = filePath.substring(index + 1);
                            location.setLogoImage(filePath);

                            InputStream in = new BufferedInputStream(url.openStream());
                            ByteArrayOutputStream out = new ByteArrayOutputStream();

                            buffer = new byte[65536];
                            count = 0;

                            // download logo image
                            while ((count = in.read(buffer, 0, buffer.length)) != -1)
                                out.write(buffer, 0, count);

                            // create logo directory if not exists
                            File logoFolder = new File(folder + "/logo/");
                            if (!logoFolder.exists()) logoFolder.mkdir();

                            // save logo image at disk
                            Path fileLogoPath = Paths.get(folder + "/logo/" + filePath);
                            Files.write(fileLogoPath.toAbsolutePath(), out.toByteArray());

                            /* close streams */
                            in.close();
                            out.close();

                            /* prepares poster image */
                            url = new URL(productNodes[k].getPosterImage());
                            filePath = url.getPath().toString();

                            // extracts image file name
                            index = filePath.lastIndexOf("/");
                            filePath = filePath.substring(index + 1);
                            location.setPosterImage(filePath);

                            in = new BufferedInputStream(url.openStream());
                            out = new ByteArrayOutputStream();

                            buffer = new byte[65536];
                            count = 0;

                            // download poster image
                            while ((count = in.read(buffer, 0, buffer.length)) != -1)
                                out.write(buffer, 0, count);

                            // make poster folder if not exists
                            File posterFolder = new File(folder + "/poster/");
                            if (!posterFolder.exists()) posterFolder.mkdir();

                            /* save poster image file to disk */
                            Path filePosterPath = Paths.get(folder + "/poster/" + filePath);
                            Files.write(filePosterPath.toAbsolutePath(), out.toByteArray());
                            location.save();

                            // update progress bar progress
                            double per = (double) steps / productNodes.length;
                            bar.setProgress(steps, true);
                            steps++;

                            /* update UI label to informs the user about work status */
                            percent.post(() -> percent.setText((int) (per * 100.0) + "%"));
                            Thread.sleep(200);
                        }
                    } catch (SecurityException sex) {
                        percent.post(() -> percent.setText("Wait..."));
                        k--;
                    }
                }

                // all done, so go to the main page
                GoToMainPage();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void GoToMainPage() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}