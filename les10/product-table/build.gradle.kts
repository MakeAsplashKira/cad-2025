plugins {
    id("java")
    id("war")
    id("application")
}
tasks.withType<JavaExec> {
    systemProperty("file.encoding", "UTF-8")
}
group = "ru.bsuedu.cad.lab"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring core
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-orm:6.2.2")

    //JPA
    implementation("org.springframework.data:spring-data-jpa:3.3.5")

    // Hibernate
    implementation("org.hibernate.orm:hibernate-core:6.6.4.Final")

    // Jakarta
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
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

    //JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")

    providedCompile("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("org.springframework:spring-webmvc:6.2.2")
}

tasks.withType<War> {
    archiveFileName.set("app.war")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath) {
        into("WEB-INF/lib")
    }
}

tasks.test {
    useJUnitPlatform()
}

