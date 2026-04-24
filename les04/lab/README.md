## Отчет о лаботаротоной работе №2. Конфигурирование приложение Spring c помощью аннотаций. Применение AOП для логирования

#### Ход работы

#### Задание 1. Скопируйте результат выполнения лабораторной работы № 1 в директорию /les04/lab/

Скопировал папку product-table из директории /les02/lab/ в /less04/lab/

#### Задание 2. Переделайте приложение так, чтобы его конфигурирование осуществлялось с помощью аннотаций @Component

В рамках задания все классы приложения были помечены аннотацией `@Component`, а также создан класс конфигурации `AppConfig` с аннотацией `@ComponentScan` для автоматического обнаружения бинов.

```java
// AppConfig.java
@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
@PropertySource("classpath:application.properties")
public class AppConfig {
}

// Пример компонента, помеченного анотацией
@Component
public class ConsoleTableRenderer implements Renderer {
    ...
}
```
#### Задание 3. Использую аннотацию @Value и SpEL сделайте так, чтобы имя файла для загрузки продуктов, приложение получало из конфигурационного файла application.properties. Данный файл поместите в каталог ресурсов (src/main/resources)

```java
//application.properties
products.file.name=product.csv

//Пример получения значения из файла конфигурации в компоненте
@Component
public class ResourceFileReader implements Reader, InitializingBean {
    @Value("${products.file.name}") //Указываем что получаем
    private String fileName;
    
    @Override
    public List<String> read() throws Exception {
        List<String> lines = new ArrayList<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;

            while((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
```

#### Задание 4. Добавьте еще одну имплементацию интерфейса Renderer - HTMLTableRenderer которая выводит таблицу в HTML-файл. Сделайте так, чтобы при работе приложения вызывалась эта реализация, а не ConsoleTableRenderer.

```java

//HTMLTableRenderer
@Component
@Primary // Это анотация нужна для того, чтобы вызывалась именно эта реализация
@AllArgsConstructor
public class HTMLTableRenderer implements Renderer {
    private final ProductProvider provider;

    @Override
    public void render() {
        List<Product> products = provider.getProducts();
        String fileName = "products.html";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(generateHtml(products));
            System.out.println("HTML-отчёт создан: " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении HTML-файла: " + e.getMessage());
        }
    }

    private String generateHtml(List<Product> products) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>Товары интернет-магазина</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
        html.append("        h1 { color: #333; text-align: center; }\n");
        html.append("        table { border-collapse: collapse; width: 100%; margin-top: 20px; }\n");
        html.append("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        html.append("        th { background-color: #4CAF50; color: white; }\n");
        html.append("        tr:nth-child(even) { background-color: #f2f2f2; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");

        html.append("    <h1>Список товаров</h1>\n");
        html.append("    <table>\n");
        html.append("        <tr>\n");
        html.append("            <th>ID</th>\n");
        html.append("            <th>Название</th>\n");
        html.append("            <th>Описание</th>\n");
        html.append("            <th>Категория</th>\n");
        html.append("            <th>Цена</th>\n");
        html.append("            <th>Количество</th>\n");
        html.append("        </tr>\n");

        for (Product product : products) {
            html.append("        <tr>\n");
            html.append(String.format("            <td>%s</td>\n", String.valueOf(product.getProductId())));
            html.append(String.format("            <td>%s</td>\n", product.getName()));
            html.append(String.format("            <td>%s</td>\n", product.getDescription()));
            html.append(String.format("            <td>%s</td>\n", String.valueOf(product.getCategoryId())));
            html.append(String.format("            <td>%.2f ₽</td>\n", product.getPrice()));
            html.append(String.format("            <td>%d</td>\n", product.getStockQuantity()));
            html.append("        </tr>\n");
        }

        html.append("    </table>\n");
        html.append("</body>\n");
        html.append("</html>\n");

        return html.toString();
    }
}

//Main
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Renderer renderer = context.getBean(Renderer.class);

        renderer.render();
    }
}

```

#### Задание 5. С помощью событий жизненного цикла бина, выведите в консоль дату и время, когда бин ResourceFileReader был полностью инициализирован.


```java
@Component // Вывод в консоль жизненного цикла бина с помощью InitializingBean
public class ResourceFileReader implements Reader, InitializingBean {
    @Value("${products.file.name}")
    private String fileName;

    @Override
    public void afterPropertiesSet() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String initTime = LocalDateTime.now().format(formatter);
        System.out.println("[ResourceFileReader] Бин инициализирован в: " + initTime) ;
    }
    ...
}
```

#### Задание 6. С помощью инструментов AOП замерьте сколько времени тратиться на парсинг CSV файла.

```java

//Добавление зависимостей в build.gradle.kts
dependencies {
    implementation("org.springframework:spring-aspects:6.2.2")
}
//Компонент для замера времени
@Aspect
@Component
public class ZamerAspect {
    @Around("execution(* ru.bsuedu.cad.lab.parser.CSVParser.parse(..))")
    public Object measureParseTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.nanoTime();
        String startTimeReadable = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH::mm:ss.SSS"));

        System.out.println("[AOP] начало парсинга CSV в: " + startTimeReadable);

        Object result = joinPoint.proceed();

        long endTime = System.nanoTime();
        String endTimeReadable = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

        double durationMs = (endTime - startTime) / 1_000_000.0;

        System.out.println("[AOP] Окончание парсинга CSV в: " + endTimeReadable);
        System.out.println("[AOP] Время парсинга CSV файла: %.1f мс", durationMs);

        return result;
    }
}
```

## Выводы
Сконфигурировал приложение Spring c помощью аннотаций. Примененил AOП для логирования