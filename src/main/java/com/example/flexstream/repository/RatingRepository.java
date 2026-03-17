package com.example.flexstream.repository;

import com.example.flexstream.model.UserRating;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RatingRepository extends CrudRepository<UserRating, Integer> {
    List<UserRating> findByUserId(int userId);
    
    @AllowFiltering
    List<UserRating> findByRating(float rating);
}
