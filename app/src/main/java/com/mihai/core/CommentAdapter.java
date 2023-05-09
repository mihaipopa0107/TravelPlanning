package com.mihai.core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mihai.travelplanning.DescriptionActivity;
import com.mihai.travelplanning.MainActivity;
import com.mihai.travelplanning.R;
import com.mihai.utils.PostRatingUtils;
import com.mihai.utils.Session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.List;

public class CommentAdapter extends ArrayAdapter<CommentViewModel> {
    List<CommentViewModel> posts;
    private Context context;
    private int resource;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<CommentViewModel> posts) {
        super(context, resource, posts);
        this.posts = posts;

        this.context = context;
        this.resource = resource;
    }

    private void removePostRating(int position) {
        CommentViewModel commentViewModel = posts.get(position);
        PostRatingUtils.RemovePostMessage(commentViewModel.getId());

        /* refresh description page */
        Intent home = new Intent(context, DescriptionActivity.class);
        context.startActivity(home);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(convertView == null) row = inflater.inflate(resource, parent, false);

        TextView Username = row.findViewById(R.id.Username);
        TextView Body = row.findViewById(R.id.Body);
        ImageView imageFlag = row.findViewById(R.id.avatar);

        try {
            Bitmap bmp = getAvatarLogo(posts.get(position).getAvatar());
            imageFlag.setImageBitmap(bmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ImageButton removeComment = row.findViewById(R.id.removeComment);
        removeComment.setOnClickListener(ev -> removePostRating(position));

        if(Session.IsAuthenticated() && Session.GetAccountId() == posts.get(position).getAccountId())
            removeComment.setVisibility(View.VISIBLE);
        else
            removeComment.setVisibility(View.GONE);

        TextView textRating = row.findViewById(R.id.textRating);
        Username.setText(posts.get(position).getUsername());

        textRating.setText(posts.get(position).getRating() + "");
        Body.setText(posts.get(position).getBody());
        return row;
    }

    private Bitmap getAvatarLogo(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] buffer = Files.readAllBytes(file.toPath());
        Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

        // set account logo image to option menu
        Bitmap src = Bitmap.createScaledBitmap(bm, 192, 192, false);
        return src;
    }
}
