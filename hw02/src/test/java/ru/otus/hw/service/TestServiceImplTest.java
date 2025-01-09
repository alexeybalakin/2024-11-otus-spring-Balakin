package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;

class TestServiceImplTest {

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void executeTestFor() {
        var student = getStudent();
        var testResult = testService.executeTestFor(student);

        assertEquals(student, testResult.getStudent());
        assertEquals(0, testResult.getRightAnswersCount());
    }

    private Student getStudent() {
        return new Student("Ivan", "Petrov");
    }
}