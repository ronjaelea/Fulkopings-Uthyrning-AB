package se.gritacademy.fulkopingsuthyrning.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.gritacademy.fulkopingsuthyrning.model.Product;
import se.gritacademy.fulkopingsuthyrning.repository.ProductRepository;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        if (!productExists(id))
            throw new EntityNotFoundException("Product with id " + id + " was not found");
        return productRepository.findById(id).get();
    }

    public void create(Product product) {
        productRepository.save(product);
    }

    public void update(Product product, Long id) {
        if(!productExists(id))
            throw new EntityNotFoundException("Product with id " + id + " was not found");
        product.setId(id);
        productRepository.save(product);
    }

    public void delete(Long id) {
        if(!productExists(id))
            throw new EntityNotFoundException("Product with id " + id + " was not found");
        productRepository.deleteById(id);
    }

    private boolean productExists(Long id) {
        return productRepository.findById(id).isPresent();
    }
}