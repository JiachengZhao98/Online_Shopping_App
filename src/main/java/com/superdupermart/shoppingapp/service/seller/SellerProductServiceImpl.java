package com.superdupermart.shoppingapp.service.seller;

import com.superdupermart.shoppingapp.dao.ProductDao;
import com.superdupermart.shoppingapp.dto.seller.SellerProductDto;
import com.superdupermart.shoppingapp.entity.Product;
import com.superdupermart.shoppingapp.exception.ResourceNotFoundException;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.superdupermart.shoppingapp.entity.User;
import com.superdupermart.shoppingapp.dao.UserDao;

@Service
public class SellerProductServiceImpl implements SellerProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    private User getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    public SellerProductDto getProductDetail(Long id) {
        User user = getLoggedInUser();
        if (!user.getRole().equalsIgnoreCase("ROLE_SELLER")) {
            throw new ResourceNotFoundException("Not A Seller, Access Denied");
        }
        Product product = productDao.getById(id);
        if(product == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        SellerProductDto dto = new SellerProductDto();
        dto.setName(product.getProductName());
        dto.setDescription(product.getDescription());
        dto.setWholesalePrice(product.getWholesalePrice());
        dto.setRetailPrice(product.getRetailPrice());
        dto.setStock(product.getStock());
        return dto;
    }

    @Override
    public void addProduct(SellerProductDto productDto) {
        User user = getLoggedInUser();
        if (!user.getRole().equalsIgnoreCase("ROLE_SELLER")) {
            throw new ResourceNotFoundException("Not A Seller, Access Denied");
        }
        Product prod = productDao.getByName(productDto.getName());
        if(prod != null) {
            throw new IllegalArgumentException("Product already exists");
        }
        if (productDto.getName() == null || productDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (productDto.getWholesalePrice() < 0 || productDto.getRetailPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (productDto.getStock() <= 0) {
            throw new IllegalArgumentException("Stock cannot be negative or zero");
        }
        Product product = new Product();
        product.setProductName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setWholesalePrice(productDto.getWholesalePrice());
        product.setRetailPrice(productDto.getRetailPrice());
        product.setStock(productDto.getStock());
        product.setActive(true);
        productDao.save(product);
    }

    @Override
    public void updateProduct(SellerProductDto productDto) {
        User user = getLoggedInUser();
        if (!user.getRole().equalsIgnoreCase("ROLE_SELLER")) {
            throw new ResourceNotFoundException("Not A Seller, Access Denied");
        }
        Product product = productDao.getByName(productDto.getName());
        if(product == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        if (productDto.getWholesalePrice() < 0 || productDto.getRetailPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (productDto.getStock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        product.setProductName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setWholesalePrice(productDto.getWholesalePrice());
        product.setRetailPrice(productDto.getRetailPrice());
        product.setStock(productDto.getStock());
        if (productDto.getStock() == 0) {
            product.setActive(false);
        }
        else {
            product.setActive(true);
        }
        productDao.update(product);
    }

    @Override
    public List<SellerProductDto> getAllProducts() {
        User user = getLoggedInUser();
        if (!user.getRole().equalsIgnoreCase("ROLE_SELLER")) {
            throw new ResourceNotFoundException("Not A Seller, Access Denied");
        }
        List<Product> products = productDao.getAll();
        return products.stream().map(product -> {
            SellerProductDto dto = new SellerProductDto();
            dto.setName(product.getProductName());
            dto.setDescription(product.getDescription());
            dto.setWholesalePrice(product.getWholesalePrice());
            dto.setRetailPrice(product.getRetailPrice());
            dto.setStock(product.getStock());
            return dto;
        }).toList();
    }
}
