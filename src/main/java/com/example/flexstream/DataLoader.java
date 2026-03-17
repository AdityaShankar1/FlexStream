
package com.example.flexstream;



import com.example.flexstream.model.UserRating;

import com.example.flexstream.repository.RatingRepository;

import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import java.nio.file.Files;

import java.nio.file.Paths;

import java.util.concurrent.Executors;

import java.util.concurrent.atomic.AtomicInteger;

import java.util.stream.Stream;



@Component

public class DataLoader implements CommandLineRunner {

    private final RatingRepository repository;

    public DataLoader(RatingRepository repository) { this.repository = repository; }



    @Override

    public void run(String... args) throws Exception {

        if (repository.count() > 0) {

            System.out.println(">>> Data exists. Skipping ingestion.");

            return;

        }

        // Pointing to Docker Volume path

        String path = "/data/ratings.csv";

        AtomicInteger count = new AtomicInteger();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor();

             Stream<String> lines = Files.lines(Paths.get(path))) {

            lines.skip(1).limit(1000000).forEach(line -> executor.submit(() -> {

                try {

                    String[] cols = line.split(",");

                    repository.save(new UserRating(

                        Integer.parseInt(cols[0]), Integer.parseInt(cols[1]), 

                        Float.parseFloat(cols[2]), Long.parseLong(cols[3])

                    ));

                    if (count.incrementAndGet() % 10000 == 0) System.out.println("Ingested " + count.get());

                } catch (Exception ignore) {}

            }));

        }

    }

}

