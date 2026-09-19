package com.preetu.backend.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.preetu.backend.dto.ProductRequest;
import com.preetu.backend.dto.ProductResponse;
import com.preetu.backend.entity.Product;
import com.preetu.backend.service.ProductService;

import jakarta.validation.Valid;

@Controller
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/create-product")
	public ProductResponse createProduct(@Valid @RequestBody ProductRequest productRequest) {
		return productService.createProduct(productRequest);
	}

	@GetMapping
	public List<ProductResponse> getAllProducts() {
		return productService.getAllProducts();
	}

	@GetMapping("get-product-by-id/{id}")
	public ProductResponse getProductById(@PathVariable Long id) {
		return productService.getProductById(id);
	}
	@GetMapping("get-product-by-name/{name}")
	public ProductResponse getProductByName(@Valid @PathVariable String name) {
		return productService.getProductByName(name);
	}
	
	@PutMapping("update-product-by-id/{id}")
	public ProductResponse updateProductById(@Valid @PathVariable Long id, @RequestBody ProductRequest productRequest) {
		return productService.updateProductById(id, productRequest);
	}

	@DeleteMapping("delete-product-by-id/{id}")
	public String deleteById(@Valid @PathVariable Long id) {
		 productService.deleteById(id);
		 return "Deleted successfully.";
	}

	@DeleteMapping("delete-product-by-name/{name}")
	public String deleteByName(@Valid @PathVariable String name) {
		System.out.println(name);
		productService.deleteByName(name);
		return "Deleted successfully.";
	}

}
