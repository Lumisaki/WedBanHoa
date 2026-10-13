package org.example.wedbanhoa.controller;

import jakarta.servlet.http.HttpSession;
import org.example.wedbanhoa.dto.CategoryResponse;
import org.example.wedbanhoa.dto.ProductResponse;
import org.example.wedbanhoa.entity.Account;
import org.example.wedbanhoa.service.CategoryService;
import org.example.wedbanhoa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public abstract class BaseController {

    @Autowired
    protected CategoryService categoryService;

    @Autowired
    protected ProductService productService;

    protected void setHeaderData(Model model, HttpSession session) {
        List<CategoryResponse> listCategories = categoryService.getAllCategories();
        model.addAttribute("listCategories", listCategories);

        if (session != null) {
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            if (cart != null && !cart.isEmpty()) {
                Map<Integer, ProductResponse> cartProducts = productService.getProductsForCart(cart.keySet());
                model.addAttribute("cartProducts", cartProducts);
            }
            Account user = (Account) session.getAttribute("userSession");
            if (user != null) {
                model.addAttribute("userSession", user);
            }
        }
    }
}
