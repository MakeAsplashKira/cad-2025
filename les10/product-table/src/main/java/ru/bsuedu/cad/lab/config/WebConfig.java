package ru.bsuedu.cad.lab.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("ru.bsuedu.cad.lab.rest")
@Import(DatabaseConfig.class)
public class WebConfig {
}