package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.iotstar.entity.Product;

public interface ProductService {

    Product save(Product entity);

    List<Product> findAll();

    Page<Product> findAll(Pageable pageable);

    List<Product> findAll(Sort sort);

    List<Product> findAllById(Iterable<Integer> ids);

    Optional<Product> findById(Integer id);

    <S extends Product> Optional<S> findOne(Example<S> example);

    long count();

    Boolean delete(Integer id);

    void delete(Product entity);

    Optional<Product> findByProductName(String productName);

    List<Product> findByProductNameContaining(String productName);

    Page<Product> findByProductNameContaining(String name, Pageable pageable);

    List<Product> findByPriceBetween(Float minPrice, Float maxPrice);

    List<Product> findAllByOrderByPriceAsc();

    List<Product> findAllByOrderByProductNameAsc();
    
    Boolean create(Product product);
    
    Product findByProductId(Integer id);
}
