package ru.otus.hw.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    private static final String ANSWER_PROMPT = "Enter you answer (number): ";
    private static final String ANSWER_ERROR = "Incorrect answer number! Please, try again.";

    @ParameterizedTest
    @CsvSource({"1, 1", "3, 0"})
    void executeTestFor(int answerNumber, int rightAnswerCount) {
        var student = getStudent();
        var questionList = getQuestions();
        var answerList = questionList.get(0).answers();
        when(questionDao.findAll()).thenReturn(questionList);
        when(ioService.readIntForRangeWithPrompt(1, 3, ANSWER_PROMPT, ANSWER_ERROR)).thenReturn(answerNumber);

        var testResult = testService.executeTestFor(student);

        verify(ioService).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");
        verify(ioService).printFormattedLine("Question: %s", questionList.get(0).text());
        verify(ioService).printFormattedLine("    Answer #%s: %s",  1,answerList.get(0).text());
        verify(ioService).printFormattedLine("    Answer #%s: %s",  2,answerList.get(1).text());
        verify(ioService).printFormattedLine("    Answer #%s: %s",  3,answerList.get(2).text());
        verify(ioService).readIntForRangeWithPrompt(1, 3, ANSWER_PROMPT, ANSWER_ERROR);

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