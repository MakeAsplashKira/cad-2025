## Отчет о лаботаротоной работе №3. Технологии работы с базами данных.JDBC

#### Ход работы

#### Задание 1. Скопируйте результат выполнения лабораторной работы № 2 в директорию /les06

Скопировал папку product-table из директории /les04/lab/ в /less06

#### Задание 2. Подключите к приложению встраиваемую базу данных H2 используя EmbeddedDatabaseBuilder

```java
// build.gradle.kts
dependencies {
    //JPA
    implementation("org.springframework.data:spring-data-jpa:3.3.5")

    // Hibernate
    implementation("org.hibernate.orm:hibernate-core:6.6.4.Final")

    // Jakarta Persistence API
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // Jakarta Annotation API
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

    // H2
    implementation("com.h2database:h2:2.3.232")

    // HikariCP
    implementation("com.zaxxer:HikariCP:6.2.1")
}

```
#### Задание 3. Напишите SQL скрипт создающие две таблицы "Продукты" (PRODUCTS) и "Категории" (CATEGORIES) (не забудьте про внешние ключи).

Создал в resources файл schema.sql

```sql
CREATE TABLE IF NOT EXISTS CATEGORIES (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500)
    );

CREATE TABLE IF NOT EXISTS PRODUCTS (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    category_id INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id)
    );

```

#### Задание 4. Настройте EmbeddedDatabaseBuilder так, чтобы он при старте приложения выполнял данный скрипт и создавал в базе данных таблицы CATEGORIES и PRODUCTS.

```java

//DatabaseConfig
package ru.bsuedu.cad.lab.config;

@Configuration
@EnableJpaRepositories(basePackages = "ru.bsuedu.cad.lab.repository")
@EnableTransactionManagement
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .build();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("ru.bsuedu.cad.lab.entity");
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");

        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.format_sql", "true");
        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}

//CategoryRepository
package ru.bsuedu.cad.lab.repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

//ProductRepository
package ru.bsuedu.cad.lab.repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}


//Product
package ru.bsuedu.cad.lab.entity;

@Entity
@Table(name = "PRODUCTS")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "category_id", nullable = false)
    private Long category;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public Product(String name, String description, Long category, BigDecimal price, Integer stockQuantity, String imageUrl, Date createdAt, Date updatedAt) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
```

#### Задание 5. Для таблицы "Категории" создайте Java класс Category, для моделирования данной сущности (аналогичный классу Product). И класс ConcreteCategoryProvider, аналогичный ConcreteProductProvider, данный класс должен предоставлять данные из CSV файла category.csv. CSV-файл должен располагаться в директории src/main/resources вашего приложения


```java
//Category
package ru.bsuedu.cad.lab.entity;

@Entity
@Table(name = "CATEGORIES")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
//ConcreteProductProvider
package ru.bsuedu.cad.lab.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.parser.CSVParser;
import ru.bsuedu.cad.lab.reader.ResourceFileReader;

import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {
    private final ResourceFileReader reader;
    private final CSVParser parser;

    @Value("${categories.file.name:category.csv}")
    private String fileName;

    public ConcreteCategoryProvider(ResourceFileReader reader, CSVParser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Category> getCategories() {
        try {
            List<String> lines = reader.read();
            return parser.parseCategories(lines);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}

```

#### Задание 6. Добавьте еще одну имплементацию интерфейса Renderer - DataBaseRenderer которая сохраняет данные считанные из SCV файлов в таблицы базы данных. Реализация DataBaseRenderer должна использоваться пол умолчанию.

```java

//DataBaseRenderer
package ru.bsuedu.cad.lab.renderer;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.provider.CategoryProvider;
import ru.bsuedu.cad.lab.provider.ProductProvider;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Primary
@Component
public class DataBaseRenderer implements Renderer {

    private final ProductProvider productProvider;
    private final CategoryProvider categoryProvider;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public DataBaseRenderer(ProductProvider productProvider,
                            CategoryProvider categoryProvider,
                            ProductRepository productRepository,
                            CategoryRepository categoryRepository) {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void render() {
        System.out.println("Сохранение в БД");

        List<Category> categories = categoryProvider.getCategories();
        System.out.println("Загружено категорий из CSV: " + categories.size());
        if(categoryRepository.count() == 0) categoryRepository.saveAll(categories);

        List<Product> products = productProvider.getProducts();
        System.out.println("Загружено продуктов из CSV: " + products.size());
        if (productRepository.count() == 0) productRepository.saveAll(products);

        System.out.println("Всего категорий в БД: " + categoryRepository.count());
        System.out.println("Всего продуктов в БД: " + productRepository.count());
    }
}

```
#### Задание 6. Реализуйте класс CategoryRequest, данный класс должен выполнять запрос к базе данных получающий следующую информацию - список категорий количество товаров в которых больше единицы. Данная информация должна выводиться в консоль с помощью библиотеки для логирования logback, уровень лога INFO.

```java
//build.gradle.kts
dependencies {
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

//CategoryRequest
package ru.bsuedu.cad.lab.service;

@Component
public class CategoryRequest {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void executeQuery() {
        logger.info("категории с кол-вом товаров > 1");

        String sql = """
            SELECT c.name, c.category_id, COUNT(p.product_id)
            FROM CATEGORIES c
            JOIN PRODUCTS p ON c.category_id = p.category_id
            GROUP BY c.category_id, c.name
            HAVING COUNT(p.product_id) > 1
            """;

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) {
            logger.info("Категории с количеством товаров больше 1 не найдены");
        } else {
            for (Object[] row : results) {
                String name = (String) row[0];
                Long categoryId = ((Number) row[1]).longValue();
                Long productCount = ((Number) row[2]).longValue();
                logger.info("Категория: {} (ID: {}) - товаров: {}", name, categoryId, productCount);
            }
        }
    }
}
//Main.java
public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class, DatabaseConfig.class);

        Renderer renderer = context.getBean(Renderer.class);

        renderer.render();

        CategoryRequest categoryRequest = context.getBean(CategoryRequest.class);
        categoryRequest.executeQuery();
    }
```

## Выводы
 Получил базовое понимание технологии работы с базами данных JDBC