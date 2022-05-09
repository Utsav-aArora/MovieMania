package com.example.moviemania.service;

import com.example.moviemania.exception.ConflictException;
import com.example.moviemania.exception.NotFoundException;
import com.example.moviemania.models.CustomFavouriteDetail;
import com.example.moviemania.models.FavouriteDetail;
import com.example.moviemania.models.FavouriteDetailToBeEdited;
import com.example.moviemania.repository.FavouriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class FavouriteService {

    @Autowired
    FavouriteRepository favouriteRepository;

    public FavouriteDetail newFavourite(CustomFavouriteDetail customFavouriteDetail) {

        FavouriteDetail favouriteDetail=new FavouriteDetail();
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

    public FavouriteDetail editFavouriteDetail(FavouriteDetailToBeEdited favouriteDetailToBeEdited, int favouriteId)
    {
      Optional<FavouriteDetail> favouriteDetail= favouriteRepository.findById(favouriteId);
      if(favouriteDetail.isPresent()) {
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
}
