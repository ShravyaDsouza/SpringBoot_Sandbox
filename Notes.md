# Spring Boot Notes

## Table of Contents

- [1. What Is Spring Boot?](#1-what-is-spring-boot)
- [2. Why Spring Boot Is Popular](#2-why-spring-boot-is-popular)
- [3. Spring Framework vs Spring Boot](#3-spring-framework-vs-spring-boot)
- [4. Core Spring Concepts](#4-core-spring-concepts)
- [5. Bean Lifecycle and Scope](#5-bean-lifecycle-and-scope)
- [6. Configuration in Spring](#6-configuration-in-spring)
- [7. Important Spring Boot Annotations](#7-important-spring-boot-annotations)
- [8. Auto-Configuration in Spring Boot](#8-auto-configuration-in-spring-boot)
- [9. Spring Boot Starters](#9-spring-boot-starters)
- [10. Layered Architecture in Spring Boot](#10-layered-architecture-in-spring-boot)
- [11. Spring MVC](#11-spring-mvc)
- [12. Aspect-Oriented Programming (AOP)](#12-aspect-oriented-programming-aop)
- [13. Event-Driven Communication in Spring](#13-event-driven-communication-in-spring)
- [14. Data Access in Spring](#14-data-access-in-spring)
- [15. Spring Data JPA and Hibernate](#15-spring-data-jpa-and-hibernate)
- [16. Hibernate Entity Lifecycle](#16-hibernate-entity-lifecycle)
- [17. Entity Relationships](#17-entity-relationships)
- [18. Transactions](#18-transactions)
- [19. Validation](#19-validation)
- [20. Spring Security](#20-spring-security)
- [21. OAuth2 and JWT](#21-oauth2-and-jwt)
- [22. Spring Boot Actuator](#22-spring-boot-actuator)
- [23. Micrometer](#23-micrometer)
- [24. Micrometer vs Eureka](#24-micrometer-vs-eureka)
- [25. Embedded Server](#25-embedded-server)
- [26. Task Execution and Scheduling](#26-task-execution-and-scheduling)
- [27. Testing in Spring Boot](#27-testing-in-spring-boot)
- [28. Spring Data Variants](#28-spring-data-variants)
- [29. Microservices with Spring](#29-microservices-with-spring)
- [30. Spring Cloud](#30-spring-cloud)
- [31. Common Best Practices](#31-common-best-practices)
- [32. What Changed in Spring Boot 3](#32-what-changed-in-spring-boot-3)
- [33. Quick Revision Summary](#33-quick-revision-summary)
- [34. Interview-Oriented Short Answers](#34-interview-oriented-short-answers)
- [35. Final Takeaway](#35-final-takeaway)
- [36. Advanced Spring Boot Notes (Modern Production Topics)](#36-advanced-spring-boot-notes-modern-production-topics)
- [37. Compact Interview Revision Sheet](#37-compact-interview-revision-sheet)
- [38. Common Pitfalls in Spring Boot](#38-common-pitfalls-in-spring-boot)

## 1. What Is Spring Boot?

Spring Boot is an opinionated framework built on top of the Spring Framework. It helps us create stand-alone, production-ready Java applications with minimal configuration.

Spring Boot reduces boilerplate by providing:

- auto-configuration
- embedded servers such as Tomcat, Jetty, or Undertow
- starter dependencies
- production-ready features through Actuator
- sensible defaults for common enterprise use cases

In simple terms:

- `Spring Framework` gives us the core building blocks
- `Spring Boot` makes those building blocks easier and faster to use in real applications

A typical Spring Boot application can often be started with a single entry point:

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

---

## 2. Why Spring Boot Is Popular

Spring Boot is widely used because it improves developer productivity while still allowing deep customization.

### Key advantages

- Rapid application development
- Reduced XML and boilerplate configuration
- Easy dependency management through starters
- Embedded web servers for easy deployment
- Strong ecosystem integration with Spring Data, Security, Cloud, and Testing
- Built-in observability and health endpoints
- Convention over configuration

### When to use Spring Boot

Spring Boot is a strong choice for:

- REST APIs
- enterprise backends
- microservices
- monoliths with layered architecture
- scheduled jobs and batch processing
- cloud-native applications

---

## 3. Spring Framework vs Spring Boot

| Topic | Spring Framework | Spring Boot |
| --- | --- | --- |
| Purpose | Core framework for Java enterprise development | Opinionated layer on top of Spring |
| Configuration | More manual configuration | Auto-configuration with defaults |
| Server setup | Usually external server setup | Embedded server support |
| Dependency management | Manual dependency selection | Starter dependencies |
| Production features | Need to assemble manually | Built-in Actuator and production tooling |

Spring Boot is not a replacement for Spring Framework. It is the easiest and most practical way to use Spring Framework.

---

## 4. Core Spring Concepts

### 4.1 Bean

A bean is an object created, managed, and lifecycle-controlled by the Spring container.

Example:

```java
@Component
public class UserService {
}
```

Here, `UserService` becomes a Spring-managed bean.

### 4.2 Inversion of Control (IoC)

Inversion of Control means the framework manages object creation and wiring instead of the programmer doing it manually.

Without Spring:

```java
UserRepository repo = new UserRepository();
UserService service = new UserService(repo);
```

With Spring, the container creates and connects these objects for us.

### 4.3 Dependency Injection (DI)

Dependency Injection is the mechanism through which Spring provides required dependencies to a bean.

Preferred approach: constructor injection.

```java
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### Types of dependency injection

- Constructor injection
- Setter injection
- Field injection

Constructor injection is preferred because it:

- makes dependencies explicit
- supports immutability
- simplifies testing
- avoids partially initialized objects

### 4.4 ApplicationContext

`ApplicationContext` is the central Spring container that creates and manages beans.

```java
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
```

In Spring Boot, we usually do not create it manually because Boot initializes it for us.

---

## 5. Bean Lifecycle and Scope

### Bean lifecycle

A Spring bean typically goes through these stages:

1. Instantiation
2. Dependency injection
3. Bean post-processing before initialization
4. Initialization
5. Ready for use
6. Destruction

Common lifecycle hooks:

- `@PostConstruct`
- `@PreDestroy`
- `InitializingBean`
- `DisposableBean`

### Bean scopes

- `singleton`: one bean instance per Spring container
- `prototype`: a new bean instance every time requested
- `request`: one bean per HTTP request
- `session`: one bean per HTTP session
- `application`: one bean per `ServletContext`

`singleton` is the default scope.

---

## 6. Configuration in Spring

Spring applications can be configured using:

- annotations
- Java configuration classes
- properties or YAML files
- XML configuration, though this is less common in modern projects

### Java configuration example

```java
@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### External configuration

Spring Boot commonly uses:

- `application.properties`
- `application.yml`

Example:

```properties
server.port=8081
spring.datasource.url=jdbc:postgresql://localhost:5432/appdb
spring.datasource.username=postgres
spring.datasource.password=secret
```

### Configuration binding

For grouped config, `@ConfigurationProperties` is cleaner than many `@Value` fields.

```java
@ConfigurationProperties(prefix = "app.mail")
public class MailProperties {
    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
```

---

## 7. Important Spring Boot Annotations

### Core annotations

- `@SpringBootApplication`: combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`
- `@Configuration`: declares a configuration class
- `@Bean`: registers a bean manually
- `@Component`: generic stereotype for a Spring-managed component
- `@Service`: marks business-layer classes
- `@Repository`: marks persistence-layer classes and enables exception translation
- `@Controller`: used for MVC controllers returning views
- `@RestController`: used for REST APIs returning JSON or response bodies

### Injection and configuration annotations

- `@Autowired`: injects dependencies, though constructor injection usually removes the need to place it explicitly
- `@Qualifier`: selects a specific bean when multiple candidates exist
- `@Value`: injects a property value
- `@ConfigurationProperties`: binds grouped properties to an object

### Web annotations

- `@RequestMapping`
- `@GetMapping`
- `@PostMapping`
- `@PutMapping`
- `@DeleteMapping`
- `@PathVariable`
- `@RequestParam`
- `@RequestBody`

### Data and transaction annotations

- `@Entity`
- `@Id`
- `@GeneratedValue`
- `@Table`
- `@Transactional`

### Async and scheduling annotations

- `@EnableAsync`
- `@Async`
- `@EnableScheduling`
- `@Scheduled`

---

## 8. Auto-Configuration in Spring Boot

Auto-configuration is one of Spring Boot's biggest productivity features.

It works by configuring beans automatically based on:

- libraries present on the classpath
- existing beans in the application context
- configuration properties

Common conditional annotations used internally include:

- `@ConditionalOnClass`
- `@ConditionalOnMissingBean`
- `@ConditionalOnProperty`

This means Spring Boot only configures what is relevant and backs off when we define our own custom bean.

Example idea:

- If `spring-boot-starter-data-jpa` is present, Boot can auto-configure JPA support
- If a `DataSource` bean already exists, Boot usually uses it instead of creating a competing one

---

## 9. Spring Boot Starters

Starters are curated dependency bundles that simplify setup.

Common starters:

- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-security`
- `spring-boot-starter-test`
- `spring-boot-starter-actuator`
- `spring-boot-starter-validation`

Example Maven dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

---

## 10. Layered Architecture in Spring Boot

A common Spring Boot application follows layered architecture.

### 10.1 Presentation layer

Responsible for:

- handling HTTP requests
- validating request payloads
- returning responses
- exposing REST endpoints or views

Usually implemented using controllers.

### 10.2 Service layer

Responsible for:

- business rules
- validations beyond basic request validation
- orchestration of multiple operations
- transaction boundaries

Usually implemented using `@Service` classes.

### 10.3 Persistence layer

Responsible for:

- database access
- query execution
- entity persistence and retrieval

Usually implemented using repositories.

### 10.4 Database layer

Responsible for:

- physical storage of application data
- constraints, indexes, tables, and relationships

A simple request flow looks like this:

`Client -> Controller -> Service -> Repository -> Database`

---

## 11. Spring MVC

Spring MVC is the web framework in the Spring ecosystem for building request-response applications.

It is based on the Model-View-Controller pattern.

### MVC components

- `Model`: data passed to the view or returned as response data
- `View`: UI representation such as JSP, Thymeleaf, or other template engines
- `Controller`: request handler that processes input and returns a response

### DispatcherServlet

`DispatcherServlet` is the front controller of Spring MVC.

It:

- receives incoming HTTP requests
- maps requests to controllers
- invokes handler methods
- resolves views or serializes response bodies
- returns the final response

### REST controller example

```java
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}
```

### Traditional MVC views

In classic MVC apps, views can be rendered using technologies such as:

- JSP
- Thymeleaf
- FreeMarker

For modern APIs, `@RestController` with JSON is more common than server-side JSP rendering.

---

## 12. Aspect-Oriented Programming (AOP)

AOP helps separate cross-cutting concerns from business logic.

Examples of cross-cutting concerns:

- logging
- security checks
- transactions
- auditing
- performance monitoring

Example:

```java
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.service.*.*(..))")
    public void logBefore() {
        System.out.println("Entering service method");
    }
}
```

### Common AOP terms

- `Aspect`: the class containing cross-cutting logic
- `Advice`: the action taken, such as `@Before` or `@After`
- `Pointcut`: expression that selects where advice should run
- `Join point`: a point during program execution, usually a method execution in Spring AOP

---

## 13. Event-Driven Communication in Spring

Spring provides an event model for loose coupling between components.

Use cases:

- order placed -> send confirmation email
- payment completed -> update inventory
- user registered -> trigger welcome workflow

### Publishing an event

```java
applicationEventPublisher.publishEvent(new OrderPlacedEvent(orderId));
```

### Listening to an event

```java
@EventListener
public void handleOrderPlaced(OrderPlacedEvent event) {
    // send email or trigger another process
}
```

Benefits:

- better decoupling
- easier extension of workflows
- improved maintainability

---

## 14. Data Access in Spring

Spring simplifies database interaction through abstractions and integrations.

### Common options

- Spring JDBC
- Spring Data JPA
- Spring Data JDBC
- Spring Data MongoDB

### Why this helps

Instead of writing low-level boilerplate for connection management, transactions, and repetitive query code, Spring provides higher-level abstractions.

---

## 15. Spring Data JPA and Hibernate

### JPA vs Hibernate

- `JPA` is a specification for object-relational persistence in Java
- `Hibernate` is a popular implementation of JPA

Spring Data JPA builds on top of JPA and Hibernate to simplify repository-based data access.

### Entity example

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
}
```

### Repository example

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByNameContainingIgnoreCase(String name);
}
```

### Benefits of Spring Data JPA

- less boilerplate
- derived query methods
- pagination and sorting
- integration with transactions
- custom JPQL and native queries when required

### Derived query example

```java
List<User> findByLastNameAndAgeGreaterThan(String lastName, int age);
```

Spring parses the method name and generates the query automatically.

---

## 16. Hibernate Entity Lifecycle

A Hibernate-managed entity moves through several states.

### 16.1 Transient

The object exists in memory but is not associated with a persistence context and is not stored in the database.

### 16.2 Persistent

The object is associated with the current persistence context. Changes are tracked and synchronized with the database.

### 16.3 Detached

The object was once persistent, but it is no longer attached to the current persistence context.

### 16.4 Removed

The object is marked for deletion and will be removed from the database.

### Dirty checking

Hibernate automatically detects changes made to persistent entities and updates the database during transaction commit.

---

## 17. Entity Relationships

Entity relationships map object associations to database foreign key relationships.

Common relationship annotations:

- `@OneToOne`
- `@OneToMany`
- `@ManyToOne`
- `@ManyToMany`

Example:

```java
@OneToMany(mappedBy = "user")
private List<Order> orders;
```

Key things to understand:

- owning side vs inverse side
- cascade types
- fetch types such as `EAGER` and `LAZY`
- referential integrity through foreign keys

Care is needed with bidirectional relationships to avoid recursion issues in JSON serialization.

---

## 18. Transactions

A transaction represents a unit of work that should succeed or fail as a whole.

### ACID properties

- Atomicity
- Consistency
- Isolation
- Durability

### Spring transaction support

Spring makes transaction management simple through `@Transactional`.

```java
@Transactional
public void processOrder() {
    // all DB operations here succeed or roll back together
}
```

Benefits:

- simpler transaction boundaries
- automatic rollback on failure in many cases
- cleaner service-layer code

Transactions are usually defined in the service layer, not the controller layer.

---

## 19. Validation

Validation ensures incoming data is correct before business logic runs.

Spring Boot commonly uses Jakarta Bean Validation with annotations such as:

- `@NotNull`
- `@NotBlank`
- `@Email`
- `@Size`
- `@Min`
- `@Max`

Example:

```java
public class CreateUserRequest {

    @NotBlank
    private String name;

    @Email
    private String email;
}
```

Controller usage:

```java
@PostMapping
public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest request) {
    return ResponseEntity.ok().build();
}
```

---

## 20. Spring Security

Spring Security is the standard framework for authentication and authorization in Spring applications.

### What it provides

- authentication
- authorization
- protection against common attacks
- integration with sessions, OAuth2, JWT, and method security

### Authentication vs authorization

- `Authentication`: Who are you?
- `Authorization`: What are you allowed to do?

### Security filter chain

Spring Security works mainly through a chain of filters that intercept requests before they reach controllers.

In modern Spring Boot applications, security is configured using a `SecurityFilterChain` bean.

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/public/**").permitAll()
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults());

    return http.build();
}
```

### Method-level security

```java
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser(Long id) {
}
```

---

## 21. OAuth2 and JWT

### OAuth2

OAuth2 is commonly used when applications delegate authentication to an identity provider.

Typical use cases:

- login with Google or GitHub
- securing APIs as a resource server
- machine-to-machine authorization

### JWT

JWT, or JSON Web Token, is a compact token format often used in stateless authentication.

Typical JWT flow:

1. User authenticates
2. Server issues a token
3. Client sends token in the `Authorization` header
4. Server validates token and authorizes the request

Example header:

```http
Authorization: Bearer <token>
```

JWT is popular for APIs, but it must be used carefully with expiration, signature validation, and secure key management.

---

## 22. Spring Boot Actuator

Actuator adds production-ready monitoring and management features.

Common endpoints:

- `/actuator/health`
- `/actuator/info`
- `/actuator/metrics`
- `/actuator/env`
- `/actuator/prometheus`

### Why Actuator matters

It helps us:

- monitor health
- inspect configuration and metrics
- integrate with monitoring systems
- improve production visibility

Example dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

---

## 23. Micrometer

Micrometer is a vendor-neutral metrics instrumentation facade used by Spring Boot.

It plays a role similar to what SLF4J does for logging, but for metrics.

### What Micrometer does

- collects JVM and application metrics
- exports metrics to monitoring systems such as Prometheus
- lets developers create custom counters, timers, and gauges

Example:

```java
registry.counter("orders.created").increment();
```

Micrometer commonly works with Actuator to expose metrics.

---

## 24. Micrometer vs Eureka

These two tools solve completely different problems.

| Feature | Micrometer | Eureka |
| --- | --- | --- |
| Primary focus | Metrics and observability | Service registration and discovery |
| Category | Monitoring library | Service registry |
| Main job | Collects and exports metrics | Helps services find each other |
| Common pairings | Prometheus, Grafana | Spring Cloud, Feign, Gateway |

### Summary

- Use `Micrometer` for observability
- Use `Eureka` for service discovery

---

## 25. Embedded Server

Spring Boot applications can run with an embedded server inside the application itself.

Common embedded servers:

- Tomcat
- Jetty
- Undertow

Benefits:

- easy local development
- simple packaging and deployment
- no need to install a separate application server in many cases

A Spring Boot web app is often packaged and run directly as a jar:

```bash
java -jar app.jar
```

---

## 26. Task Execution and Scheduling

Spring supports background execution and scheduled jobs.

### Asynchronous execution

```java
@Async
public void sendEmail() {
}
```

This is useful for non-blocking tasks such as:

- sending emails
- generating reports
- publishing notifications

### Scheduling

```java
@Scheduled(fixedRate = 5000)
public void runTask() {
}
```

This runs a task every 5 seconds.

To enable these features, use:

- `@EnableAsync`
- `@EnableScheduling`

---

## 27. Testing in Spring Boot

Testing is a major strength of the Spring ecosystem.

A strong testing strategy usually includes:

- unit tests
- slice tests
- integration tests

### 27.1 Unit tests

Test business logic in isolation using JUnit and Mockito.

Best for:

- service methods
- utility classes
- validation logic

### 27.2 Slice tests

Slice tests load only part of the application context.

Examples:

- `@WebMvcTest`: tests controllers
- `@DataJpaTest`: tests JPA repositories

These are faster than full integration tests.

### 27.3 Integration tests

`@SpringBootTest` loads the complete application context.

Use it when verifying:

- component integration
- configuration correctness
- end-to-end behavior within the application

Example:

```java
@SpringBootTest
class ApplicationTests {
}
```

### MockMvc

`MockMvc` allows testing MVC controllers without starting a real server.

### `@MockBean`

`@MockBean` replaces a real Spring bean with a mock inside the application context for testing.

---

## 28. Spring Data Variants

### Spring Data JPA

Used for relational databases with JPA and ORM support.

### Spring Data JDBC

Provides a simpler persistence model than JPA, with less abstraction and less ORM magic.

Useful when:

- we want straightforward relational mapping
- full JPA behavior is unnecessary
- simpler SQL-oriented persistence is preferred

### Spring Data MongoDB

Used for MongoDB and document-based persistence.

It provides a repository model similar to other Spring Data modules while preserving document-database concepts.

---

## 29. Microservices with Spring

Microservices architecture breaks a large application into smaller, independently deployable services.

### Characteristics

- each service has a focused responsibility
- services communicate over APIs or messaging
- services can be deployed independently
- failures must be handled carefully

### Benefits

- scalability
- team autonomy
- independent deployment
- technology flexibility in some architectures

### Challenges

- distributed system complexity
- observability
- service discovery
- fault tolerance
- configuration management

Spring Boot is often used to build the individual services, while Spring Cloud adds supporting distributed-system capabilities.

---

## 30. Spring Cloud

Spring Cloud provides tools and patterns for distributed systems and cloud-native applications.

Common capabilities include:

- service discovery
- centralized configuration
- API gateway support
- declarative HTTP clients
- resilience patterns

### 30.1 Eureka

Eureka is a service registry.

It allows services to:

- register themselves
- discover other services by logical name
- avoid hardcoding hostnames and ports

### 30.2 Spring Cloud Gateway

Acts as an API gateway and single entry point for external clients.

Typical responsibilities:

- request routing
- authentication and authorization delegation
- rate limiting
- filtering
- header transformation

### 30.3 Spring Cloud OpenFeign

OpenFeign helps create declarative HTTP clients.

Instead of writing low-level HTTP client code, we define an interface.

### 30.4 Spring Cloud Config

Centralizes configuration for multiple services and environments.

This is useful for:

- consistent config management
- environment-specific settings
- centralized updates

### 30.5 Circuit Breaker

Circuit breakers protect systems from cascading failure when downstream services are slow or unavailable.

Typical behavior:

- allow normal calls when healthy
- open the circuit when failure threshold is crossed
- stop sending requests temporarily
- recover gradually after a timeout

Modern Spring projects often use Resilience4j for this purpose.

---

## 31. Common Best Practices

- Prefer constructor injection over field injection
- Keep controllers thin and business logic in services
- Put transaction boundaries in the service layer
- Use DTOs instead of exposing entities directly in APIs
- Validate request payloads using Bean Validation
- Use Actuator and Micrometer for production observability
- Write unit tests and slice tests in addition to integration tests
- Use profiles for environment-specific configuration
- Avoid overusing `@Autowired` on fields
- Be careful with bidirectional JPA relationships and lazy loading

---

## 32. What Changed in Spring Boot 3

Spring Boot 3 introduced several important changes.

### Major updates

- Java 17 or higher is required
- package names changed from `javax.*` to `jakarta.*`
- improved support for native images with GraalVM
- modernization across the Spring ecosystem

This is especially important when migrating older applications.

---

## 33. Quick Revision Summary

### Spring Core

- Bean
- IoC
- DI
- ApplicationContext
- bean lifecycle and scopes

### Spring Boot

- auto-configuration
- starter dependencies
- embedded server
- Actuator

### Web

- Spring MVC
- `DispatcherServlet`
- controllers and REST APIs

### Persistence

- Spring Data JPA
- Hibernate lifecycle
- entity relationships
- transactions

### Security

- authentication
- authorization
- OAuth2
- JWT

### Cloud and microservices

- Eureka
- Gateway
- OpenFeign
- Config Server
- circuit breaker

### Operations and testing

- Micrometer
- Actuator
- MockMvc
- `@DataJpaTest`
- `@WebMvcTest`
- `@SpringBootTest`

---

## 34. Interview-Oriented Short Answers

### What is Spring Boot?

Spring Boot is a framework built on top of Spring that helps create stand-alone, production-ready applications quickly with minimal configuration.

### What is a bean?

A bean is an object managed by the Spring container.

### What is IoC?

IoC means the framework controls object creation and dependency management rather than the application doing it manually.

### What is DI?

Dependency Injection is the process of providing a class with its required dependencies from the container.

### What is auto-configuration?

Auto-configuration automatically configures Spring beans based on classpath dependencies, application properties, and existing beans.

### Difference between `@Controller` and `@RestController`?

`@Controller` is generally used for MVC view rendering, while `@RestController` returns response bodies directly, usually as JSON.

### What is `@Transactional`?

It marks a method or class so that its database operations execute within a transaction.

### What is the role of `DispatcherServlet`?

It is the front controller in Spring MVC that receives requests and dispatches them to the correct handlers.

### What is the difference between authentication and authorization?

Authentication verifies identity. Authorization checks permissions.

### What is the difference between Micrometer and Eureka?

Micrometer is for metrics and observability. Eureka is for service discovery.

---

## 35. Final Takeaway

Spring Boot makes enterprise Java development faster by combining Spring's flexibility with opinionated defaults, auto-configuration, embedded infrastructure, and production-ready integrations. To work effectively with Spring Boot, it is important to understand both the fundamentals of Spring, such as beans, IoC, and DI, and the practical ecosystem around data access, security, testing, and microservices.

---

## 36. Advanced Spring Boot Notes (Modern Production Topics)

Modern Spring Boot applications go beyond basic layered architecture. These topics are highly relevant in production systems and in modern interviews.

### 36.1 Virtual Threads (Project Loom)

Spring Boot supports virtual threads to improve concurrency handling for many workloads.

#### What problem they solve

Traditional platform threads are relatively expensive in terms of memory consumption and context switching overhead. This limits how many concurrent blocking tasks a server can handle efficiently.

#### What virtual threads do

- lightweight threads managed by the JVM
- allow handling very high concurrency
- let us keep simple blocking code instead of immediately switching to reactive programming
- improve scalability for I/O-heavy applications

#### How to enable

```properties
spring.threads.virtual.enabled=true
```

#### Important notes

- Requires Java 21 or higher
- Not a replacement for every concurrency model
- Be careful with thread pinning, especially around `synchronized` blocks and some blocking native operations
- Best suited for request-heavy and I/O-bound workloads

### 36.2 AOT Compilation and GraalVM Native Images

Spring Boot supports Ahead-of-Time processing and native image generation.

#### Benefits

- faster startup time
- lower memory usage
- better suitability for cloud, serverless, and scale-to-zero environments

#### Trade-offs

- less flexibility for highly dynamic runtime behavior
- reflection, proxies, and dynamic class loading may need explicit configuration
- native builds can increase build complexity and build time

Typical areas that may need configuration hints:

- Hibernate entities
- proxies
- serialization
- reflection-based libraries

### 36.3 Advanced JPA Relationship Management

Basic relationships are easy to define, but many real-world persistence bugs come from relationship configuration details.

#### Orphan removal

```java
@OneToMany(mappedBy = "user", orphanRemoval = true)
private List<Order> orders;
```

When a child entity is removed from the collection, it can also be deleted from the database.

#### Cascade types

Common cascade options:

- `PERSIST`
- `MERGE`
- `REMOVE`
- `ALL`

These propagate parent operations to child entities.

#### Fetch strategies

`LAZY`
Loads related data only when accessed.
Safer for performance in most cases.

`EAGER`
Loads related data immediately.
Can cause unnecessary queries and larger object graphs if overused.

Important caution:

- Improper use of `LAZY` loading can still lead to `LazyInitializationException`
- Fetching strategy mistakes can also cause N+1 query problems

### 36.4 Transaction Isolation and Propagation

Spring allows fine-grained transaction control using `@Transactional`.

#### Isolation levels

Isolation controls how concurrent transactions interact.

Common levels:

- `READ_COMMITTED`
- `REPEATABLE_READ`
- `SERIALIZABLE`

General rule:

- higher isolation gives stronger consistency
- higher isolation usually reduces concurrency and may affect performance

#### Propagation types

Propagation defines how transactions behave across method calls.

Common types:

- `REQUIRED`
- `REQUIRES_NEW`
- `MANDATORY`
- `SUPPORTS`

Example:

```java
@Transactional(
    isolation = Isolation.READ_COMMITTED,
    propagation = Propagation.REQUIRED
)
```

### 36.5 Spring Boot 3 Migration (Jakarta Shift)

Spring Boot 3 introduced major ecosystem changes that affect older applications.

#### Key changes

- Requires Java 17 or higher
- Package namespace moved from `javax.*` to `jakarta.*`

Example:

```java
import jakarta.persistence.Entity;
```

#### Impact

This change affects several areas:

- JPA
- validation
- servlets
- filters
- third-party libraries that depended on older `javax.*` packages

This is a major migration point for legacy projects.

### 36.6 Observability with Micrometer Tracing

Modern systems need more than logs and metrics. They also need distributed tracing.

#### Micrometer Tracing

Micrometer Tracing helps attach tracing context to requests, including:

- Trace ID
- Span ID

This makes it easier to follow a request as it moves across services.

#### OpenTelemetry integration

Traces can be exported to observability tools such as:

- Jaeger
- Zipkin
- OpenTelemetry-compatible backends

#### Why it matters

- debugging distributed systems
- performance monitoring
- root cause analysis
- understanding latency across service boundaries

### 36.7 Stateless Security for REST APIs

Modern REST APIs are usually stateless.

#### Key configuration

```java
http.sessionManagement(session ->
    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
);
```

#### Core concepts

- each request carries authentication information, usually a JWT
- the server does not store session state for the client
- this works well for scalable API systems

#### CORS vs CSRF

`CORS`
Allows a frontend running on a different origin to call the backend.

`CSRF`
Protects session-based applications from cross-site request forgery.

In many stateless JWT-based APIs, CSRF protection is often disabled because the application is not relying on browser-managed session cookies in the same way as traditional web apps.

### 36.8 Testing: `@Mock` vs `@MockBean` vs `@MockitoBean`

Understanding these annotations is important when choosing between unit and Spring-context-based tests.

#### `@Mock`

- comes from Mockito
- used in unit tests
- does not involve the Spring ApplicationContext

#### `@MockBean`

- replaces a real bean inside the Spring context
- useful in integration or slice tests
- lets us isolate one dependency while still loading Spring-managed components

#### `@MockitoBean`

- preferred in newer Spring versions
- intended as the modern replacement for `@MockBean`

### 36.9 Interview Quick-Hits (Modern)

#### What is AOT?

Ahead-of-Time processing prepares part of the Spring application context at build time, which improves startup and helps native execution.

#### Why use virtual threads?

To handle high concurrency with simpler blocking code and lower memory overhead.

#### What is the risk of `EAGER` fetching?

It can trigger large, unnecessary queries and degrade performance.

#### Difference between `@Mock` and `@MockBean`?

- `@Mock`: pure unit testing with Mockito
- `@MockBean`: replaces a bean inside the Spring context

---

## 37. Compact Interview Revision Sheet

This section is a fast revision guide for interviews and last-minute review.

### Core Spring

- `Bean`: object managed by the Spring container
- `IoC`: control of object creation is handled by the framework
- `DI`: dependencies are injected into classes instead of being created manually
- `ApplicationContext`: central container that manages Spring beans

### Spring Boot

- opinionated layer on top of Spring
- reduces boilerplate with auto-configuration and starter dependencies
- supports embedded servers and production tooling
- common entry annotation: `@SpringBootApplication`

### Web and MVC

- `DispatcherServlet`: front controller of Spring MVC
- `@Controller`: used for MVC views
- `@RestController`: used for REST APIs returning JSON
- `@RequestBody`: converts request JSON into a Java object
- `@PathVariable`: extracts values from the URL

### Data and JPA

- `JPA`: persistence specification
- `Hibernate`: popular JPA implementation
- `@Entity`: maps a class to a database table
- `@Repository`: persistence layer stereotype
- `@Transactional`: wraps database work in a transaction
- `LAZY` fetch is usually safer than `EAGER` for performance

### Security

- `Authentication`: who the user is
- `Authorization`: what the user can access
- modern config uses `SecurityFilterChain`
- stateless APIs usually use JWT and `SessionCreationPolicy.STATELESS`

### Testing

- `@Mock`: pure Mockito unit test
- `@WebMvcTest`: web layer slice test
- `@DataJpaTest`: persistence slice test
- `@SpringBootTest`: full application context test
- `@MockBean`: replaces a Spring bean during tests

### Microservices and Cloud

- `Eureka`: service discovery
- `Gateway`: API gateway
- `OpenFeign`: declarative HTTP client
- `Config Server`: centralized configuration
- circuit breaker prevents cascading service failures

### Observability

- `Actuator`: production endpoints like health and metrics
- `Micrometer`: metrics instrumentation
- `Micrometer Tracing`: distributed tracing with trace and span IDs

### Modern Topics

- `Virtual Threads`: lightweight concurrency model, best with Java 21+
- `AOT`: build-time optimization for faster startup
- `GraalVM Native Image`: low-memory, fast-start deployment option
- `jakarta.*`: required namespace shift in Spring Boot 3+

---

## 38. Common Pitfalls in Spring Boot

These are common mistakes that cause bugs, performance problems, and confusing behavior in real projects.

### 38.1 JPA Pitfalls

- Using `EAGER` fetching everywhere
This can trigger large result sets, unnecessary joins, and slow queries.

- Ignoring the N+1 query problem
This happens when one query loads parent entities and many additional queries load child entities one by one.

- Triggering `LazyInitializationException`
This often happens when a lazily loaded association is accessed outside an active persistence context.

- Exposing entities directly in API responses
This can leak internal structure, create serialization loops, and tightly couple the API to the database model.

- Misusing cascade operations
Overusing `CascadeType.ALL` can delete or persist related data unexpectedly.

- Forgetting `orphanRemoval` semantics
Removing an entity from a collection does not always mean it will be deleted unless the relationship is configured for that behavior.

### 38.2 Security Pitfalls

- Mixing session-based and stateless security models without a clear design
This often creates inconsistent authentication behavior.

- Disabling CSRF without understanding why
It is commonly disabled for stateless JWT APIs, but it should not be disabled blindly in session-based web applications.

- Trusting JWT presence without proper validation
A token must be validated for signature, expiration, issuer, and intended audience where applicable.

- Forgetting CORS configuration
The backend may work in Postman but fail in browsers if cross-origin rules are not configured correctly.

- Putting authorization only in controllers
Important authorization rules should also exist at the service or method-security level where appropriate.

- Exposing sensitive Actuator endpoints publicly
Operational endpoints should be restricted and carefully configured in production.

### 38.3 Transaction Pitfalls

- Starting transactions in the wrong layer
Transactions are usually best defined in the service layer, not in controllers.

- Assuming `@Transactional` works on every internal method call
Self-invocation can bypass the Spring proxy and prevent transactional behavior from applying as expected.

- Choosing overly strict isolation levels by default
Higher isolation can reduce concurrency and hurt performance.

- Misunderstanding propagation behavior
Using `REQUIRES_NEW` or `MANDATORY` without understanding the impact can produce unexpected rollbacks or failures.

- Performing long-running remote calls inside a database transaction
This can hold locks too long and reduce throughput.

- Assuming every exception triggers rollback automatically
Rollback behavior depends on exception type and transaction configuration.

### 38.4 Practical Prevention Tips

- keep entities and API DTOs separate
- prefer constructor injection and thin controllers
- inspect generated SQL when debugging JPA performance
- keep transactions focused and short-lived
- secure Actuator endpoints in production
- add tests for security rules, transactions, and repository queries
