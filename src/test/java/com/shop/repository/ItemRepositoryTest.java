package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest //모든 Bean을 IoC컨테이너에 등록
@Log4j2
@TestPropertySource(locations="classpath:application-test.properties")
public class ItemRepositoryTest {

    @PersistenceContext
    EntityManager em; //영속성 컨텍스트(엔티티를 영구히 저장하는 환경) 사용

    @Autowired
    ItemRepository itemRepository; //ItemRepository를 사용하기 위해 @Autowired를 이용하여 Bean 주입

    @Test
    @DisplayName("상품 저장 테스트") //테스트 코드 실행 시 @DisplayName에 테스트명이 노출
    public void creatItemTest() {
        Item item = new Item();
        item.setItemNm("테스트 상품2");
        item.setPrice(20000);
        item.setItemDetail("테스트 상품 상세 설명2");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(200);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }//creatItemTest

    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();

            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명 " + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }//for
    }//createItemList

    public void createItemList2() {
        for (int i = 1; i <= 5; i++) {
            Item item = new Item();

            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명 " + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }//for

        for (int i = 1; i <= 10; i++) {
            Item item = new Item();

            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명 " + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }//for
    }//createItemList



    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }//for
    }//findByItemNmTest

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmoOrItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }//for
    }//findByItemNmoOrItemDetailTest

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        //this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }//for
    }//findByPriceLessThanTest

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        //this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }//for
    }//findByPriceLessThanOrderByPriceDesc

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        //this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }//for
    }//findByItemDetailTest

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNativeTest() {
        //this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }//for
    }//findByItemDetailByNative

    @Test
    @DisplayName("Querydsl 조회 테스트 1")
    public void queryDslTest() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        //JPAQueryFactory : QueryDSL을 사용하기 위한 핵심 객체
        QItem qItem = QItem.item;
        //QItem : QueryDSL 엔티티
        //QueryDSL을 통해 쿼리 생성
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                //qItem을 이용하여 Item엔티티 를 조회
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                //itemSellStatus가 sell인 것만 선택
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                //itemDetail에 "테스트 상품 상세 설명"문자열이 포함된 것만 선택
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();
        //query.fetch() 결과를 가져오는 메서드


        for (Item item : itemList) {
            System.out.println(item.toString());
        }//for
    }//queryDslTest

    @Test
    @DisplayName("Querydsl 조회 테스트 2")
    public void queryDslTest2() {

        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        //Querydsl에서 동적인 검색 조건을 생성하기 위한 빌더 패턴을 사용하는 객체

        QItem item = QItem.item;
        //QItem : QueryDSL 엔티티
        //QueryDSL을 통해 쿼리 생성

        /* 검색 조건 설정 */
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        //item.itemDetail.like("%" + itemDetail + "%") 항목 상세 설명에 itemDetail 변수에 해당하는 문자열이 포함된 항목 선택
        booleanBuilder.and(item.price.gt(price));

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5); //페이지네이션 설정
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        //findAll메소드를 사용하여 동적 검색 조건과 페이지네이션 설정을 적용하여 데이터 조회
        System.out.println("total elements : " + itemPagingResult.getTotalElements());


        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem : resultItemList){
            System.out.println(resultItem.toString());
        }//for
    }//queryDslTest2


}