package com.example.myfilmandtvlist.movie;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

public class Movie implements Parcelable {
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

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
      public Movie createFromParcel(Parcel in) { return new Movie(in); }

      public Movie[] newArray(int size) { return new Movie[size]; }
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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photo) {
        this.photoURL = photo;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        out.writeInt(isFavorite() ? 1 : 0);
    }

    private Movie(Parcel in) {
        // harus berurutan dengan writeToParcel gan, Omo Omo Omo
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
        favorite = in.readInt() == 1;
    }

    public Movie(JSONObject object) {
        try {
            this.id  = object.getInt("id");
            this.name  = object.getString("title");
            this.release = object.getString("release_date");
            this.description = object.getString("overview");
            this.photoURL = "https://image.tmdb.org/t/p/w200"+object.getString("poster_path");
            this.rating = object.getDouble("vote_average");
            int genreId = object.getJSONArray("genre_ids").getInt(0);
            this.popularity = object.getDouble("popularity");
            this.voter = object.getInt("vote_count");
            //Log.d("Tes Download","genre film ---------------- : " + description);
            this.genre = Integer.toString(genreId);
            this.favorite = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Movie() {
        this.favorite = false;
    }

}
