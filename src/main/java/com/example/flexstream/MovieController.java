package com.example.flexstream;

import com.example.flexstream.model.HistoryResponse;
import com.example.flexstream.repository.RatingRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MovieController {

    private final RatingRepository repository;
    private final MovieService movieService;

    public MovieController(RatingRepository repository, MovieService movieService) {
        this.repository = repository;
        this.movieService = movieService;
    }

    @GetMapping("/history/{userId}")
    public List<HistoryResponse> getUserHistory(@PathVariable int userId) {
        return repository.findByUserId(userId).stream()
                .map(r -> new HistoryResponse(
                        r.getUserId(),
                        r.getMovieId(),
                        movieService.getTitle(r.getMovieId()),
                        r.getRating(),
                        r.getTimestamp()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/top")
    public List<HistoryResponse> getTopMovies() {
        return repository.findByRating(5.0f).stream()
                .limit(10)
                .map(r -> new HistoryResponse(
                        r.getUserId(),
                        r.getMovieId(),
                        movieService.getTitle(r.getMovieId()),
                        r.getRating(),
                        r.getTimestamp()
                ))
                .collect(Collectors.toList());
    }
}
