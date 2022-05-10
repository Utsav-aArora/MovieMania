package com.example.moviemania.models;

import com.example.moviemania.constants.FavouriteContentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomFavouriteDetail {

    private String comment;
    private Integer rating;
    private Long contentId;
    private FavouriteContentType contentType;
}

