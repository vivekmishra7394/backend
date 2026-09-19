package com.preetu.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.preetu.backend.dto.ProductRequest;
import com.preetu.backend.dto.ProductResponse;
import com.preetu.backend.dto.UserResponse;
import com.preetu.backend.entity.Product;
import com.preetu.backend.entity.Users;
import com.preetu.backend.repository.ProductRepository;

@Service
public class ProductService {

	private ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public ProductResponse createProduct(ProductRequest productRequest) {
		Product product = new Product();

		product.setName(productRequest.getName());
		product.setCategory(productRequest.getCategory());
		product.setPrice(productRequest.getPrice());
		product.setDescription(productRequest.getDescription());
		product.setStockQuantity(productRequest.getStockQuantity());

		Product savedProduct = productRepository.save(product);

		return toResponse(savedProduct);

	}

	public List<ProductResponse> getAllProducts() {
		return productRepository.findAll().stream().map(this::toResponse).toList();
	}

	public ProductResponse getProductById(Long id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found."));

		return toResponse(product);
	}

	public ProductResponse getProductByName(String name) {
		Product product = productRepository.findByName(name)
				.orElseThrow(() -> new RuntimeException("Product not found."));
		return toResponse(product);

	}

	public ProductResponse updateProductById(Long id, ProductRequest existingProduct) {
		Product updatedproduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));
		updatedproduct.setCategory(existingProduct.getCategory());
		updatedproduct.setDescription(existingProduct.getDescription());
		updatedproduct.setName(existingProduct.getName());
		updatedproduct.setPrice(existingProduct.getPrice());
		updatedproduct.setStockQuantity(existingProduct.getStockQuantity());

		Product existingProducts = productRepository.save(updatedproduct);

		return toResponse(existingProducts);

	}

	public void deleteById(Long id) {
		productRepository.deleteById(id);
	}

	@Transactional
	public void deleteByName(String name) {
		productRepository.deleteByName(name);
	}

	private ProductResponse toResponse(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
				product.getStockQuantity(), product.getCategory());
	}

}
