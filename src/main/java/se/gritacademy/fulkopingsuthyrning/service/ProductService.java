package se.gritacademy.fulkopingsuthyrning.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import se.gritacademy.fulkopingsuthyrning.controller.dto.ProductRequest;
import se.gritacademy.fulkopingsuthyrning.exception.ConflictException;
import se.gritacademy.fulkopingsuthyrning.exception.ResourceNotFoundException;
import se.gritacademy.fulkopingsuthyrning.model.Product;
import se.gritacademy.fulkopingsuthyrning.repository.ProductRepository;
import se.gritacademy.fulkopingsuthyrning.repository.RentalRepository;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final RentalRepository rentalRepository;

    public ProductService(ProductRepository productRepository,  RentalRepository rentalRepository) {
        this.productRepository = productRepository;
        this.rentalRepository = rentalRepository;
    }

    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    public Product create(ProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setCategory(request.category());
        product.setPricePerDay(request.pricePerDay());

        return productRepository.save(product);
    }

    public Product update(Long id, ProductRequest request) {
        // nekar update av Product medan den är uthyrd
        if (rentalRepository.existsByProductIdAndReturnedDateIsNull(id))
            throw new ConflictException("Cannot update product that is currently rented out");
        Product product = findById(id);

        product.setName(request.name());
        product.setCategory(request.category());
        product.setPricePerDay(request.pricePerDay());

        return product;

    }

    public void delete(Long id) {
        if (!productRepository.existsById(id))
            throw new ResourceNotFoundException("Product", id);
        if (rentalRepository.existsByProductId(id))
            throw new ConflictException("Cannot delete a product that is referenced in rentals");
        productRepository.deleteById(id);
    }
}