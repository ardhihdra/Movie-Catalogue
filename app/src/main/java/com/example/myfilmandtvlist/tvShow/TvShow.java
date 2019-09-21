package com.example.myfilmandtvlist.tvShow;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class TvShow implements Parcelable {
    private String name;
    private String description;
    private String release;

    private String genre;
    private String photoURL;

    private int photo;
    private int id;
    private double rating;
    private double popularity;
    private int voter;
    private boolean favorite = false;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public static final Parcelable.Creator<TvShow> CREATOR = new Parcelable.Creator<TvShow>() {
        public TvShow createFromParcel(Parcel in) { return new TvShow(in); }

        public TvShow[] newArray(int size) { return new TvShow[size]; }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public double getRating() {
        return rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVoter() {
        return voter;
    }

    public void setVoter(int voter) {
        this.voter = voter;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
        out.writeString(release);
        out.writeString(description);
        out.writeString(photoURL);
        out.writeDouble(rating);
        out.writeString(genre);
        out.writeInt(photo);
        out.writeDouble(popularity);
        out.writeInt(voter);
    }

    private TvShow(Parcel in) {
        id = in.readInt();
        name = in.readString();
        release = in.readString();
        description = in.readString();
        photoURL = in.readString();
        rating = in.readDouble();
        genre = in.readString();
        photo = in.readInt();
        popularity = in.readDouble();
        voter = in.readInt();
    }

    public TvShow(JSONObject object) {
        try {
            this.id  = object.getInt("id");
            this.name  = object.getString("name");
            this.release = object.getString("first_air_date");
            this.description = object.getString("overview");
            this.photoURL = "https://image.tmdb.org/t/p/w200"+object.getString("poster_path");
            this.rating = object.getDouble("vote_average");
            int genreId = object.getJSONArray("genre_ids").getInt(0);
            this.popularity = object.getDouble("popularity");
            this.voter = object.getInt("vote_count");
            //Log.d("Tes Download","genre film ---------------- : " + description);
            this.genre = Integer.toString(genreId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TvShow() {

    }
}
