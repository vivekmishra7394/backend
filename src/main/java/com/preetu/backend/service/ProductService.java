package com.preetu.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.preetu.backend.dto.ProductRequest;
import com.preetu.backend.dto.ProductResponse;
import com.preetu.backend.entity.Product;
import com.preetu.backend.exception.ConflictException;
import com.preetu.backend.exception.ResourceNotFoundException;
import com.preetu.backend.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	// CREATE PRODUCT
	public ProductResponse createProduct(ProductRequest productRequest) {

		if (productRepository.existsByName(productRequest.getName())) {
			throw new ConflictException(
					"Product name already exists. Please use a unique product name." + productRequest.getName());
		}

		Product product = new Product();

		product.setName(productRequest.getName());
		product.setCategory(productRequest.getCategory());
		product.setPrice(productRequest.getPrice());
		product.setDescription(productRequest.getDescription());
		product.setStockQuantity(productRequest.getStockQuantity());

		Product savedProduct = productRepository.save(product);

		return toResponse(savedProduct);
	}

	// GET ALL PRODUCTS
	public Page<ProductResponse> getAllProducts(Pageable pageable) {

		return productRepository.findAll(pageable).map(this::toResponse);
	}

	// GET PRODUCT BY ID
	public ProductResponse getProductById(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		return toResponse(product);
	}

	// GET PRODUCT BY NAME
	public ProductResponse getProductByName(String name) {

		Product product = productRepository.findByName(name)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with name: " + name));

		return toResponse(product);
	}

	// UPDATE PRODUCT
	public ProductResponse updateProductById(Long id, ProductRequest existingProduct) {

		Product updatedProduct = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		updatedProduct.setCategory(existingProduct.getCategory());
		updatedProduct.setDescription(existingProduct.getDescription());
		updatedProduct.setName(existingProduct.getName());
		updatedProduct.setPrice(existingProduct.getPrice());
		updatedProduct.setStockQuantity(existingProduct.getStockQuantity());

		Product savedProduct = productRepository.save(updatedProduct);

		return toResponse(savedProduct);
	}

	// DELETE PRODUCT BY ID
	public void deleteById(Long id) {

		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Product not found with id: " + id);
		}

		productRepository.deleteById(id);
	}

	// DELETE PRODUCT BY NAME
	@Transactional
	public void deleteByName(String name) {

		if (!productRepository.existsByName(name)) {
			throw new ResourceNotFoundException("Product not found with name: " + name);
		}

		productRepository.deleteByName(name);
	}

	// ENTITY → RESPONSE DTO
	private ProductResponse toResponse(Product product) {

		return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
				product.getStockQuantity(), product.getCategory());
	}
}