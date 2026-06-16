## [2026-06-16 10:05]

### Task
Hoan thanh toan bo Sprint 1: seed data, Shop API public, va cau hinh response wrapper cho NightBite.

### Files Changed
- pom.xml
- src/main/resources/application.properties
- src/main/java/com/nightbite/shared/utils/ApiResponse.java
- src/main/java/com/nightbite/shared/exception/ResourceNotFoundException.java
- src/main/java/com/nightbite/shared/exception/BadRequestException.java
- src/main/java/com/nightbite/shared/exception/GlobalExceptionHandler.java
- src/main/java/com/nightbite/shared/config/DataSeeder.java
- src/main/java/com/nightbite/infrastructure/persistence/ShopRepository.java
- src/main/java/com/nightbite/infrastructure/persistence/ProductRepository.java
- src/main/java/com/nightbite/infrastructure/persistence/ReviewRepository.java
- src/main/java/com/nightbite/infrastructure/persistence/UserRepository.java
- src/main/java/com/nightbite/infrastructure/persistence/AdminRepository.java
- src/main/java/com/nightbite/features/shop/ShopRequest.java
- src/main/java/com/nightbite/features/shop/ShopDto.java
- src/main/java/com/nightbite/features/shop/ShopProductDto.java
- src/main/java/com/nightbite/features/shop/ShopReviewDto.java
- src/main/java/com/nightbite/features/shop/ShopService.java
- src/main/java/com/nightbite/features/shop/ShopController.java

### Implementation
- Bo sung `spring-boot-starter-validation` va dat `server.servlet.context-path=/savibite`.
- Tao `ApiResponse<T>` va exception handler de dong nhat response format.
- Tao repository cho Shop/Product/Review/User/Admin.
- Tao DTO, service, controller cho cac API BE-03 va BE-04.
- Tao `DataSeeder` de nap 5 users, 1 admin, 5 shops, va 12 products mau.

### Notes
- Da verify compile bang `mvnw.cmd -DskipTests compile` voi JDK 21.
- Da verify test context load bang `mvnw.cmd test`; Spring Boot khoi dong thanh cong va seed data chay on dinh.

### Status
Completed
## [2026-06-16 09:52]

### Task
Tao 9 JPA entity noi dung BE-01 theo schema NightBite va chuan bi cac enum lien quan.

### Files Changed
- src/main/java/com/nightbite/domain/Role.java
- src/main/java/com/nightbite/domain/BoxType.java
- src/main/java/com/nightbite/domain/ItemType.java
- src/main/java/com/nightbite/domain/OrderStatus.java
- src/main/java/com/nightbite/domain/PaymentMethod.java
- src/main/java/com/nightbite/domain/PaymentStatus.java
- src/main/java/com/nightbite/domain/User.java
- src/main/java/com/nightbite/domain/Shop.java
- src/main/java/com/nightbite/domain/Admin.java
- src/main/java/com/nightbite/domain/Product.java
- src/main/java/com/nightbite/domain/NightBiteBox.java
- src/main/java/com/nightbite/domain/Order.java
- src/main/java/com/nightbite/domain/OrderItem.java
- src/main/java/com/nightbite/domain/Review.java

### Implementation
- Tao cac enum dung chung cho role, box type, item type, order status, payment method, payment status.
- Tao 9 entity voi Lombok, JPA mapping, default value, precision/scale, va timestamp audit dung theo tai lieu.
- Cau hinh relation ManyToOne, OneToOne va unique constraint can thiet cho schema.

### Notes
- Da verify bang `mvnw.cmd` voi JDK 21 va project compile thanh cong.
- Test context-load chay duoc voi MySQL local hien co; van con can DB local dung cau hinh de phat trien tiep.

### Status
Completed

