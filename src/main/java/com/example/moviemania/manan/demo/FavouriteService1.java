//package com.example.moviemania.manan.demo;
//
//import com.example.moviemania.models.FavouriteDetail;
//import com.example.moviemania.repository.FavouriteRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class FavouriteService {
//
//    @Autowired
//    private FavouriteRepository favouriteRepository;
//
//
//    public FavouriteDetail addFavourite() {
//        return null;
//    }
//
//    public List<MockModel> getFavourites() {
//
//        var favouritedetails =  favouriteRepository.findAll();
//
//
//        return favouritedetails.stream().map(detail -> {
//            var model = new MockModel();
//            model.setMovieId(detail.getContentId());
//            return model;
//        }).collect(Collectors.toList());
//    }
//}
