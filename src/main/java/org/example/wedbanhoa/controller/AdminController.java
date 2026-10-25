package org.example.wedbanhoa.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.wedbanhoa.dto.*;
import org.example.wedbanhoa.entity.Account;
import org.example.wedbanhoa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private org.example.wedbanhoa.service.BankSettingService bankSettingService;

    // Kiểm tra nhân sự có quyền vào trang quản trị (Admin: 1, Manager: 3, Employee: 4)
    private boolean isStaffOrAdmin(HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        if (user == null || user.getRoleId() == null) return false;
        int r = user.getRoleId();
        return r == 1 || r == 3 || r == 4;
    }

    // Kiểm tra DUY NHẤT quyền Admin tối cao (1)
    private boolean isStrictAdmin(HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        return user != null && user.getRoleId() != null && user.getRoleId() == 1;
    }

    private boolean checkAdmin(HttpSession session) {
        return isStaffOrAdmin(session);
    }

    // ==========================================
    // 1. DASHBOARD & THỐNG KÊ
    // ==========================================
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

        // Bổ sung thống kê trạng thái đơn hàng & bán chạy & biểu đồ doanh thu theo tháng
        Map<String, Long> statusCounts = orderService.getOrderStatusCounts();
        model.addAttribute("statusCounts", statusCounts);
        model.addAttribute("bestSellers", productService.getBestSellerProducts(5));

        int currentYear = java.time.LocalDate.now().getYear();
        List<Double> monthlyRevenues = orderService.getMonthlyRevenues(currentYear);
        model.addAttribute("monthlyRevenues", monthlyRevenues);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("bankSetting", bankSettingService.getBankSetting());

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

    // ==========================================
    // 2. QUẢN LÝ SẢN PHẨM (CRUD)
    // ==========================================
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

    @PostMapping({"/deleteProduct", "/DeleteProduct"})
    public String deleteProduct(@RequestParam("pid") Integer pid,
                                @RequestParam(value = "from", defaultValue = "ManagerProduct") String from,
                                HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        boolean ok = productService.deleteProduct(pid);
        if (ok) {
            session.setAttribute("msgSuccess", "Đã xóa sản phẩm thành công!");
        } else {
            session.setAttribute("msgError", "Không thể xóa sản phẩm do có ràng buộc dữ liệu đơn hàng!");
        }
        return "redirect:/" + ("admin".equalsIgnoreCase(from) ? "AdminDashboard" : "ManagerProduct");
    }

    // ==========================================
    // 3. QUẢN LÝ DANH MỤC
    // ==========================================
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

    // ==========================================
    // 4. QUẢN LÝ ĐƠN HÀNG & DUYỆT THANH TOÁN (COD & BANK TRANSFER)
    // ==========================================
    @GetMapping("/ManageOrder")
    public String manageOrder(@RequestParam(value = "status", required = false) String statusFilter,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);

        int pageSize = 10;
        Page<OderResponse> orderPage = orderService.getAllOrdersForAdminFiltered(statusFilter, page, pageSize);

        model.addAttribute("allOrders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", Math.max(1, orderPage.getTotalPages()));
        model.addAttribute("currentStatus", statusFilter != null ? statusFilter : "all");
        model.addAttribute("statusCounts", orderService.getOrderStatusCounts());
        model.addAttribute("pageUrlPattern", "ManageOrder?status=" + (statusFilter != null ? statusFilter : "all") + "&page=");

        return "view/manageOrders";
    }

    @PostMapping("/UpdateOrderStatus")
    public String updateOrderStatus(@RequestParam("orderId") Integer orderId,
                                    @RequestParam("status") String status,
                                    @RequestParam(value = "filterStatus", required = false) String filterStatus,
                                    HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        orderService.updateStatus(orderId, status);
        session.setAttribute("msgSuccess", "Cập nhật trạng thái đơn #" + orderId + " thành công!");
        return "redirect:/ManageOrder?status=" + (filterStatus != null ? filterStatus : "all");
    }

    // Duyệt nhanh thanh toán ngân hàng cho Admin
    @PostMapping("/Admin/ConfirmBankPayment")
    public String confirmBankPayment(@RequestParam("orderId") Integer orderId,
                                     @RequestParam(value = "from", defaultValue = "manage") String from,
                                     HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        boolean ok = orderService.confirmBankPayment(orderId);
        if (ok) {
            session.setAttribute("msgSuccess", "Đã xác nhận thanh toán chuyển khoản cho đơn #" + orderId + "! Đơn hàng chuyển sang Đang xử lý / giao hàng.");
        } else {
            session.setAttribute("msgError", "Không tìm thấy đơn hàng #" + orderId);
        }
        if ("detail".equalsIgnoreCase(from)) {
            return "redirect:/AdminOrderDetail?orderId=" + orderId;
        }
        return "redirect:/ManageOrder?status=Waiting Payment";
    }

    // Từ chối / Hủy đơn ngân hàng chưa thanh toán
    @PostMapping("/Admin/RejectBankPayment")
    public String rejectBankPayment(@RequestParam("orderId") Integer orderId,
                                    @RequestParam(value = "reason", required = false) String reason,
                                    @RequestParam(value = "from", defaultValue = "manage") String from,
                                    HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        orderService.rejectBankPayment(orderId, reason != null ? reason : "Không nhận được tiền chuyển khoản");
        session.setAttribute("msgSuccess", "Đã hủy đơn hàng #" + orderId + " do chưa hoàn tất thanh toán chuyển khoản.");
        if ("detail".equalsIgnoreCase(from)) {
            return "redirect:/AdminOrderDetail?orderId=" + orderId;
        }
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

    // ==========================================
    // 5. QUẢN LÝ KHÁCH HÀNG & PHÂN QUYỀN (USER MANAGEMENT)
    // ==========================================
    @GetMapping("/ManagerCustomer")
    public String managerCustomer(Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);
        List<AccountResponse> accounts = accountService.getAllAccounts();
        model.addAttribute("listC", accounts);
        return "view/ManagerCustomer";
    }

    // ==========================================
    // CẬP NHẬT THÔNG TIN NGÂN HÀNG (CHỈ ADMIN TỐI CAO)
    // ==========================================
    @PostMapping("/Admin/UpdateBankSetting")
    public String updateBankSetting(@RequestParam("bankName") String bankName,
                                    @RequestParam("accountNumber") String accountNumber,
                                    @RequestParam("accountHolder") String accountHolder,
                                    @RequestParam(value = "qrImageUrl", required = false) String qrImageUrl,
                                    HttpSession session) {
        if (!isStrictAdmin(session)) {
            session.setAttribute("msgError", "Chỉ tài khoản Admin tối cao mới có quyền cập nhật thông tin ngân hàng!");
            return "redirect:/AdminDashboard";
        }
        String qr = (qrImageUrl != null && !qrImageUrl.isBlank()) ? qrImageUrl.trim() : "thanhtoan.jpg";
        if (qr.startsWith("/images/")) {
            qr = qr.substring("/images/".length());
        }
        bankSettingService.updateBankSetting(bankName, accountNumber, accountHolder, qr);
        session.setAttribute("msgSuccess", "Cập nhật thông tin tài khoản ngân hàng thành công!");
        return "redirect:/AdminDashboard";
    }

    @PostMapping("/Admin/AddCustomer")
    public String addCustomerByAdmin(@RequestParam("fullname") String fullname,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     @RequestParam(value = "phone", required = false) String phone,
                                     @RequestParam(value = "address", required = false) String address,
                                     @RequestParam(value = "roleId", defaultValue = "4") Integer roleId,
                                     HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        if (!isStrictAdmin(session)) {
            session.setAttribute("msgError", "Chỉ tài khoản Admin mới có quyền tạo thêm tài khoản quản lý / nhân viên!");
            return "redirect:/ManagerCustomer";
        }

        if (roleId != null && roleId == 1) {
            session.setAttribute("msgError", "Hệ thống chỉ cho phép duy nhất 1 tài khoản Admin!");
            return "redirect:/ManagerCustomer";
        }

        if (roleId == null || (roleId != 3 && roleId != 4)) {
            roleId = 4; // Chỉ cho phép Manager (3) hoặc Employee (4)
        }

        AccountRequest req = AccountRequest.builder()
                .fullname(fullname)
                .email(email)
                .password(password)
                .phone(phone)
                .address(address)
                .roleId(roleId)
                .build();
        boolean created = accountService.createAccountByAdmin(req);
        if (created) {
            String roleName = roleId == 3 ? "Quản lý" : "Nhân viên";
            session.setAttribute("msgSuccess", "Tạo tài khoản " + roleName + " thành công!");
        } else {
            session.setAttribute("msgError", "Email này đã tồn tại trong hệ thống!");
        }
        return "redirect:/ManagerCustomer";
    }

    @PostMapping("/Admin/UpdateCustomer")
    public String updateCustomerByAdmin(@RequestParam("userId") Integer userId,
                                        @RequestParam("fullname") String fullname,
                                        @RequestParam(value = "phone", required = false) String phone,
                                        @RequestParam(value = "address", required = false) String address,
                                        @RequestParam("roleId") Integer roleId,
                                        @RequestParam(value = "password", required = false) String password,
                                        HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        if (!isStrictAdmin(session)) {
            session.setAttribute("msgError", "Chỉ tài khoản Admin mới có quyền cập nhật thông tin và vai trò nhân viên!");
            return "redirect:/ManagerCustomer";
        }

        Account existing = accountService.getEntityById(userId);
        if (existing == null) {
            session.setAttribute("msgError", "Không tìm thấy tài khoản để cập nhật!");
            return "redirect:/ManagerCustomer";
        }

        // Nếu tài khoản hiện tại không phải Admin nhưng cố tình đổi thành Admin
        if (existing.getRoleId() != null && existing.getRoleId() != 1 && roleId != null && roleId == 1) {
            session.setAttribute("msgError", "Không thể phân quyền Admin cho tài khoản khác! Hệ thống chỉ có 1 Admin duy nhất.");
            return "redirect:/ManagerCustomer";
        }

        boolean updated = accountService.updateAccountByAdmin(userId, fullname, phone, address, roleId, password);
        if (updated) {
            session.setAttribute("msgSuccess", "Cập nhật tài khoản #" + userId + " và phân quyền thành công!");
        } else {
            session.setAttribute("msgError", "Không thể cập nhật tài khoản!");
        }
        return "redirect:/ManagerCustomer";
    }

    @PostMapping("/Admin/DeleteCustomer")
    public String deleteCustomerByAdmin(@RequestParam("userId") Integer userId, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        if (!isStrictAdmin(session)) {
            session.setAttribute("msgError", "Chỉ tài khoản Admin mới có quyền xóa tài khoản!");
            return "redirect:/ManagerCustomer";
        }

        Account currentAdmin = (Account) session.getAttribute("userSession");
        if (currentAdmin != null && currentAdmin.getUserId().equals(userId)) {
            session.setAttribute("msgError", "Không thể tự xóa tài khoản đang đăng nhập!");
            return "redirect:/ManagerCustomer";
        }

        Account target = accountService.getEntityById(userId);
        if (target != null && target.getRoleId() != null && target.getRoleId() == 1) {
            session.setAttribute("msgError", "Không thể xóa tài khoản Admin tối cao của hệ thống!");
            return "redirect:/ManagerCustomer";
        }

        try {
            boolean deleted = accountService.deleteAccount(userId);
            if (deleted) {
                session.setAttribute("msgSuccess", "Đã xóa tài khoản #" + userId + " thành công!");
            } else {
                session.setAttribute("msgError", "Không tìm thấy tài khoản để xóa!");
            }
        } catch (Exception e) {
            session.setAttribute("msgError", "Không thể xóa tài khoản này vì đã có lịch sử đơn hàng hoặc đánh giá liên kết!");
        }
        return "redirect:/ManagerCustomer";
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
        List<ReviewResponse> customerReviews = reviewService.getReviewsByUserId(id);

        model.addAttribute("customer", customer);
        model.addAttribute("activeTab", tab);
        model.addAttribute("orders", customerOrders);
        model.addAttribute("reviews", customerReviews);
        return "view/customerDetail";
    }

    // ==========================================
    // 6. QUẢN LÝ ĐÁNH GIÁ SẢN PHẨM (REVIEWS)
    // ==========================================
    @GetMapping("/AdminProductReviews")
    public String productReviews(@RequestParam("pid") Integer pid, Model model, HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        setHeaderData(model, session);

        ProductResponse product = productService.getProductById(pid);
        if (product == null) return "redirect:/ManagerProduct";

        List<ReviewResponse> reviews = reviewService.getReviewsByProductId(pid);

        model.addAttribute("productDetail", product);
        model.addAttribute("listReviews", reviews);
        return "view/AdminProductReviews";
    }

    @PostMapping("/Admin/DeleteReview")
    public String deleteReview(@RequestParam("reviewId") Integer reviewId,
                               @RequestParam("pid") Integer pid,
                               HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/Login";
        reviewService.deleteReview(reviewId);
        session.setAttribute("msgSuccess", "Đã xóa đánh giá thành công!");
        return "redirect:/AdminProductReviews?pid=" + pid;
    }

    // ==========================================
    // 7. QUẢN LÝ MÃ GIẢM GIÁ (COUPONS)
    // ==========================================
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
