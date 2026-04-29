package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.config.AppConfig;
import ru.bsuedu.cad.lab.config.DatabaseConfig;
import ru.bsuedu.cad.lab.renderer.Renderer;
import ru.bsuedu.cad.lab.service.CategoryRequest;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class, DatabaseConfig.class);

        Renderer renderer = context.getBean(Renderer.class);

        renderer.render();

        CategoryRequest categoryRequest = context.getBean(CategoryRequest.class);
        categoryRequest.executeQuery();
    }
}