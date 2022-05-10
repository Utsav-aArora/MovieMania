package com.example.moviemania.models;

import com.example.moviemania.constants.FavouriteContentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FavouriteDetail {

    @Id
    @GeneratedValue
    private Integer favouriteId;
    private LocalDate updatedOn;
    private String comment;
    private Integer rating;
    private Long contentId;
    private FavouriteContentType contentType;

}

