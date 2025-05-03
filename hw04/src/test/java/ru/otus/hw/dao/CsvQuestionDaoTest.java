package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.AppProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CsvQuestionDaoTest {

    @MockBean
    private AppProperties appProperties;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    private static final String FILE_NAME = "test_questions.csv";
    private static final String QUESTION_1 = "Is there life on Mars?";
    private static final String QUESTION_2 = "How should resources be loaded from jar in Java?";
    private static final String QUESTION_3 = "Which option is a good way to handle the exception?";
    private static final int QUESTION_COUNT = 3;

    @Test
    void findAll() {
        when(appProperties.getTestFileName()).thenReturn(FILE_NAME);

        var questionList = csvQuestionDao.findAll();

        assertEquals(QUESTION_COUNT, questionList.size());
        assertEquals(QUESTION_1, questionList.get(0).text());
        assertEquals(QUESTION_2, questionList.get(1).text());
        assertEquals(QUESTION_3, questionList.get(2).text());
    }
}