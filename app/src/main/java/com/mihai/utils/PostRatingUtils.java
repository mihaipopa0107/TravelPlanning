package com.mihai.utils;

import com.mihai.core.CommentViewModel;
import com.mihai.models.Account;
import com.mihai.models.PostRating;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostRatingUtils {
    public static boolean canPostMessage() {
        /* user is authenticated, so check if commented in the past */
        if(Session.IsAuthenticated()) {
            Long accountId = Session.GetAccountId();
            Long locationId = Session.getTravelLocationId();

            List<PostRating> postRatingList = Select.from(PostRating.class).list().stream()
                    .filter(e -> e.getAccountId() == accountId && e.getLocationId() == locationId).collect(Collectors.toList());

            /* if wrote a post with rating, he can not post again */
            if(postRatingList.size() > 0) return false;
            else return true;
        }

        return false;
    }

    public static boolean RemovePostMessage(Long id) {
        if(Session.IsAuthenticated()) {
            Long accountId = Session.GetAccountId();

            List<PostRating> postRatingList = Select.from(PostRating.class).list().stream()
                    .filter(e -> e.getAccountId() == accountId && e.getId() == id).collect(Collectors.toList());

            if(postRatingList.size() == 1) {
                /* there exists such comment */
                postRatingList.get(0).delete();
                return true;
            }

            return false;
        }

        return false;
    }

    public static List<CommentViewModel> getPostRatingsByLocation() {
        List<CommentViewModel> posts = new ArrayList<>();
        Long locationId = Session.getTravelLocationId();

        if(locationId > -1) {
            List<PostRating> postRatingList = Select.from(PostRating.class).list().stream()
                    .filter(e -> e.getLocationId() == locationId).collect(Collectors.toList());

            for(int j = 0; j < postRatingList.size(); j++) {
                PostRating comment = postRatingList.get(j);
                CommentViewModel post = new CommentViewModel();

                post.setBody(comment.getBody());
                post.setId(comment.getId());

                post.setRating(comment.getRating());
                post.setAccountId(comment.getAccountId());

                List<Account> accounts = Select.from(Account.class).list().stream()
                        .filter(e -> e.getId() == comment.getAccountId()).collect(Collectors.toList());

                /* map post comment */
                post.setUsername(accounts.get(0).getUsername());
                post.setAvatar(accounts.get(0).getAvatar());
                posts.add(post);
            }

            return posts;
        }

        return null;
    }

    public static boolean createPostRating(String body, int rating) {
        /* when user is authenticated */
        if(Session.IsAuthenticated()) {
            Long accountId = Session.GetAccountId();
            Long locationId = Session.getTravelLocationId();

            List<PostRating> postRatings = Select.from(PostRating.class).list().stream()
                    .filter(e -> e.getAccountId() == accountId && e.getLocationId() == locationId).collect(Collectors.toList());

            if(postRatings.size() == 0) {
                PostRating postRating = new PostRating();
                postRating.setRating(rating);
                postRating.setBody(body);

                /* save new post rating of user */
                postRating.setAccountId(accountId);
                postRating.setLocationId(locationId);
                postRating.save();
            }

            // post rating exists
            return false;
        }

        // not authenticated user
        return false;
    }

    public static double getCurrentTravelRating() {
        Long locationId = Session.getTravelLocationId();
        double average = 0.0;

        // fetch all post ratings
        List<PostRating> postRatings = Select.from(PostRating.class).list().stream()
                .filter(e -> e.getLocationId() == locationId).collect(Collectors.toList());


        for(int k = 0; k < postRatings.size(); k++)
            average += postRatings.get(k).getRating();

        if(postRatings.size() > 0)
            average /= postRatings.size();

        return average;
    }
}
