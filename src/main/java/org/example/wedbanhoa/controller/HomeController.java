package org.example.wedbanhoa.controller;

import jakarta.servlet.http.HttpSession;
import org.example.wedbanhoa.dto.CategoryResponse;
import org.example.wedbanhoa.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController extends BaseController {

    @GetMapping({"/", "/index", "/home"})
    public String index(Model model, HttpSession session) {
        setHeaderData(model, session);
        List<ProductResponse> discounted = productService.getHomeDiscountedProducts();
        model.addAttribute("dataProducts", discounted);

        String successMsg = (String) session.getAttribute("successMsg");
        if (successMsg != null) {
            model.addAttribute("successMsg", successMsg);
            session.removeAttribute("successMsg");
        }
        return "view/index";
    }

    @GetMapping({"/Category", "/category"})
    public String categoryPage(@RequestParam("cid") Integer cid,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               Model model, HttpSession session) {
        setHeaderData(model, session);
        int pageSize = 12;
        Page<ProductResponse> productPage = productService.searchAndSortProductsPageable(null, String.valueOf(cid), "default", page, pageSize);

        CategoryResponse category = categoryService.getById(cid);
        String categoryName = (category != null) ? category.getCategoryName() : "Danh mục hoa";

        model.addAttribute("dataProducts", productPage.getContent());
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("cid", cid);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", Math.max(1, productPage.getTotalPages()));
        return "view/category";
    }

    @GetMapping({"/Search", "/search"})
    public String search(@RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "page", defaultValue = "1") int page,
                         Model model, HttpSession session) {
        setHeaderData(model, session);
        int pageSize = 12;
        Page<ProductResponse> productPage = productService.searchAndSortProductsPageable(keyword, "all", "default", page, pageSize);

        model.addAttribute("dataProducts", productPage.getContent());
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", Math.max(1, productPage.getTotalPages()));
        model.addAttribute("pageUrlPattern", "Search?keyword=" + (keyword != null ? keyword : "") + "&page=");
        return "view/index";
    }

    // Endpoint REST API phục vụ test Postman & Demo thuyết trình N+1
    @GetMapping({"/api/products", "/api/category"})
    @org.springframework.web.bind.annotation.ResponseBody
    public List<ProductResponse> getProductsApi(@RequestParam(value = "cid", required = false) Integer cid) {
        if (cid != null) {
            return productService.searchAndSortProducts(null, String.valueOf(cid), "default");
        }
        return productService.getAllProducts();
    }
}
