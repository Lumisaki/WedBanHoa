package org.example.wedbanhoa.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.wedbanhoa.dto.*;
import org.example.wedbanhoa.entity.Account;
import org.example.wedbanhoa.service.AccountService;
import org.example.wedbanhoa.service.CategoryService;
import org.example.wedbanhoa.service.CouponService;
import org.example.wedbanhoa.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
public class AdminController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CouponService couponService;

    private boolean checkAdmin(HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        return user != null && user.getRoleId() != null && user.getRoleId() == 1;
    }

    @GetMapping("/AdminDashboard")
    public String dashboard(@RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "category", required = false) String category,
                            @RequestParam(value = "sort", required = false) String sort,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);

        model.addAttribute("totalRevenue", orderService.getTotalRevenue());
        model.addAttribute("totalOrders", orderService.getTotalOrders());
        model.addAttribute("totalProductCount", productService.getTotalProducts());
        model.addAttribute("listCC", categoryService.getAllCategories());

        int pageSize = 10;
        Page<ProductResponse> productPage = productService.searchAndSortProductsPageable(keyword, category, sort, page, pageSize);

        model.addAttribute("allProducts", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", Math.max(1, productPage.getTotalPages()));
        model.addAttribute("currentKeyword", keyword);
        model.addAttribute("currentCategory", category);
        model.addAttribute("currentSort", sort);
        model.addAttribute("pageUrlPattern", "AdminDashboard?keyword=" + (keyword != null ? keyword : "") +
                "&category=" + (category != null ? category : "") + "&sort=" + (sort != null ? sort : "") + "&page=");

        return "view/admin";
    }

    @GetMapping("/ManagerProduct")
    public String managerProduct(@RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "category", required = false) String category,
                                 @RequestParam(value = "sort", required = false) String sort,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);

        int pageSize = 10;
        Page<ProductResponse> productPage = productService.searchAndSortProductsPageable(keyword, category, sort, page, pageSize);

        model.addAttribute("listP", productPage.getContent());
        model.addAttribute("listCC", categoryService.getAllCategories());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", Math.max(1, productPage.getTotalPages()));
        model.addAttribute("currentKeyword", keyword);
        model.addAttribute("currentCategory", category);
        model.addAttribute("currentSort", sort);
        model.addAttribute("pageUrlPattern", "ManagerProduct?keyword=" + (keyword != null ? keyword : "") +
                "&category=" + (category != null ? category : "") + "&sort=" + (sort != null ? sort : "") + "&page=");

        return "view/ManagerProduct";
    }

    @GetMapping({"/addProduct", "/AddProduct"})
    public String showAddProductForm(@RequestParam(value = "from", defaultValue = "manager") String from,
                                     Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);
        model.addAttribute("listCC", categoryService.getAllCategories());
        model.addAttribute("fromPage", from);
        model.addAttribute("productRequest", new ProductRequest());
        return "view/addProduct";
    }

    @PostMapping("/AddProduct")
    public String handleAddProduct(@Valid @ModelAttribute("productRequest") ProductRequest productRequest,
                                   BindingResult bindingResult,
                                   @RequestParam(value = "pimage", required = false) MultipartFile imageFile,
                                   @RequestParam(value = "fromPage", defaultValue = "manager") String fromPage,
                                   Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";

        if (bindingResult.hasErrors()) {
            setHeaderData(model, session);
            model.addAttribute("listCC", categoryService.getAllCategories());
            model.addAttribute("fromPage", fromPage);
            return "view/addProduct";
        }

        try {
            productService.saveProductFromRequest(productRequest, imageFile);
            session.setAttribute("msgSuccess", "Thêm sản phẩm hoa tươi mới thành công!");
        } catch (IOException e) {
            session.setAttribute("msgError", "Lỗi lưu tải ảnh lên hệ thống: " + e.getMessage());
        }

        return "redirect:/" + ("admin".equalsIgnoreCase(fromPage) ? "AdminDashboard" : "ManagerProduct");
    }

    @GetMapping({"/loadProduct", "/EditProduct"})
    public String showEditProductForm(@RequestParam("pid") Integer pid,
                                      @RequestParam(value = "from", defaultValue = "manager") String from,
                                      Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);

        ProductRequest detail = productService.getProductForEdit(pid);
        if (detail == null) {
            session.setAttribute("msgError", "Không tìm thấy sản phẩm ID #" + pid);
            return "redirect:/ManagerProduct";
        }

        model.addAttribute("detail", detail);
        model.addAttribute("listCC", categoryService.getAllCategories());
        model.addAttribute("fromPage", from);
        return "view/UpdateProduct";
    }

    @PostMapping("/EditProduct")
    public String handleEditProduct(@Valid @ModelAttribute("productRequest") ProductRequest productRequest,
                                    BindingResult bindingResult,
                                    @RequestParam(value = "new_pimage", required = false) MultipartFile newImage,
                                    @RequestParam(value = "fromPage", defaultValue = "manager") String fromPage,
                                    Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";

        if (bindingResult.hasErrors()) {
            setHeaderData(model, session);
            model.addAttribute("detail", productRequest);
            model.addAttribute("listCC", categoryService.getAllCategories());
            model.addAttribute("fromPage", fromPage);
            return "view/UpdateProduct";
        }

        try {
            productService.saveProductFromRequest(productRequest, newImage);
            session.setAttribute("msgSuccess", "Cập nhật sản phẩm thành công!");
        } catch (IOException e) {
            session.setAttribute("msgError", "Lỗi cập nhật ảnh sản phẩm: " + e.getMessage());
        }

        return "redirect:/" + ("admin".equalsIgnoreCase(fromPage) ? "AdminDashboard" : "ManagerProduct");
    }

    @PostMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("pid") Integer pid,
                                @RequestParam(value = "from", defaultValue = "ManagerProduct") String from,
                                HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        boolean ok = productService.deleteProduct(pid);
        if (ok) {
            session.setAttribute("msgSuccess", "Đã xóa sản phẩm thành công!");
        } else {
            session.setAttribute("msgError", "Không thể xóa sản phẩm do có ràng buộc dữ liệu!");
        }
        return "redirect:/" + ("admin".equalsIgnoreCase(from) ? "AdminDashboard" : "ManagerProduct");
    }

    @GetMapping("/ManageCategory")
    public String manageCategory(Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "view/manageCategory";
    }

    @PostMapping("/ManageCategory")
    public String handleCategoryAction(@RequestParam("action") String action,
                                       @RequestParam(value = "cid", required = false) Integer cid,
                                       @RequestParam(value = "cname", required = false) String cname,
                                       HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        CategoryRequest req = CategoryRequest.builder().categoryId(cid).categoryName(cname).build();

        if ("add".equalsIgnoreCase(action)) {
            categoryService.insertCategory(req);
            session.setAttribute("msgSuccess", "Thêm danh mục thành công!");
        } else if ("update".equalsIgnoreCase(action)) {
            categoryService.updateCategory(req);
            session.setAttribute("msgSuccess", "Cập nhật danh mục thành công!");
        } else if ("delete".equalsIgnoreCase(action)) {
            categoryService.deleteCategory(cid);
            session.setAttribute("msgSuccess", "Đã xóa danh mục!");
        }
        return "redirect:/ManageCategory";
    }

    @GetMapping("/ManageOrder")
    public String manageOrder(@RequestParam(value = "page", defaultValue = "1") int page,
                              Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);

        int pageSize = 10;
        Page<OderResponse> orderPage = orderService.getAllOrdersForAdminPaged(page, pageSize);
        model.addAttribute("allOrders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", Math.max(1, orderPage.getTotalPages()));
        model.addAttribute("pageUrlPattern", "ManageOrder?page=");

        return "view/manageOrders";
    }

    @PostMapping("/UpdateOrderStatus")
    public String updateOrderStatus(@RequestParam("orderId") Integer orderId,
                                    @RequestParam("status") String status,
                                    HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        orderService.updateStatus(orderId, status);
        session.setAttribute("msgSuccess", "Cập nhật trạng thái đơn #" + orderId + " thành công!");
        return "redirect:/ManageOrder";
    }

    @GetMapping("/AdminOrderDetail")
    public String adminOrderDetail(@RequestParam("orderId") Integer orderId, Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);

        OderResponse order = orderService.getOrderById(orderId);
        if (order == null) return "redirect:/ManageOrder";

        List<OderDetailResponse> items = orderService.getOrderDetails(orderId);
        model.addAttribute("order", order);
        model.addAttribute("items", items);
        return "view/adminOrderDetail";
    }

    @GetMapping("/ManagerCustomer")
    public String managerCustomer(Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);
        List<AccountResponse> customers = accountService.getAllCustomers();
        model.addAttribute("listC", customers);
        return "view/ManagerCustomer";
    }

    @GetMapping("/CustomerDetail")
    public String customerDetail(@RequestParam("id") Integer id,
                                 @RequestParam(value = "tab", defaultValue = "info") String tab,
                                 Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);

        AccountResponse customer = accountService.getById(id);
        if (customer == null) return "redirect:/ManagerCustomer";

        List<OderResponse> customerOrders = orderService.getOrderHistoryByUserId(id);

        model.addAttribute("customer", customer);
        model.addAttribute("activeTab", tab);
        model.addAttribute("orders", customerOrders);
        model.addAttribute("reviews", Collections.emptyList());
        return "view/customerDetail";
    }

    @GetMapping("/AdminProductReviews")
    public String productReviews(@RequestParam("pid") Integer pid, Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);

        ProductResponse product = productService.getProductById(pid);
        if (product == null) return "redirect:/ManagerProduct";

        model.addAttribute("productDetail", product);
        model.addAttribute("reviews", Collections.emptyList());
        return "view/AdminProductReviews";
    }

    @GetMapping("/ManageCoupon")
    public String manageCoupon(Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);
        model.addAttribute("coupons", couponService.getAllCoupons());
        return "view/manageCoupon";
    }

    @PostMapping("/ManageCoupon")
    public String handleCouponAction(@RequestParam("action") String action,
                                     @RequestParam(value = "id", required = false) Integer id,
                                     @RequestParam(value = "code", required = false) String code,
                                     @RequestParam(value = "discountPercent", required = false) Integer percent,
                                     @RequestParam(value = "expiryDate", required = false) String expiry,
                                     @RequestParam(value = "status", required = false, defaultValue = "1") Integer status,
                                     @RequestParam(value = "minOrderValue", required = false, defaultValue = "0") Double minVal,
                                     HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        LocalDate expDate = (expiry != null && !expiry.isEmpty()) ? LocalDate.parse(expiry) : null;

        CouponRequest req = CouponRequest.builder()
                .couponId(id)
                .code(code)
                .discountPercent(percent)
                .expiryDate(expDate)
                .status(status)
                .minOrderValue(minVal)
                .build();

        if ("add".equalsIgnoreCase(action)) {
            couponService.insertCoupon(req);
            session.setAttribute("msgSuccess", "Thêm mã giảm giá thành công!");
        } else if ("update".equalsIgnoreCase(action)) {
            couponService.updateCoupon(req);
            session.setAttribute("msgSuccess", "Cập nhật mã giảm giá thành công!");
        } else if ("generate".equalsIgnoreCase(action)) {
            String gen = couponService.generateCoupon();
            session.setAttribute("msgSuccess", "Đã sinh mã ngẫu nhiên: " + gen);
        }
        return "redirect:/ManageCoupon";
    }

    @GetMapping("/ManageCoupon/delete")
    public String deleteCoupon(@RequestParam("id") Integer id, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        couponService.deleteCoupon(id);
        session.setAttribute("msgSuccess", "Đã xóa mã giảm giá!");
        return "redirect:/ManageCoupon";
    }
}
