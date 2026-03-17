package com.example.flexstream.model;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("ratings_by_user")
public class UserRating {

    @PrimaryKeyColumn(name = "userid", type = PrimaryKeyType.PARTITIONED)
    private int userId;

    @PrimaryKeyColumn(name = "timestamp", ordinal = 0, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private long timestamp;

    @PrimaryKeyColumn(name = "movieid", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private int movieId;

    @Column("rating")
    private float rating;

    public UserRating() {}

    public UserRating(int userId, int movieId, float rating, long timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
}
