package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.otus.hw.config.TestFileNameProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    private List<String[]> csvValues = new ArrayList<>();

    private static final String FILE_NAME = "questions.csv";

    @BeforeEach
    void setUp() {
        openMocks(this);

        var inputStream = getClass().getClassLoader().getResourceAsStream(FILE_NAME);
        var scanner = new Scanner(inputStream);
        scanner.nextLine();
        while(scanner.hasNextLine()) {
            String[] values = scanner.nextLine().split(";");
            csvValues.add(values);
        }
    }

    @Test
    void findAll() {
        when(fileNameProvider.getTestFileName()).thenReturn(FILE_NAME);

        var questionList = csvQuestionDao.findAll();

        assertEquals(csvValues.size(), questionList.size());
        assertEquals(csvValues.get(0)[0], questionList.get(0).text());
        assertEquals(csvValues.get(1)[0], questionList.get(1).text());
        assertEquals(csvValues.get(2)[0], questionList.get(2).text());
    }
}