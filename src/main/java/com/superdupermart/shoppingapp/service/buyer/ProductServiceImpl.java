package com.superdupermart.shoppingapp.service.buyer;

import com.superdupermart.shoppingapp.dao.ProductDao;
import com.superdupermart.shoppingapp.dto.buyer.ProductDto;
import com.superdupermart.shoppingapp.dto.buyer.ProductDetailDto;
import com.superdupermart.shoppingapp.entity.Product;
import com.superdupermart.shoppingapp.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public List<ProductDto> getAllInStockProducts() {
        List<Product> products = productDao.getAllInStock();
        return products.stream().map(p -> {
            ProductDto dto = new ProductDto();
            dto.setName(p.getProductName());
            dto.setDescription(p.getDescription());
            dto.setRetailPrice(p.getRetailPrice());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ProductDetailDto getProductDetail(Long id) {
        Product p = productDao.getById(id);
        if(p == null || p.getStock() <= 0) {
            throw new ResourceNotFoundException("Product not found or out of stock");
        }
        ProductDetailDto dto = new ProductDetailDto();
        dto.setId(p.getId());
        dto.setName(p.getProductName());
        dto.setDescription(p.getDescription());
        dto.setRetailPrice(p.getRetailPrice());
        return dto;
    }
}
