package com.example.moviemania.service.external;

import com.example.moviemania.models.TmdbMovieDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TmdbService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public TmdbMovieDetail getOneFavouriteData(Long movieId)
    {

        TmdbMovieDetail movieData = webClientBuilder.baseUrl("https://api.themoviedb.org/3/movie").build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{movieId}")
                        .queryParam("api_key", "e4e3d08c7563c09b2ff1825334ddd2de")
                        .build(movieId)
                )
                .retrieve()
                .bodyToMono(TmdbMovieDetail.class)
                .block();
        return movieData;
    }
}
