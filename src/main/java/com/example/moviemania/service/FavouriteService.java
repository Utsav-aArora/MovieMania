package com.example.moviemania.service;

import com.example.moviemania.exception.ConflictException;
import com.example.moviemania.exception.NotFoundException;
import com.example.moviemania.models.*;
import com.example.moviemania.repository.FavouriteRepository;
import com.example.moviemania.service.external.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavouriteService {

    @Autowired
    FavouriteRepository favouriteRepository;

    @Autowired
    TmdbService tmdbService;


    public FavouriteDetail newFavourite(CustomFavouriteDetail customFavouriteDetail) {

        FavouriteDetail favouriteDetail = new FavouriteDetail();
        favouriteDetail.setContentType(customFavouriteDetail.getContentType());
        favouriteDetail.setContentId(customFavouriteDetail.getContentId());
        favouriteDetail.setComment(customFavouriteDetail.getComment());
        favouriteDetail.setRating(customFavouriteDetail.getRating());
        favouriteDetail.setUpdatedOn(LocalDate.now());

        Optional<FavouriteDetail> optionalFavouriteDetail = favouriteRepository.findByContentTypeAndContentId(customFavouriteDetail.getContentType(), customFavouriteDetail.getContentId());
        if (optionalFavouriteDetail.isEmpty()) {
            return favouriteRepository.save(favouriteDetail);
        }
        throw new ConflictException("Duplicate Found");
    }

    public FavouriteDetail editFavouriteDetail(FavouriteDetailToBeEdited favouriteDetailToBeEdited, int favouriteId) {
        Optional<FavouriteDetail> favouriteDetail = favouriteRepository.findById(favouriteId);
        if (favouriteDetail.isPresent()) {
            favouriteDetail.get().setComment(favouriteDetailToBeEdited.getComment());
            favouriteDetail.get().setRating(favouriteDetailToBeEdited.getRating());
            favouriteDetail.get().setUpdatedOn(LocalDate.now());
            return favouriteRepository.save(favouriteDetail.get());
        }
        throw new NotFoundException("favourite you are trying to update does not exists");
    }


    public void deletefavourite(Integer favouriteID) {

        favouriteRepository.deleteById(favouriteID);
    }

    public FinalCustomisedFavouriteDetail getFavouriteDetailById(Integer favouriteId) {
        Optional<FavouriteDetail> optionalFavouriteDetail = favouriteRepository.findById(favouriteId);

        return optionalFavouriteDetail.map(favouriteDetail -> {
            FinalCustomisedFavouriteDetail finalCustomisedFavouriteDetail = new FinalCustomisedFavouriteDetail();

            TmdbMovieDetail tmdbMovieDetails = tmdbService.getOneFavouriteData(favouriteDetail.getContentId());
            finalCustomisedFavouriteDetail.setFavouriteDetail(favouriteDetail);
            finalCustomisedFavouriteDetail.setId(tmdbMovieDetails.getId());
            finalCustomisedFavouriteDetail.setAdult(tmdbMovieDetails.getAdult());
            finalCustomisedFavouriteDetail.setGenres(tmdbMovieDetails.getGenres());
            finalCustomisedFavouriteDetail.setBackdrop_path(tmdbMovieDetails.getBackdrop_path());
            finalCustomisedFavouriteDetail.setOriginal_language(tmdbMovieDetails.getOriginal_language());
            finalCustomisedFavouriteDetail.setOverview(tmdbMovieDetails.getOverview());
            finalCustomisedFavouriteDetail.setPopularity(tmdbMovieDetails.getPopularity());
            finalCustomisedFavouriteDetail.setOriginal_title(tmdbMovieDetails.getOriginal_title());
            finalCustomisedFavouriteDetail.setPoster_path(tmdbMovieDetails.getPoster_path());
            finalCustomisedFavouriteDetail.setTitle(tmdbMovieDetails.getTitle());
            finalCustomisedFavouriteDetail.setVideo(tmdbMovieDetails.getVideo());
            finalCustomisedFavouriteDetail.setRelease_date(tmdbMovieDetails.getRelease_date());
            finalCustomisedFavouriteDetail.setVote_count(tmdbMovieDetails.getVote_count());
            finalCustomisedFavouriteDetail.setVote_average(tmdbMovieDetails.getVote_average());

            return finalCustomisedFavouriteDetail;

        }).orElseThrow(() -> new NotFoundException("Movie not found for this favourite"));
    }

    public List<FinalCustomisedFavouriteDetail> getAllFavouriteDetail() {

        List<FavouriteDetail> favouriteDetails = favouriteRepository.findAll();
        return favouriteDetails.stream().map(favouriteDetail -> {

            FinalCustomisedFavouriteDetail finalCustomisedFavouriteDetail = new FinalCustomisedFavouriteDetail();

            TmdbMovieDetail tmdbMovieDetails = tmdbService.getOneFavouriteData(favouriteDetail.getContentId());

            finalCustomisedFavouriteDetail.setFavouriteDetail(favouriteDetail);
            finalCustomisedFavouriteDetail.setId(tmdbMovieDetails.getId());
            finalCustomisedFavouriteDetail.setAdult(tmdbMovieDetails.getAdult());
            finalCustomisedFavouriteDetail.setGenres(tmdbMovieDetails.getGenres());
            finalCustomisedFavouriteDetail.setBackdrop_path(tmdbMovieDetails.getBackdrop_path());
            finalCustomisedFavouriteDetail.setOriginal_language(tmdbMovieDetails.getOriginal_language());
            finalCustomisedFavouriteDetail.setOverview(tmdbMovieDetails.getOverview());
            finalCustomisedFavouriteDetail.setPopularity(tmdbMovieDetails.getPopularity());
            finalCustomisedFavouriteDetail.setOriginal_title(tmdbMovieDetails.getOriginal_title());
            finalCustomisedFavouriteDetail.setPoster_path(tmdbMovieDetails.getPoster_path());
            finalCustomisedFavouriteDetail.setTitle(tmdbMovieDetails.getTitle());
            finalCustomisedFavouriteDetail.setVideo(tmdbMovieDetails.getVideo());
            finalCustomisedFavouriteDetail.setRelease_date(tmdbMovieDetails.getRelease_date());
            finalCustomisedFavouriteDetail.setVote_count(tmdbMovieDetails.getVote_count());
            finalCustomisedFavouriteDetail.setVote_average(tmdbMovieDetails.getVote_average());

            return finalCustomisedFavouriteDetail;

        }).collect(Collectors.toList());

    }
}
