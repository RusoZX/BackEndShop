package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.wrapper.ProductWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.id in (select i.product.id from ShoppingCart i where i.user.id= :userId)")
    List<Product> findAllInShoppingCart( @Param("userId") Long userId);

    @Query("select new com.daniilzverev.shopserver.wrapper.ProductWrapper(p.id, p.title, p.price, p.stock)" +
            " from Product p")
    List<ProductWrapper> findAllWrapper(Pageable pageable);

    @Query("select new com.daniilzverev.shopserver.wrapper.ProductWrapper(p.id, p.title, p.price, p.stock)" +
            " from Product p where p.category like %:category%")
    List<ProductWrapper> findAllByCategory(@Param("category") String category,Pageable pageable);

    @Query("select new com.daniilzverev.shopserver.wrapper.ProductWrapper(p.id, p.title, p.price, p.stock)" +
            " from Product p order by price asc")
    List<ProductWrapper> findAllByPriceAsc(Pageable pageable);

    @Query("select new com.daniilzverev.shopserver.wrapper.ProductWrapper(p.id, p.title, p.price, p.stock)" +
            " from Product p order by price desc")
    List<ProductWrapper> findAllByPriceDesc(Pageable pageable);

    @Query("select new com.daniilzverev.shopserver.wrapper.ProductWrapper(p.id, p.title, p.price, p.stock)" +
            " from Product p where p.brand like %:brand%")
    List<ProductWrapper> findAllByBrand(@Param("brand") String brand,Pageable pageable);

    @Query("select new com.daniilzverev.shopserver.wrapper.ProductWrapper(p.id, p.title, p.price, p.stock)" +
            " from Product p where p.color like %:color%")
    List<ProductWrapper> findAllByColor(@Param("color") String color,Pageable pageable);

    @Query("select new com.daniilzverev.shopserver.wrapper.ProductWrapper(p.id, p.title, p.price, p.stock)" +
            " from Product p where p.title like %:title%")
    List<ProductWrapper> findAllByTitle(@Param("title") String title,Pageable pageable);

}
