package org.example.wedbanhoa.controller;

import jakarta.servlet.http.HttpSession;
import org.example.wedbanhoa.dto.ProductResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class ProductDetailController extends BaseController {

    @GetMapping("/ProductDetail")
    public String detail(@RequestParam("pid") Integer pid, Model model, HttpSession session) {
        setHeaderData(model, session);
        ProductResponse p = productService.getProductById(pid);
        if (p == null) {
            return "redirect:/index";
        }
        model.addAttribute("p", p);

        // Lấy sản phẩm liên quan cùng danh mục
        if (p.getCategoryId() != null) {
            List<ProductResponse> related = productService.searchAndSortProducts(null, String.valueOf(p.getCategoryId()), "default");
            model.addAttribute("allProducts", related);
        } else {
            model.addAttribute("allProducts", Collections.emptyList());
        }

        model.addAttribute("reviews", Collections.emptyList());
        return "view/productDetail";
    }

    @PostMapping("/Review")
    public String handleReview(@RequestParam("pid") Integer pid,
                               @RequestParam("rating") Integer rating,
                               @RequestParam("comment") String comment,
                               HttpSession session) {
        session.setAttribute("successMsg", "Cảm ơn bạn đã gửi đánh giá cho sản phẩm!");
        return "redirect:/ProductDetail?pid=" + pid;
    }
}
