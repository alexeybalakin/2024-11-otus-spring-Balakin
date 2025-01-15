package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;

@ComponentScan(basePackages = "ru.otus")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    QuestionDao csvQuestionDao(AppProperties appProperties) {
        return new CsvQuestionDao(appProperties);
    }
}
