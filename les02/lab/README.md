## Отчет о лаботаротоной работе №1. Gradle. Базовое приложение Spring

#### Ход работы

#### Задание 1-3. 

Установил Temurin JDK 17.0.18, Gradle 8.12, создал проект под названием product-table

#### Задание 4. Добавьте в ваш проект библиотеку org.springframework:spring-context:6.2.2


```java
// build.gradle.kts
dependencies {
    implementation("org.springframework:spring-context:6.2.2")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
```
#### Задание 5. Реализуйте приложение, которое должно представлять собой консольное приложение разработанное с помощью фреймворка Spring и конфигурируемое с помощью Java-конфигурации. Приложение должно читать данные о товарах для магазина из csv-файла и выводить его в консоль в виде таблицы

```java
//config/AppConfig.java

@Configuration
public class AppConfig {
    @Bean
    public ResourceFileReader resourceFileReader() {
        return new ResourceFileReader();
    }

    @Bean
    public CSVParser csvParser() {
        return new CSVParser();
    }

    @Bean
    public ConcreteProductProvider productProvider() {
        return new ConcreteProductProvider(resourceFileReader(), csvParser());
    }

    @Bean
    public ConsoleTableRenderer tableRenderer() {
        return new ConsoleTableRenderer();
    }
}

//model/Product.java

@AllArgsConstructor
@Getter
public class Product {
    Long productId; //0
    String name; //1
    String description; //2
    int categoryId; //3
    BigDecimal price; //4
    int stockQuantity; //5
    String imageUrl; //6
    Date createdAt; //7
    Date updatedAt; //8
}

//parser/CSVParser.java

public class CSVParser {
    public List<Product> parse(List<String> lines) {
        List<Product> products = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] columns = line.split(",");

            Product product = new Product(
                    Long.parseLong(columns[0]),
                    columns[1].trim(),
                    columns[2].trim(),
                    Integer.parseInt(columns[3]),
                    BigDecimal.valueOf(Double.parseDouble(columns[4])),
                    Integer.parseInt(columns[5]),
                    columns[6].trim(),
                    Date.valueOf(columns[7]),
                    Date.valueOf(columns[8])
            );
            products.add(product);
        }
        return products;
    }
}

//provider/ConcreteProductProvider.java

public class ConcreteProductProvider {
    private final ResourceFileReader reader;
    private final CSVParser parser;

    public ConcreteProductProvider(ResourceFileReader reader, CSVParser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    public List<Product> getProducts() {
        try {
            List<String> lines = reader.readLines("product.csv");
            return parser.parse(lines);
        } catch (Exception e) {
            e.printStackTrace();
            return  List.of();
        }
    }
}

//reader/RecourceFileReader.java

public class ResourceFileReader {

    public List<String> readLines(String filePath) throws Exception {
        List<String> lines = new ArrayList<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;

            while((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}

//renderer/ConsoleTableRenderer

public class ConsoleTableRenderer {
    public void render(List<Product> products) {
        System.out.println(
                "+------------" +
                "+----------------------------------" +
                "+-----------------" +
                "+----------" +
                "+--------+"
        );
        System.out.printf(
                "| %-10s | %-30s | %-15s | %-8s | %-6s |\n",
                "ID", "Название", "Цена", "Категория", "Остаток"
        );
        System.out.println("+------------+----------------------------------+-----------------+----------+--------+");

        for (Product p : products) {
            System.out.printf("| %-10s | %-30s | %-15.2f | %-8s | %-6d |\n",
                    p.getProductId(),
                    truncate(p.getName(), 30),
                    p.getPrice(),
                    p.getCategoryId(),
                    p.getStockQuantity()
            );
        }
        System.out.println("+------------+----------------------------------+-----------------+----------+--------+");
    }

    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}
//Main.java
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ConcreteProductProvider provider = context.getBean(ConcreteProductProvider.class);
        ConsoleTableRenderer renderer = context.getBean(ConsoleTableRenderer.class);

        renderer.render(provider.getProducts());
    }
}
```

## Выводы
Создал проект вида Java Applictaion с помощью Gradle, воспользовался библиотекой Spring. Науился работать с bean-ами.