package com.example.moviemania.service;

import com.example.moviemania.constants.FavouriteContentType;
import com.example.moviemania.exception.ConflictException;
import com.example.moviemania.exception.NotFoundException;
import com.example.moviemania.models.*;
import com.example.moviemania.repository.FavouriteRepository;
import com.example.moviemania.service.external.TmdbService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = FavouriteService.class)
public class FavouriteServiceTest {

    @MockBean
    FavouriteRepository favouriteRepository;

    @MockBean
    TmdbService tmdbService;


    @Autowired
    FavouriteService favouriteService;

    @Test
    public void returnSameFavouriteOnSaving() {
        FavouriteDetail favouriteDetail = new FavouriteDetail();
        favouriteDetail.setComment("nice movie");
        favouriteDetail.setRating(4);
        favouriteDetail.setContentId(123213L);
        favouriteDetail.setContentType(FavouriteContentType.MOVIE);
        favouriteDetail.setFavouriteId(1);
        favouriteDetail.setUpdatedOn(LocalDate.now());

        CustomFavouriteDetail customFavouriteDetail=new CustomFavouriteDetail();
        customFavouriteDetail.setComment("nice movie");
        customFavouriteDetail.setRating(4);
        customFavouriteDetail.setContentId(123213L);
        customFavouriteDetail.setContentType(FavouriteContentType.MOVIE);


        when(favouriteRepository.save(any(FavouriteDetail.class))).thenReturn(favouriteDetail);

        FavouriteDetail favouriteDetail1 = favouriteService.newFavourite(customFavouriteDetail);

        Assertions.assertEquals(customFavouriteDetail.getContentId(), favouriteDetail1.getContentId());
    }

    @Test
    public void throwConflictExceptionOnSavingIfFavouriteAlreadyExists()
    {
        FavouriteDetail favouriteDetail=new FavouriteDetail();
        favouriteDetail.setComment("nice movie");
        favouriteDetail.setRating(4);
        favouriteDetail.setContentId(12321L);
        favouriteDetail.setContentType(FavouriteContentType.MOVIE);
        favouriteDetail.setContentType(FavouriteContentType.MOVIE);
        favouriteDetail.setContentId(123L);

        CustomFavouriteDetail customFavouriteDetail=new CustomFavouriteDetail();
        customFavouriteDetail.setComment("nice movie");
        customFavouriteDetail.setRating(4);
        customFavouriteDetail.setContentId(123213L);
        customFavouriteDetail.setContentType(FavouriteContentType.MOVIE);

        when(favouriteRepository.findByContentTypeAndContentId(customFavouriteDetail.getContentType(),customFavouriteDetail.getContentId())).thenReturn(Optional.of(favouriteDetail));
        Assertions.assertThrows(ConflictException.class, ()-> favouriteService.newFavourite(customFavouriteDetail));

    }

    @Test
    public void checkIfChangesInFavouriteDetailAreReflected()
    {
        Integer favouriteId=1;

        FavouriteDetail favouriteDetail = new FavouriteDetail();
        favouriteDetail.setComment("nice movie");
        favouriteDetail.setRating(4);
        favouriteDetail.setContentId(123213L);
        favouriteDetail.setContentType(FavouriteContentType.MOVIE);
        favouriteDetail.setFavouriteId(1);
        favouriteDetail.setUpdatedOn(LocalDate.now());

        FavouriteDetailToBeEdited favouriteDetailToBeEdited=new FavouriteDetailToBeEdited();
        favouriteDetailToBeEdited.setComment("nice movie");
        favouriteDetailToBeEdited.setRating(4);

        when(favouriteRepository.getById(favouriteId)).thenReturn(favouriteDetail);

        when(favouriteRepository.save(any(FavouriteDetail.class))).thenReturn(favouriteDetail);

        Assertions.assertEquals(favouriteDetailToBeEdited.getComment(),favouriteDetail.getComment());
        Assertions.assertEquals(favouriteDetailToBeEdited.getRating(),favouriteDetail.getRating());
        Assertions.assertThrows(NotFoundException.class,()->favouriteService.editFavouriteDetail(favouriteDetailToBeEdited,favouriteId));

    }

    @Test
    public void checkIfFavouriteIsDeletedFromDataBase()
    {
        Integer favouriteID=1;
        favouriteService.deletefavourite(favouriteID);
        verify(favouriteRepository, times(1)).deleteById(favouriteID);
    }

    @Test
    public void checkIfCorrespondingDataIsReturningForGivenFavouriteId()
    {
        Integer favouriteId=1;

        FavouriteDetail favouriteDetail = new FavouriteDetail();
        favouriteDetail.setComment("nice movie");
        favouriteDetail.setRating(4);
        favouriteDetail.setContentId(123213L);
        favouriteDetail.setContentType(FavouriteContentType.MOVIE);
        favouriteDetail.setUpdatedOn(LocalDate.now());

        long contentId=123;
        TmdbMovieDetail tmdbMovieDetail=new TmdbMovieDetail();
        tmdbMovieDetail.setId(contentId);
        when(tmdbService.getOneFavouriteData(contentId)).thenReturn(tmdbMovieDetail);
        when(favouriteRepository.getById(favouriteId)).thenReturn(favouriteDetail);

        FinalCustomisedFavouriteDetail finalCustomisedFavouriteDetail=new FinalCustomisedFavouriteDetail();
        finalCustomisedFavouriteDetail.setFavouriteDetail(favouriteDetail);
        Assertions.assertEquals(favouriteId,finalCustomisedFavouriteDetail.getFavouriteDetail().getFavouriteId());
    }

    @Test
    public void returnCustomisedFavouriteDetailCorrespondingToGetRequest()
    {
        FavouriteDetail favouriteDetail=new FavouriteDetail();
        FavouriteDetail favouriteDetail1=new FavouriteDetail();

        favouriteDetail.setFavouriteId(123);
        favouriteDetail.setContentId(123L);
        favouriteDetail1.setFavouriteId(456);
        favouriteDetail1.setContentId(456L);

        TmdbMovieDetail tmdbMovieDetail=new TmdbMovieDetail();
        tmdbMovieDetail.setId(123L);
        tmdbMovieDetail.setAdult(false);
        tmdbMovieDetail.setBackdrop_path("/jOuCWdh0BE6XPu2Vpjl08wDAeFz.jpg");
        tmdbMovieDetail.setGenres(List.of(new Genre(123,"asdasd")));
        tmdbMovieDetail.setOverview("as");
        tmdbMovieDetail.setOriginal_language("sfsdf");
        tmdbMovieDetail.setOriginal_title("sadad");
        tmdbMovieDetail.setPopularity(4F);
        tmdbMovieDetail.setPoster_path("ytuty");
        tmdbMovieDetail.setRelease_date(LocalDate.now());
        tmdbMovieDetail.setTitle("tyutyu");
        tmdbMovieDetail.setVote_average(2F);
        tmdbMovieDetail.setVote_count(22L);
        tmdbMovieDetail.setVideo(false);

        TmdbMovieDetail tmdbMovieDetail1=new TmdbMovieDetail();
        tmdbMovieDetail1.setId(456L);
        tmdbMovieDetail1.setAdult(false);
        tmdbMovieDetail1.setBackdrop_path("/jOuCWdh0BE6XPu2Vpjl08wDAeFz.jpg");
        tmdbMovieDetail1.setGenres(List.of(new Genre(123,"asdasd")));
        tmdbMovieDetail1.setOverview("as");
        tmdbMovieDetail1.setOriginal_language("sfsdf");
        tmdbMovieDetail1.setOriginal_title("sadad");
        tmdbMovieDetail1.setPopularity(4F);
        tmdbMovieDetail1.setPoster_path("");
        tmdbMovieDetail1.setRelease_date(LocalDate.now());
        tmdbMovieDetail1.setTitle("");
        tmdbMovieDetail1.setVote_average(2F);
        tmdbMovieDetail1.setVote_count(22L);
        tmdbMovieDetail1.setVideo(false);


        when(tmdbService.getOneFavouriteData(123L)).thenReturn(tmdbMovieDetail);
        when(tmdbService.getOneFavouriteData(456L)).thenReturn(tmdbMovieDetail1);


        when(favouriteRepository.findAll()).thenReturn(List.of(favouriteDetail,favouriteDetail1));
        List<FinalCustomisedFavouriteDetail> finalCustomisedFavouriteDetails=favouriteService.getAllFavouriteDetail();
        Assertions.assertEquals(2,finalCustomisedFavouriteDetails.size());
        Assertions.assertEquals(123,finalCustomisedFavouriteDetails.get(0).getFavouriteDetail().getFavouriteId());
        Assertions.assertEquals(456,finalCustomisedFavouriteDetails.get(1).getFavouriteDetail().getFavouriteId());

        verify(tmdbService, times(1)).getOneFavouriteData(123L);
        verify(tmdbService, times(1)).getOneFavouriteData(456L);

    }
}
