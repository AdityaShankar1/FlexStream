
package com.example.flexstream;



import jakarta.annotation.PostConstruct;

import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.stereotype.Service;

import java.nio.file.Files;

import java.nio.file.Paths;

import java.util.stream.Stream;



@Service

public class MovieService {

    private final StringRedisTemplate redisTemplate;

    private static final String REDIS_KEY = "movies:title";



    public MovieService(StringRedisTemplate redisTemplate) {

        this.redisTemplate = redisTemplate;

    }



    @PostConstruct

    public void init() {

        try {

            if (Boolean.FALSE.equals(redisTemplate.hasKey(REDIS_KEY))) {

                // Pointing to Docker Volume path

                String path = "/data/movies.csv"; 

                try (Stream<String> lines = Files.lines(Paths.get(path))) {

                    lines.skip(1).forEach(line -> {

                        String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                        if (cols.length >= 2) {

                            redisTemplate.opsForHash().put(REDIS_KEY, cols[0], cols[1].replace("\"", ""));

                        }

                    });

                }

                System.out.println(">>> Redis Cache Populated.");

            }

        } catch (Exception e) {

            System.err.println(">>> REDIS ERROR: " + e.getMessage());

        }

    }



    public String getTitle(int movieId) {

        Object title = redisTemplate.opsForHash().get(REDIS_KEY, String.valueOf(movieId));

        return title != null ? title.toString() : "Unknown Movie (" + movieId + ")";

    }

}

