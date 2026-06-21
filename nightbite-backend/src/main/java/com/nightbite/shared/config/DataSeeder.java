package com.nightbite.shared.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nightbite.domain.Admin;
import com.nightbite.domain.Product;
import com.nightbite.domain.Shop;
import com.nightbite.domain.User;
import com.nightbite.domain.Order;
import com.nightbite.domain.OrderItem;
import com.nightbite.domain.Review;
import com.nightbite.domain.OrderStatus;
import com.nightbite.domain.PaymentMethod;
import com.nightbite.domain.PaymentStatus;
import com.nightbite.domain.ItemType;

import com.nightbite.infrastructure.persistence.AdminRepository;
import com.nightbite.infrastructure.persistence.ProductRepository;
import com.nightbite.infrastructure.persistence.ShopRepository;
import com.nightbite.infrastructure.persistence.UserRepository;
import com.nightbite.infrastructure.persistence.OrderRepository;
import com.nightbite.infrastructure.persistence.OrderItemRepository;
import com.nightbite.infrastructure.persistence.ReviewRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Lớp khởi tạo dữ liệu mẫu ban đầu (Data Seeder) cho hệ thống NightBite[cite: 1].
 * Tự động chạy khi ứng dụng Spring Boot khởi động thành công.
 */
@Component
@RequiredArgsConstructor
@Transactional
public class DataSeeder implements ApplicationRunner {

    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    // Bổ sung các Repository phục vụ cho việc tạo dữ liệu mẫu phân hệ Đơn hàng & Đánh giá (Task của Nhã)
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(ApplicationArguments args) {
        seedUsers();
        seedAdmin();
        seedShopsAndProducts();
        seedOrdersAndReviews(); // Kích hoạt hàm đổ dữ liệu Đơn hàng và Đánh giá mẫu
    }

    private void seedUsers() {
        if (userRepository.count() > 0) {
            return;
        }

        List<User> users = List.of(
                createUser("ZALO-USER-001", "Tran Minh Anh", "0901000001", "anh1@nightbite.vn"),
                createUser("ZALO-USER-002", "Le Hoang Nam", "0901000002", "nam2@nightbite.vn"),
                createUser("ZALO-USER-003", "Pham Ngoc Ha", "0901000003", "ha3@nightbite.vn"),
                createUser("ZALO-USER-004", "Vo Quoc Bao", "0901000004", "bao4@nightbite.vn"),
                createUser("ZALO-USER-005", "Nguyen Thuy Linh", "0901000005", "linh5@nightbite.vn"));

        userRepository.saveAll(users);
    }

    private User createUser(String zaloId, String fullName, String phone, String email) {
        return User.builder()
                .zaloId(zaloId)
                .fullName(fullName)
                .phone(phone)
                .email(email)
                .passwordHash(passwordEncoder.encode("Password123!"))
                .avatarUrl(null)
                .trustScore(100)
                .isActive(true)
                .build();
    }

    private void seedAdmin() {
        if (adminRepository.count() > 0) {
            return;
        }

        Admin admin = Admin.builder()
                .username("admin")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .fullName("NightBite Admin")
                .email("admin@nightbite.vn")
                .build();

        adminRepository.save(admin);
    }

    private void seedShopsAndProducts() {
        if (shopRepository.count() == 0) {
            List<Shop> shops = new ArrayList<>();
            shops.add(createShop("Banh Mi Nha Nha", "Nguyen Van A", "0912000001", "banhmi@nightbite.vn", "1 Le Loi, Quan 1", "Quan 1", "Banh mi va do an nhanh", "https://cdn.nightbite.vn/logo-banhmi.png", "https://cdn.nightbite.vn/banner-banhmi.png"));
            shops.add(createShop("Tra Sua Mint", "Tran Thi B", "0912000002", "trasua@nightbite.vn", "99 Nguyen Trai, Quan 5", "Quan 5", "Tra sua va do uong manh", "https://cdn.nightbite.vn/logo-trasua.png", "https://cdn.nightbite.vn/banner-trasua.png"));
            shops.add(createShop("Com Ga Thu Duc", "Le Van C", "0912000003", "comga@nightbite.vn", "12 Vo Van Ngan, Thu Duc", "Thu Duc", "Com trua gia tot", "https://cdn.nightbite.vn/logo-comga.png", "https://cdn.nightbite.vn/banner-comga.png"));
            shops.add(createShop("Sweet Corner", "Pham Thi D", "0912000004", "sweet@nightbite.vn", "45 Phan Xich Long, Phu Nhuan", "Phu Nhuan", "Ban ngot cuoi ngay", "https://cdn.nightbite.vn/logo-sweet.png", "https://cdn.nightbite.vn/banner-sweet.png"));
            shops.add(createShop("Healthy Bowl", "Do Van E", "0912000005", "healthy@nightbite.vn", "67 Nguyen Oanh, Go Vap", "Go Vap", "Do an healthy", "https://cdn.nightbite.vn/logo-healthy.png", "https://cdn.nightbite.vn/banner-healthy.png"));
            shopRepository.saveAll(shops);
        }

        if (productRepository.count() == 0) {
            List<Shop> shops = shopRepository.findAll();
            if (shops.isEmpty()) {
                return;
            }

            List<Product> products = new ArrayList<>();
            LocalDate today = LocalDate.now();
            LocalTime start = LocalTime.of(0, 0);
            LocalTime end = LocalTime.of(23, 59, 59);

            products.add(createProduct(shops.get(0), "Banh mi thit nguoi", "Banh mi nong moi", "https://cdn.nightbite.vn/product-banhmi-1.png", 40000, 22000, 20, "thit,gluten", "Banh mi", today, start, end));
            products.add(createProduct(shops.get(0), "Banh mi xiu mai", "Dam vi truyen thong", "https://cdn.nightbite.vn/product-banhmi-2.png", 45000, 25000, 18, "thit,gluten", "Banh mi", today, start, end));
            products.add(createProduct(shops.get(0), "Banh mi chay", "Phu hop an nhanh", "https://cdn.nightbite.vn/product-banhmi-3.png", 30000, 18000, 25, "gluten", "Banh mi", today, start, end));

            products.add(createProduct(shops.get(1), "Tra sua truyen thong", "Ngon va beo", "https://cdn.nightbite.vn/product-trasua-1.png", 55000, 32000, 30, "sua", "Tra sua", today, start, end));
            products.add(createProduct(shops.get(1), "Tra sua matcha", "Huong matcha nhe", "https://cdn.nightbite.vn/product-trasua-2.png", 65000, 36000, 22, "sua,matcha", "Tra sua", today, start, end));
            products.add(createProduct(shops.get(1), "Tra dao", "Thanh mat", "https://cdn.nightbite.vn/product-trasua-3.png", 50000, 28000, 24, "trai cay", "Do uong", today, start, end));

            products.add(createProduct(shops.get(2), "Com ga nuong", "Com nong giao ngay", "https://cdn.nightbite.vn/product-comga-1.png", 70000, 42000, 16, "trung", "Com", today, start, end));
            products.add(createProduct(shops.get(2), "Com ga xoi mo", "Dam da", "https://cdn.nightbite.vn/product-comga-2.png", 80000, 48000, 14, "trung", "Com", today, start, end));
            products.add(createProduct(shops.get(2), "Com suon non", "Phan com day dan", "https://cdn.nightbite.vn/product-comga-3.png", 75000, 45000, 12, "thit", "Com", today, start, end));

            products.add(createProduct(shops.get(3), "Tiramisu slice", "Banh ngot mix", "https://cdn.nightbite.vn/product-sweet-1.png", 60000, 33000, 15, "sua,trung", "Banh ngot", today, start, end));
            products.add(createProduct(shops.get(3), "Croissant bo", "Ban trong ngay", "https://cdn.nightbite.vn/product-sweet-2.png", 52000, 29000, 17, "bo,gluten", "Banh ngot", today, start, end));
            products.add(createProduct(shops.get(4), "Chicken bowl", "Healthy meal", "https://cdn.nightbite.vn/product-healthy-1.png", 90000, 50000, 10, "thit ga", "Healthy", today, start, end));

            productRepository.saveAll(products);
        }
    }

    /**
     * Khởi tạo dữ liệu mẫu cho phân hệ Đơn hàng (Orders), Chi tiết đơn hàng (Order Items)
     * và Đánh giá phản hồi (Reviews) thuộc quyền xử lý của Nhã (Sprint 2 & 3)[cite: 1].
     */
    private void seedOrdersAndReviews() {
        if (orderRepository.count() > 0) {
            return; // Nếu đã có dữ liệu đơn hàng thì không thực hiện đổ thêm
        }

        List<User> users = userRepository.findAll();
        List<Shop> shops = shopRepository.findAll();
        List<Product> products = productRepository.findAll();

        if (users.isEmpty() || shops.isEmpty() || products.isEmpty()) {
            return;
        }

        // --- ĐƠN HÀNG 1: Trạng thái hoàn thành (DONE), đã thanh toán, có kèm đánh giá review ---
        Order order1 = Order.builder()
                .user(users.get(0)) // Người mua: Tran Minh Anh
                .shop(shops.get(0)) // Quán: Banh Mi Nha Nha
                .orderCode("NB-20260619-00001")
                .status(OrderStatus.DONE)
                .totalAmount(BigDecimal.valueOf(47000, 2)) // 470.00 theo cách định giá của hàm mẫu
                .pickupTime(LocalDateTime.now().minusHours(2))
                .note("Gói kỹ giúp mình nhé")
                .paymentMethod(PaymentMethod.ZALOPAY)
                .paymentStatus(PaymentStatus.PAID)
                .build();
        orderRepository.save(order1);

        // Chi tiết các món của đơn hàng 1
        OrderItem item1_1 = OrderItem.builder()
                .order(order1)
                .itemType(ItemType.PRODUCT)
                .product(products.get(0)) // Banh mi thit nguoi
                .itemName(products.get(0).getName())
                .unitPrice(products.get(0).getSalePrice())
                .quantity(1)
                .subtotal(products.get(0).getSalePrice())
                .build();

        OrderItem item1_2 = OrderItem.builder()
                .order(order1)
                .itemType(ItemType.PRODUCT)
                .product(products.get(1)) // Banh mi xiu mai
                .itemName(products.get(1).getName())
                .unitPrice(products.get(1).getSalePrice())
                .quantity(1)
                .subtotal(products.get(1).getSalePrice())
                .build();
        orderItemRepository.saveAll(List.of(item1_1, item1_2));

        // Tạo bài Đánh giá mẫu cho Đơn hàng 1 (Task BE-20)[cite: 1]
        Review review1 = Review.builder()
                .order(order1)
                .user(users.get(0))
                .shop(shops.get(0))
                .rating(5)
                .comment("Bánh mì ngon, nóng hổi dù mua vào cuối ngày, chủ quán rất nhiệt tình sản phẩm chất lượng!")
                .imageUrls("https://cdn.nightbite.vn/review-banhmi.png")
                .isVisible(true)
                .build();
        reviewRepository.save(review1);

        // Cập nhật điểm và số lượt đánh giá của Quán số 1 tương ứng dữ liệu review mẫu
        Shop shop1 = shops.get(0);
        shop1.setReviewCount(1);
        shop1.setRatingAvg(new BigDecimal("5.00"));
        shopRepository.save(shop1);


        // --- ĐƠN HÀNG 2: Đơn mới đang chờ xử lý (PENDING) ---
        Order order2 = Order.builder()
                .user(users.get(1)) // Người mua: Le Hoang Nam
                .shop(shops.get(1)) // Quán: Tra Sua Mint
                .orderCode("NB-20260619-00002")
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(32000, 2)) // 320.00
                .pickupTime(LocalDateTime.now().plusHours(1))
                .note("Ít đường ít đá")
                .paymentMethod(PaymentMethod.MOCK)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        orderRepository.save(order2);

        OrderItem item2_1 = OrderItem.builder()
                .order(order2)
                .itemType(ItemType.PRODUCT)
                .product(products.get(3)) // Tra sua truyen thong
                .itemName(products.get(3).getName())
                .unitPrice(products.get(3).getSalePrice())
                .quantity(1)
                .subtotal(products.get(3).getSalePrice())
                .build();
        orderItemRepository.save(item2_1);


        // --- ĐƠN HÀNG 3: Đơn hàng bị hủy (CANCELLED) ---
        Order order3 = Order.builder()
                .user(users.get(2)) // Người mua: Pham Ngoc Ha
                .shop(shops.get(2)) // Quán: Com Ga Thu Duc
                .orderCode("NB-20260619-00003")
                .status(OrderStatus.CANCELLED)
                .totalAmount(BigDecimal.valueOf(42000, 2)) // 420.00
                .pickupTime(LocalDateTime.now().minusDays(1))
                .note("Giao nhanh")
                .paymentMethod(PaymentMethod.CASH)
                .paymentStatus(PaymentStatus.FAILED)
                .cancelReason("Khách hàng không đến nhận món đúng giờ hẹn (Boom hàng)")
                .build();
        orderRepository.save(order3);

        OrderItem item3_1 = OrderItem.builder()
                .order(order3)
                .itemType(ItemType.PRODUCT)
                .product(products.get(6)) // Com ga nuong
                .itemName(products.get(6).getName())
                .unitPrice(products.get(6).getSalePrice())
                .quantity(1)
                .subtotal(products.get(6).getSalePrice())
                .build();
        orderItemRepository.save(item3_1);
    }

    private Shop createShop(String shopName, String ownerName, String phone, String email, String address, String district, String description, String logoUrl, String bannerUrl) {
        return Shop.builder()
                .shopName(shopName)
                .ownerName(ownerName)
                .phone(phone)
                .email(email)
                .passwordHash(passwordEncoder.encode("Shop@123"))
                .address(address)
                .district(district)
                .description(description)
                .logoUrl(logoUrl)
                .bannerUrl(bannerUrl)
                .isActive(true)
                .ratingAvg(new BigDecimal("0.00"))
                .reviewCount(0)
                .build();
    }

    private Product createProduct(Shop shop, String name, String description, String imageUrl, int originalPrice, int salePrice, int quantityAvailable,
                                  String allergenTags, String category, LocalDate saleDate, LocalTime saleStartTime, LocalTime saleEndTime) {
        return Product.builder()
                .shop(shop)
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .originalPrice(BigDecimal.valueOf(originalPrice, 2))
                .salePrice(BigDecimal.valueOf(salePrice, 2))
                .quantityAvailable(quantityAvailable)
                .quantitySold(0)
                .saleStartTime(saleStartTime)
                .saleEndTime(saleEndTime)
                .saleDate(saleDate)
                .allergenTags(allergenTags)
                .category(category)
                .isActive(true)
                .build();
    }
}