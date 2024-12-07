package vn.iotstar.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import vn.iotstar.entity.Product;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductRepository productRepository;

    // Constructor
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @SuppressWarnings("deprecation")
	@Override
    public Product save(Product entity) {
        if (entity.getProductId() == null) {
            // Thêm mới
            return productRepository.save(entity);
        } else {
            // Cập nhật
            Optional<Product> opt = findById(entity.getProductId());
            if (opt.isPresent()) {
                Product existingProduct = opt.get();
                
                if (StringUtils.isEmpty(entity.getImage())) {
                    // Giữ lại ảnh cũ nếu không cung cấp ảnh mới
                    entity.setImage(existingProduct.getImage());
                }
                return productRepository.save(entity);
            }
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> findAll(Sort sort) {
        return productRepository.findAll(sort);
    }

    @Override
    public List<Product> findAllById(Iterable<Integer> ids) {
        return productRepository.findAllById(ids);
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public <S extends Product> Optional<S> findOne(Example<S> example) {
        return productRepository.findOne(example);
    }

    @Override
    public long count() {
        return productRepository.count();
    }

    @Override
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public void delete(Product entity) {
        productRepository.delete(entity);
    }

    @Override
    public Optional<Product> findByProductName(String productName) {
        return productRepository.findByProductName(productName);
    }

    @Override
    public List<Product> findByProductNameContaining(String name) {
        return productRepository.findByProductNameContainingIgnoreCase(name);
    }

    @Override
    public Page<Product> findByProductNameContaining(String name, Pageable pageable) {
        return productRepository.findByProductNameContaining(name, pageable);
    }

    @Override
    public List<Product> findByPriceBetween(Float minPrice, Float maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<Product> findAllByOrderByPriceAsc() {
        return productRepository.findAllByOrderByPriceAsc();
    }

    @Override
    public List<Product> findAllByOrderByProductNameAsc() {
        return productRepository.findAllByOrderByProductNameAsc();
    }

}
