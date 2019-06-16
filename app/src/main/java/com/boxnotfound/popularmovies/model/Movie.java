package com.boxnotfound.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
The Movie Entity is Parcelable to allow ease of transfer between the MovieActivity and DetailActivity
*/
@Entity(tableName = "favorite_movie_table")
public class Movie implements Parcelable {

    /*
        The RemoteMovieDataSource calls to retrieve popular movie data will be initially deserialized
        through the MovieJSONResult class via Gson, as per the TMDB returned JSON fields.  From there,
        the MovieJSONResult object's List<Movie> is populated from the returned
        "results" JSON array via Gson.
    */
    @SerializedName("id") @PrimaryKey private long id;
    @SerializedName("title") @Ignore private String title;
    @SerializedName("original_title") @Ignore private String originalTitle;
    @SerializedName("overview") @Ignore private String overview;
    @SerializedName("release_date") @Ignore private String releaseDate;
    @SerializedName("vote_average") @Ignore private double userRating;
    @SerializedName("poster_path") @ColumnInfo(name = "poster_path") private String posterPath;
    @SerializedName("backdrop_path") @Ignore private String backdropPosterPath;
    @SerializedName("genre_ids") @Ignore private List<Integer> genreIds;
    @SerializedName("genres") @Ignore private List<Genre> genreList;

    // Constructor for Favorite Movies Table movies
    public Movie(final long id, @NonNull final String posterPath) {
        this.id = id;
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getUserRating() {
        return userRating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public List<Genre> getGenreList() { return genreList; }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBackdropPosterPath() {
        return backdropPosterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeDouble(userRating);
        dest.writeString(posterPath);
        dest.writeString(backdropPosterPath);
        dest.writeSerializable((Serializable) genreIds);
        dest.writeSerializable((Serializable) genreList);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        userRating = in.readDouble();
        posterPath = in.readString();
        backdropPosterPath = in.readString();
        genreIds = (List<Integer>) in.readSerializable();
        genreList = (List<Genre>) in.readSerializable();
    }
}
