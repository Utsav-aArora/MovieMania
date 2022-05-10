//package com.example.moviemania.manan.demo;
//
//import com.example.moviemania.models.FavouriteDetail;
//import com.example.moviemania.repository.FavouriteRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.List;
//
//@SpringBootTest(classes = com.example.moviemania.manan.demo.FavouriteService.class)
//class FavouriteServiceTest {
//
//    @Autowired
//    private FavouriteService favouriteService;
//
//    @MockBean
//    private FavouriteRepository favouriteRepository;
//
//    @Test
//    void testContext() {
//        Assertions.assertNotNull(favouriteService);
//    }
//
//
//    @Test
//    void testAddFavourite(){
//
//        ArgumentCaptor<FavouriteDetail> detailCaptor = ArgumentCaptor.forClass(FavouriteDetail.class);
//        Mockito.verify(favouriteRepository).save(detailCaptor.capture());
//        FavouriteDetail detail =  favouriteService.addFavourite();
//
//
//        FavouriteDetail favouriteDetail =  detailCaptor.getValue();
//        Assertions.assertEquals( "movie was good" ,favouriteDetail.getComment());
//
//
//
//    }
//
//
//    @Test
//    void getFavourites(){
//        int kgfId = 1;
//        int rrrId = 2;
//
//        var kgfFavouriteDetail = new FavouriteDetail();
//        kgfFavouriteDetail.setContentId(kgfId);
//
//        var rrrFavouriteDetail = new FavouriteDetail();
//        rrrFavouriteDetail.setContentId(rrrId);
//
//        List<FavouriteDetail> of = List.of(kgfFavouriteDetail,rrrFavouriteDetail);
//        Mockito.when(favouriteRepository.findAll()).thenReturn(of);
//
//        List<MockModel> result = favouriteService.getFavourites();
//
//        Assertions.assertEquals(2, result.size());
//        var movie1 = result.get(0);
//        Assertions.assertEquals(kgfId,movie1.getMovieId());
//
////        Assertions.assertEquals("RRR" , movie1.getMovieName());
//        var movie2 = result.get(1);
//        Assertions.assertEquals(rrrId,movie2.getMovieId());
//
////        Assertions.assertEquals("KGF" , movie1.getMovieName());
//
//
//    }
//
//}