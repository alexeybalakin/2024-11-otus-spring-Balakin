package ru.otus.hw.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TestServiceImplTest {

    @MockBean
    private LocalizedIOServiceImpl ioService;
    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private TestServiceImpl testService;

    private static final String ANSWER_PROMPT = "TestService.enter.you.answer";
    private static final String ANSWER_ERROR = "TestService.incorrect.answer.number";
    private static final String ANSWER = "TestService.answer";

    @ParameterizedTest
    @CsvSource({"1, 1", "3, 0"})
    void executeTestFor(int answerNumber, int rightAnswerCount) {
        var student = getStudent();
        var questionList = getQuestions();
        var answerList = questionList.get(0).answers();
        when(questionDao.findAll()).thenReturn(questionList);
        when(ioService.readIntForRangeWithPromptLocalized(1, 3, ANSWER_PROMPT, ANSWER_ERROR)).thenReturn(answerNumber);

        var testResult = testService.executeTestFor(student);

        verify(ioService, times(2)).printLine("");
        verify(ioService).printLineLocalized("TestService.answer.the.questions");
        verify(ioService, times(2)).printLine("");
        verify(ioService).printFormattedLineLocalized("TestService.question", questionList.get(0).text());
        verify(ioService).printFormattedLineLocalized(ANSWER,  1,answerList.get(0).text());
        verify(ioService).printFormattedLineLocalized(ANSWER,  2,answerList.get(1).text());
        verify(ioService).printFormattedLineLocalized(ANSWER,  3,answerList.get(2).text());
        verify(ioService).readIntForRangeWithPromptLocalized(1, 3, ANSWER_PROMPT, ANSWER_ERROR);

        assertEquals(student, testResult.getStudent());
        assertEquals(rightAnswerCount, testResult.getRightAnswersCount());
    }

    private Student getStudent() {
        return new Student("Ivan", "Petrov");
    }

    private List<Question> getQuestions() {
        var answer1 = new Answer("answer1", true);
        var answer2 = new Answer("answer2", false);
        var answer3 = new Answer("answer3", false);
        var question = new Question("question1", List.of(answer1, answer2, answer3));
        return List.of(question);
    }
}