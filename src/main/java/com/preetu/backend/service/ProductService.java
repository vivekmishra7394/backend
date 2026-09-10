package com.preetu.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.preetu.backend.entity.Product;
import com.preetu.backend.repository.ProductRepository;

@Service
public class ProductService {

	private ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product createProduct(Product product) {
		return productRepository.save(product);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found."));
	}

	public Product getProductByName(String name) {
		return productRepository.findByName(name).orElseThrow(()-> new RuntimeException("Product not found."));
	}

	public Product updateProductById(Long id, Product existingProduct) {
		Product updatedproduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));
		updatedproduct.setCategory(existingProduct.getCategory());
		updatedproduct.setDescription(existingProduct.getDescription());
		updatedproduct.setName(existingProduct.getName());
		updatedproduct.setPrice(existingProduct.getPrice());
		updatedproduct.setStockQuantity(existingProduct.getStockQuantity());

		return productRepository.save(updatedproduct);

	}

	public void deleteById(Long id) {
		productRepository.deleteById(id);
	}

	@Transactional
	public void deleteByName(String name) {
		productRepository.deleteByName(name);
	}

}
