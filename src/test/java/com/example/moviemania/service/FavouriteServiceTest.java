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
        favouriteDetail.setFavouriteId(1);
        favouriteDetail.setUpdatedOn(LocalDate.now());
        favouriteDetail.setFavouriteId(1);

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

        long contentId=123;
        TmdbMovieDetail tmdbMovieDetail=new TmdbMovieDetail();
        tmdbMovieDetail.setId(contentId);
        when(tmdbService.getOneFavouriteData(contentId)).thenReturn(tmdbMovieDetail);

        when(favouriteRepository.findAll()).thenReturn(List.of(favouriteDetail,favouriteDetail1));
        List<FinalCustomisedFavouriteDetail> finalCustomisedFavouriteDetails=favouriteService.getAllFavouriteDetail();
        Assertions.assertEquals(2,finalCustomisedFavouriteDetails.size());

        verify(tmdbService, times(2)).getOneFavouriteData(contentId);




    }
}
