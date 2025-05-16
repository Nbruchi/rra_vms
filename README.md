# Potential Issues Preventing Application Startup

## 1. Spring Boot Version Incompatibility
**Issue:** The Spring Boot version in pom.xml (3.4.5) is too high and not yet released.
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.4.5</version>
    <relativePath/>
</parent>
```
**Correction:** Downgrade to a stable released version like 3.2.0 or 3.1.0:
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
    <relativePath/>
</parent>
```

## 2. OpenAPI Dependency Incompatibility
**Issue:** The springdoc-openapi-ui dependency is not compatible with Spring Boot 3.x:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.8.0</version>
</dependency>
```
**Correction:** Replace with the Spring Boot 3 compatible version:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

## 3. JWT Token Generation Issue
**Issue:** In JwtService.java, the method `expiration()` is used instead of `setExpiration()`:
```java
.expiration(new Date(System.currentTimeMillis() + expirationTime))
```
**Correction:** Change to `setExpiration()`:
```java
.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
```

## 4. Incorrect Role Format in CustomUserDetailsService
**Issue:** In CustomUserDetailsService.java, the role is incorrectly prefixed with "ROLE_":
```java
.roles("ROLE_STANDARD")
```
**Correction:** Remove the "ROLE_" prefix as Spring Security automatically adds it:
```java
.roles("STANDARD")
```

## 5. Missing Role Assignment in User Registration
**Issue:** In UserService.java, when registering a new user, the role is not set:
```java
User user = new User(request.email());
user.setPassword(passwordEncoder.encode(request.password()));
```
**Correction:** Add role assignment:
```java
User user = new User(request.email());
user.setPassword(passwordEncoder.encode(request.password()));
user.setRole(Role.STANDARD); // Add this line
```

## 6. Incomplete User Model Default Methods
**Issue:** In User.java, the UserDetails interface methods return the default values:
```java
@Override
public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
}
```
**Correction:** These methods should return true explicitly:
```java
@Override
public boolean isAccountNonExpired() {
    return true;
}
```

## 7. Missing Database Driver
**Issue:** If PostgreSQL is not installed or configured properly, the application will fail to start.

**Correction:** Ensure PostgreSQL is installed and running, or modify application.properties to use an in-memory database for development:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```
And add H2 dependency:
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

## 8. Potential Circular Dependency
**Issue:** There might be a circular dependency between UserRepo, JwtAuthFilter, and CustomUserDetailsService.

**Correction:** Consider using constructor injection with @Lazy annotation:
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    public CustomUserDetailsService(@Lazy UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    // ...
}
```

## 9. Missing Message Pattern in PlateNumber Validation
**Issue:** The @Pattern annotation in PlateNumber.java is missing a message attribute:
```java
@Pattern(regexp = "^RA[A-Z]\\d{3}[A-Z]$")
```
**Correction:** Add a message attribute:
```java
@Pattern(regexp = "^RA[A-Z]\\d{3}[A-Z]$", message = "Invalid plate number format")
```

## 10. Potential Null Pointer in Vehicle Entity
**Issue:** The Vehicle entity has a OneToMany relationship with TransferHistory that could be null:
```java
@OneToMany(mappedBy = "vehicle")
private List<TransferHistory> transfers;
```
**Correction:** Initialize the list to avoid NullPointerException:
```java
@OneToMany(mappedBy = "vehicle")
private List<TransferHistory> transfers = new ArrayList<>();
```
And add the import:
```java
import java.util.ArrayList;
```