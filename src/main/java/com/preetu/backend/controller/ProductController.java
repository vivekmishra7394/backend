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

import com.preetu.backend.entity.Product;
import com.preetu.backend.service.ProductService;

@Controller
@RestController
@RequestMapping("/products")
public class ProductController {

	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/create-product")
	public Product createProduct(Product product) {
		return productService.createProduct(product);
	}

	@GetMapping
	public List<Product> getAllProducts() {
		return productService.getAllProducts();
	}

	@GetMapping("get-product-by-id/{id}")
	public Product getProductById(@PathVariable Long id) {
		return productService.getProductById(id);
	}
	@GetMapping("get-product-by-name/{name}")
	public Product getProductByName(@PathVariable String name) {
		return productService.getProductByName(name);
	}
	
	@PutMapping("update-product-by-id/{id}")
	public Product updateProductById(@PathVariable Long id, @RequestBody Product products) {
		return productService.updateProductById(id, products);
	}

	@DeleteMapping("delete-product-by-id/{id}")
	public String deleteById(@PathVariable Long id) {
		 productService.deleteById(id);
		 return "Deleted successfully.";
	}

	@DeleteMapping("delete-product-by-name/{name}")
	public String deleteByName(@PathVariable String name) {
		System.out.println(name);
		productService.deleteByName(name);
		return "Deleted successfully.";
	}

}
