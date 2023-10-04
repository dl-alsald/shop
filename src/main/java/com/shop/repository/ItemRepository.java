package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>{
    //JpaRepository<Item(엔티티 타입 클래스), Long(기본키 타입)>
    //JpaRepository는 기본적인 CRUD 및 페이징 처리를 위한 메소드 정의 되어있음
    /* QuerydslPredicateExecutor
    Predicate: '조건이 맞다'고 판단하는 근거를 함수로 제공
    Predicate를 파라미터로 전달하기 위해서 QuerydslPredicateExecutor 상속
    */
    List<Item> findByItemNm(String itemNm); //매개변수로는 검색할 때 사용할 상품명 변수를 넘겨줌

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
    //@Param파라미터로 넘어온 값을 JPQL에 들어갈 변수로 지정해 줄 수 있음
    //현재는 itemDetail 변수를 like% ~ % 사이에 :itemDetail 값이 들어가도록 작성

    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);




}
