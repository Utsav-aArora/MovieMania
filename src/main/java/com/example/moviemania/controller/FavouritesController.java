package com.example.moviemania.controller;

import com.example.moviemania.models.CustomFavouriteDetail;
import com.example.moviemania.models.FavouriteDetail;
import com.example.moviemania.models.FavouriteDetailToBeEdited;
import com.example.moviemania.service.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favourites")
public class FavouritesController {

    @Autowired
    FavouriteService favouriteService;

    @GetMapping
    public void showAllFavorites(@RequestParam int type)
    {
        System.out.println(type);
    }

    @GetMapping("/{favouriteId}")
    public void showOneFavorite(@PathVariable int favouriteId)
    {
        System.out.println(favouriteId);
    }

    @PostMapping
    public ResponseEntity<FavouriteDetail> getUserData(@RequestBody CustomFavouriteDetail customFavouriteDetail)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(favouriteService.newFavourite(customFavouriteDetail));
    }

    @PutMapping("/{favouriteId}")
    public ResponseEntity<FavouriteDetail> editFavorite(@PathVariable int favouriteId,@RequestBody FavouriteDetailToBeEdited favouriteDetailToBeEdited)
    {
       return ResponseEntity.ok(favouriteService.editFavouriteDetail(favouriteDetailToBeEdited,favouriteId));
    }

    @DeleteMapping("/{favouriteId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable int favouriteId)
    {
        favouriteService.deletefavourite(favouriteId);
        return ResponseEntity.noContent().build();
    }

}
