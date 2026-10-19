package se.gritacademy.fulkopingsuthyrning.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
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

    public Product create(Product product) {
    /*
         id får inte tas med om det skulle komma med från body
         då blir existerande rad overwritten
         fixa helst med DTO
    */
        return productRepository.save(product);
    }

    public Product update(Product product, Long id) {
        if(!productExists(id))
            throw new EntityNotFoundException("Product with id " + id + " was not found");
        product.setId(id);
        return productRepository.save(product);
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