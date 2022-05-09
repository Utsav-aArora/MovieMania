package com.example.moviemania.service;

import com.example.moviemania.constants.FavouriteContentType;
import com.example.moviemania.exception.ConflictException;
import com.example.moviemania.exception.NotFoundException;
import com.example.moviemania.models.CustomFavouriteDetail;
import com.example.moviemania.models.FavouriteDetail;
import com.example.moviemania.models.FavouriteDetailToBeEdited;
import com.example.moviemania.repository.FavouriteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = FavouriteService.class)
public class FavouriteServiceTest {

    @MockBean
    FavouriteRepository favouriteRepository;

    @Autowired
    FavouriteService favouriteService;

    @Test
    public void returnSameFavouriteOnSaving() {
        FavouriteDetail favouriteDetail = new FavouriteDetail();
        favouriteDetail.setComment("nice movie");
        favouriteDetail.setRating(4);
        favouriteDetail.setContentId(123213);
        favouriteDetail.setContentType(FavouriteContentType.MOVIE);
        favouriteDetail.setId(1);
        favouriteDetail.setUpdatedOn(LocalDate.now());

        CustomFavouriteDetail customFavouriteDetail=new CustomFavouriteDetail();
        customFavouriteDetail.setComment("nice movie");
        customFavouriteDetail.setRating(4);
        customFavouriteDetail.setContentId(123213);
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
        favouriteDetail.setContentId(123213);
        favouriteDetail.setContentType(FavouriteContentType.MOVIE);
        favouriteDetail.setContentType(FavouriteContentType.MOVIE);
        favouriteDetail.setContentId(123);


        CustomFavouriteDetail customFavouriteDetail=new CustomFavouriteDetail();
        customFavouriteDetail.setComment("nice movie");
        customFavouriteDetail.setRating(4);
        customFavouriteDetail.setContentId(123213);
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
        favouriteDetail.setContentId(123213);
        favouriteDetail.setContentType(FavouriteContentType.MOVIE);
        favouriteDetail.setId(1);
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

}
