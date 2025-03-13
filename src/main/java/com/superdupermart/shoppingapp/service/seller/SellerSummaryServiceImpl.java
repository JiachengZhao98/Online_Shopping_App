package com.superdupermart.shoppingapp.service.seller;

import com.superdupermart.shoppingapp.dao.UserDao;
import com.superdupermart.shoppingapp.dto.seller.SellerSummaryDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.superdupermart.shoppingapp.entity.User;
import com.superdupermart.shoppingapp.exception.ResourceNotFoundException;

@Service
public class SellerSummaryServiceImpl implements SellerSummaryService {

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
    public SellerSummaryDto getSellerSummary() {
        User user = getLoggedInUser();
        if (!user.getRole().equalsIgnoreCase("ROLE_SELLER")) {
            throw new ResourceNotFoundException("Not A Seller, Access Denied");
        }
        SellerSummaryDto dto = new SellerSummaryDto();
        dto.setTopProfitProduct("Sample Product");
        dto.setTopPopularProducts("Product1, Product2, Product3");
        dto.setTotalItemsSold(100);
        return dto;
    }
}
