plugins {
    id("java")
}

group = "ru.bsuedu.cad.lab"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring core
    implementation("org.springframework:spring-context:6.2.2")

    // String aspects
    implementation("org.springframework:spring-aspects:6.2.2")

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

    // Logback
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Tests
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}