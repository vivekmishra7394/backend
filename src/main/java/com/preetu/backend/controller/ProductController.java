package com.preetu.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.preetu.backend.dto.ApiResponse;
import com.preetu.backend.dto.ProductRequest;
import com.preetu.backend.dto.ProductResponse;
import com.preetu.backend.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/create-product")
	public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {

		ProductResponse product = productService.createProduct(productRequest);

		return new ApiResponse<>(true, "Product created successfully", product);
	}

	@GetMapping
	public ApiResponse<Page<ProductResponse>> getAllProducts(Pageable pageable) {

		Page<ProductResponse> products = productService.getAllProducts(pageable);

		return new ApiResponse<>(true, "Products fetched successfully", products);
	}

	@GetMapping("/get-product-by-id/{id}")
	public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) {

		ProductResponse product = productService.getProductById(id);

		return new ApiResponse<>(true, "Product fetched successfully", product);
	}

	@GetMapping("/get-product-by-name/{name}")
	public ApiResponse<ProductResponse> getProductByName(@PathVariable String name) {

		ProductResponse product = productService.getProductByName(name);

		return new ApiResponse<>(true, "Product fetched successfully", product);
	}

	@PutMapping("/update-product-by-id/{id}")
	public ApiResponse<ProductResponse> updateProductById(@PathVariable Long id,
			@Valid @RequestBody ProductRequest productRequest) {

		ProductResponse product = productService.updateProductById(id, productRequest);

		return new ApiResponse<>(true, "Product updated successfully", product);
	}

	@DeleteMapping("/delete-product-by-id/{id}")
	public ApiResponse<String> deleteById(@PathVariable Long id) {

		productService.deleteById(id);

		return new ApiResponse<>(true, "Product deleted successfully", "Deleted successfully.");
	}

	@DeleteMapping("/delete-product-by-name/{name}")
	public ApiResponse<String> deleteByName(@PathVariable String name) {

		productService.deleteByName(name);

		return new ApiResponse<>(true, "Product deleted successfully", "Deleted successfully.");
	}
}