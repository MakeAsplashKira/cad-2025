package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.config.AppConfig;
import ru.bsuedu.cad.lab.provider.ConcreteProductProvider;
import ru.bsuedu.cad.lab.renderer.ConsoleTableRenderer;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ConcreteProductProvider provider = context.getBean(ConcreteProductProvider.class);
        ConsoleTableRenderer renderer = context.getBean(ConsoleTableRenderer.class);

        renderer.render(provider.getProducts());
    }
}