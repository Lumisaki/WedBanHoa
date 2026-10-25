package org.example.wedbanhoa.controller;

import jakarta.servlet.http.HttpSession;
import org.example.wedbanhoa.dto.AccountRequest;
import org.example.wedbanhoa.dto.AccountResponse;
import org.example.wedbanhoa.dto.OderResponse;
import org.example.wedbanhoa.entity.Account;
import org.example.wedbanhoa.service.AccountService;
import org.example.wedbanhoa.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AuthController extends BaseController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/Login")
    public String loginPage(Model model, HttpSession session) {
        setHeaderData(model, session);
        String successMsg = (String) session.getAttribute("successMsg");
        if (successMsg != null) {
            model.addAttribute("successMsg", successMsg);
            session.removeAttribute("successMsg");
        }
        session.removeAttribute("errorMsg");
        session.removeAttribute("error");
        return "view/login";
    }

    @PostMapping("/Login")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               HttpSession session, Model model) {
        Account userEntity = accountService.loginEntity(email, password);
        if (userEntity != null) {
            session.setAttribute("userSession", userEntity);
            if (userEntity.getRoleId() != null && userEntity.getRoleId() == 1) {
                return "redirect:/AdminDashboard";
            }
            return "redirect:/index";
        }
        setHeaderData(model, session);
        model.addAttribute("error", "Sai email hoặc mật khẩu!");
        return "view/login";
    }

    @GetMapping({"/Register", "/register"})
    public String registerPage(Model model, HttpSession session) {
        setHeaderData(model, session);
        return "view/register";
    }

    @PostMapping({"/Register", "/register"})
    public String processRegister(@RequestParam("fullname") String fullname,
                                  @RequestParam("email") String email,
                                  @RequestParam("password") String password,
                                  @RequestParam(value = "phone", required = false) String phone,
                                  @RequestParam(value = "address", required = false) String address,
                                  @RequestParam(value = "question", required = false) String question,
                                  @RequestParam(value = "answer", required = false) String answer,
                                  HttpSession session, Model model) {
        AccountRequest request = AccountRequest.builder()
                .fullname(fullname)
                .email(email)
                .password(password)
                .phone(phone)
                .address(address)
                .securityQuestion(question)
                .securityAnswer(answer)
                .roleId(2)
                .build();

        if (accountService.register(request)) {
            session.setAttribute("successMsg", "Chúc mừng bạn đã tạo tài khoản thành công! Hãy đăng nhập để mua hoa nhé.");
            return "redirect:/Login";
        }
        setHeaderData(model, session);
        model.addAttribute("error", "Email này đã được sử dụng! Vui lòng dùng email khác.");
        return "view/register";
    }

    @GetMapping("/Logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/Login";
    }

    @PostMapping("/api/clear-session-msg")
    @org.springframework.web.bind.annotation.ResponseBody
    public java.util.Map<String, String> clearSessionMsg(HttpSession session) {
        if (session != null) {
            String[] flashKeys = {"msgSuccess", "msgError", "errorMsg", "successMsg", "sweetSuccess", "sweetError", "reviewMsg", "message", "error"};
            for (String key : flashKeys) {
                session.removeAttribute(key);
            }
        }
        return java.util.Map.of("status", "cleared");
    }

    @GetMapping("/profilm")
    public String profile(Model model, HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        if (user == null) return "redirect:/Login";
        setHeaderData(model, session);

        AccountResponse response = accountService.getById(user.getUserId());
        model.addAttribute("user", response);

        Double totalSpent = orderService.getTotalSpentByUser(user.getUserId());
        model.addAttribute("totalMoney", totalSpent != null ? totalSpent : 0.0);

        List<OderResponse> orders = orderService.getOrderHistoryByUserId(user.getUserId());
        model.addAttribute("listOrder", orders);

        return "view/profilm";
    }

    @PostMapping("/UpdateProfile")
    public String updateProfile(@RequestParam("fullname") String fullname,
                                @RequestParam("phone") String phone,
                                @RequestParam("address") String address,
                                HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        if (user == null) return "redirect:/Login";

        AccountRequest req = AccountRequest.builder()
                .userId(user.getUserId())
                .fullname(fullname)
                .phone(phone)
                .address(address)
                .build();

        AccountResponse updated = accountService.updateProfile(req);
        if (updated != null) {
            user.setFullname(updated.getFullname());
            user.setPhone(updated.getPhone());
            user.setAddress(updated.getAddress());
            session.setAttribute("userSession", user);
            session.setAttribute("message", "Cập nhật hồ sơ thành công!");
        } else {
            session.setAttribute("error", "Cập nhật hồ sơ thất bại!");
        }
        return "redirect:/profilm";
    }

    @PostMapping("/ChangePassword")
    public String changePassword(@RequestParam("oldPass") String oldPass,
                                 @RequestParam("newPass") String newPass,
                                 @RequestParam("reNewPass") String reNewPass,
                                 HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        if (user == null) return "redirect:/Login";

        if (!newPass.equals(reNewPass)) {
            session.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/profilm";
        }

        boolean ok = accountService.changePassword(user.getUserId(), oldPass, newPass);
        if (ok) {
            session.setAttribute("message", "Đổi mật khẩu thành công!");
        } else {
            session.setAttribute("error", "Mật khẩu hiện tại không chính xác!");
        }
        return "redirect:/profilm";
    }

    @GetMapping("/forgot")
    public String forgotPasswordPage(Model model, HttpSession session) {
        setHeaderData(model, session);
        return "view/forgotpassword";
    }

    @PostMapping("/forgot")
    public String processForgotPassword(@RequestParam("email") String email,
                                        @RequestParam("answer") String answer,
                                        @RequestParam("newPassword") String newPassword,
                                        Model model, HttpSession session) {
        setHeaderData(model, session);
        boolean ok = accountService.resetPassword(email, answer, newPassword);
        if (ok) {
            model.addAttribute("msg", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập lại.");
        } else {
            model.addAttribute("error", "Email hoặc câu trả lời bảo mật không chính xác!");
        }
        return "view/forgotpassword";
    }
}
