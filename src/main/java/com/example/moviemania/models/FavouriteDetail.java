package com.example.moviemania.models;

import com.example.moviemania.constants.FavouriteContentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class FavouriteDetail {

    @Id
    @GeneratedValue
    private Integer id;
    private LocalDate updatedOn;
    private String comment;
    private Integer rating;
    private Integer contentId;
    private FavouriteContentType contentType;

}

