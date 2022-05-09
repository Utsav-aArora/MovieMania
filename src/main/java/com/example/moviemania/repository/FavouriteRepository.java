package com.example.moviemania.repository;

import com.example.moviemania.constants.FavouriteContentType;
import com.example.moviemania.models.FavouriteDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<FavouriteDetail,Integer>
{
    Optional<FavouriteDetail> findByContentTypeAndContentId(FavouriteContentType contentType, Integer contentId);
}
