package org.example.wedbanhoa.service;

import org.example.wedbanhoa.dto.ProductRequest;
import org.example.wedbanhoa.dto.ProductResponse;
import org.example.wedbanhoa.entity.Category;
import org.example.wedbanhoa.entity.Product;
import org.example.wedbanhoa.repository.CategoryRepository;
import org.example.wedbanhoa.repository.OrderDetailRepository;
import org.example.wedbanhoa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {

        List<Product> list = productRepository.findAll(Sort.by(Sort.Direction.DESC, "productId"));
        return list.stream().map(this::enrichProductResponse).toList();
    }
    @Transactional(readOnly = true)
    public List<ProductResponse> getHomeDiscountedProducts() {
        List<Product> list = productRepository.findByDiscountGreaterThan(0);
        populateSold(list);
        return list.stream().map(this::enrichProductResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockProducts(int threshold) {
        List<Product> list = productRepository.findAll().stream()
                .filter(p -> p.getStock() != null && p.getStock() <= threshold)
                .toList();
        populateSold(list);
        return list.stream().map(this::enrichProductResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getBestSellerProducts(int limit) {
        List<Product> list = productRepository.findAll();
        populateSold(list);
        return list.stream()
                .sorted((a, b) -> Integer.compare(b.getSold() != null ? b.getSold() : 0, a.getSold() != null ? a.getSold() : 0))
                .limit(limit)
                .map(this::enrichProductResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Integer id) {
        Product p = productRepository.findById(id).orElse(null);
        if (p == null) return null;
        Integer sold = orderDetailRepository.getSoldQuantityByProductId(p.getProductId());
        p.setSold(sold != null ? sold : 0);
        return enrichProductResponse(p);
    }

    @Transactional(readOnly = true)
    public ProductRequest getProductForEdit(Integer id) {
        Product p = productRepository.findById(id).orElse(null);
        if (p == null) return null;
        return toRequest(p); // Gọi hàm mapping nội bộ
    }

    @Transactional(readOnly = true)
    public Product getProductEntityById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public long getTotalProducts() {
        return productRepository.count();
    }

    @Transactional
    public ProductResponse saveProductFromRequest(ProductRequest request, MultipartFile imageFile) throws IOException {
        String imageName = handleImageUpload(imageFile, request.getOld_pimage());
        if (imageName != null) {
            request.setImage(imageName);
        }

        Product product;
        if (request.getProductId() != null) {
            product = productRepository.findById(request.getProductId())
                    .orElse(toEntity(request)); // Gọi hàm mapping nội bộ
            updateEntityFromRequest(product, request); // Gọi hàm cập nhật nội bộ
        } else {
            product = toEntity(request); // Gọi hàm mapping nội bộ
        }

        Product saved = productRepository.save(product);
        return enrichProductResponse(saved);
    }

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public boolean deleteProduct(Integer id) {
        try {
            productRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public Map<Integer, ProductResponse> getProductsForCart(Set<Integer> productIds) {
        Map<Integer, ProductResponse> map = new HashMap<>();
        if (productIds == null || productIds.isEmpty()) return map;
        List<Product> list = productRepository.findByProductIdIn(productIds);
        for (Product p : list) {
            map.put(p.getProductId(), enrichProductResponse(p));
        }
        return map;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchAndSortProductsPageable(String keyword, String categoryIdStr, String sortType, int page, int pageSize) {
        Integer categoryId = null;
        if (categoryIdStr != null && !categoryIdStr.isEmpty() && !"all".equalsIgnoreCase(categoryIdStr)) {
            try {
                categoryId = Integer.parseInt(categoryIdStr);
            } catch (NumberFormatException ignored) {}
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "productId");
        if ("price_asc".equalsIgnoreCase(sortType)) {
            sort = Sort.by(Sort.Direction.ASC, "price");
        } else if ("price_desc".equalsIgnoreCase(sortType)) {
            sort = Sort.by(Sort.Direction.DESC, "price");
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        Page<Product> productPage = productRepository.searchProductsPageable(keyword, categoryId, pageable);
        populateSold(productPage.getContent());

        List<ProductResponse> dtoList = productPage.getContent().stream()
                .map(this::enrichProductResponse)
                .toList();

        return new PageImpl<>(dtoList, pageable, productPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> searchAndSortProducts(String keyword, String categoryIdStr, String sortType) {
        Integer categoryId = null;
        if (categoryIdStr != null && !categoryIdStr.isEmpty() && !"all".equalsIgnoreCase(categoryIdStr)) {
            try {
                categoryId = Integer.parseInt(categoryIdStr);
            } catch (NumberFormatException ignored) {}
        }
        List<Product> list = productRepository.searchProducts(keyword, categoryId);
        populateSold(list);

        if ("bestseller".equalsIgnoreCase(sortType)) {
            list.sort((p1, p2) -> Integer.compare(p2.getSold() != null ? p2.getSold() : 0, p1.getSold() != null ? p1.getSold() : 0));
        } else if ("price_asc".equalsIgnoreCase(sortType)) {
            list.sort(Comparator.comparingDouble(p -> p.getPrice() * (1 - (p.getDiscount() != null ? p.getDiscount() : 0) / 100.0))); // Đã fix cú pháp
        } else if ("price_desc".equalsIgnoreCase(sortType)) {
            list.sort((p1, p2) -> Double.compare(
                    p2.getPrice() * (1 - (p2.getDiscount() != null ? p2.getDiscount() : 0) / 100.0), // Đã fix cú pháp
                    p1.getPrice() * (1 - (p1.getDiscount() != null ? p1.getDiscount() : 0) / 100.0))); // Đã fix cú pháp
        }

        return list.stream().map(this::enrichProductResponse).toList();
    }

    private void populateSold(List<Product> products) {
        if (products == null || products.isEmpty()) return;
        try {
            List<Object[]> soldList = orderDetailRepository.getAllSoldQuantities();
            Map<Integer, Integer> soldMap = new HashMap<>();
            for (Object[] row : soldList) {
                if (row[0] != null && row[1] != null) {
                    soldMap.put(((Number) row[0]).intValue(), ((Number) row[1]).intValue());
                }
            }
            for (Product p : products) {
                p.setSold(soldMap.getOrDefault(p.getProductId(), 0));
            }
        } catch (Exception e) {
            for (Product p : products) {
                if (p.getSold() == null) p.setSold(0);
            }
        }
    }

    private ProductResponse enrichProductResponse(Product p) {
        String catName = "";
        if (p.getCategory() != null) {
            catName = p.getCategory().getCategoryName();
        } else if (p.getCategoryId() != null) {
            catName = categoryRepository.findById(p.getCategoryId())
                    .map(Category::getCategoryName)
                    .orElse("");
        }
        return toResponse(p, catName); // Gọi hàm mapping nội bộ
    }

    private String handleImageUpload(MultipartFile file, String oldImage) throws IOException {
        if (file == null || file.isEmpty()) {
            return oldImage;
        }
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path uploadDir = Paths.get("src/main/resources/static/images");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path targetPath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }


    private Product toEntity(ProductRequest request) {
        if (request == null) return null;
        return Product.builder()
                .productId(request.getProductId())
                .productName(request.getProductName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .image(request.getImage())
                .categoryId(request.getCategoryId())
                .discount(request.getDiscount() != null ? request.getDiscount() : 0)
                .build();
    }

    private void updateEntityFromRequest(Product product, ProductRequest request) {
        if (product == null || request == null) return;
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            product.setImage(request.getImage());
        }
        product.setCategoryId(request.getCategoryId());
        product.setDiscount(request.getDiscount() != null ? request.getDiscount() : 0);
    }

    private ProductResponse toResponse(Product entity) {
        if (entity == null) return null;
        return ProductResponse.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .image(entity.getImage())
                .categoryId(entity.getCategoryId())
                .discount(entity.getDiscount())
                .sold(entity.getSold() != null ? entity.getSold() : 0)
                .quantity(entity.getQuantity())
                .build();
    }

    private ProductResponse toResponse(Product entity, String categoryName) {
        ProductResponse res = toResponse(entity);
        if (res != null) {
            res.setCategoryName(categoryName);
        }
        return res;
    }

    private ProductRequest toRequest(Product entity) {
        if (entity == null) return null;
        return ProductRequest.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .image(entity.getImage())
                .categoryId(entity.getCategoryId())
                .discount(entity.getDiscount())
                .build();
    }
}