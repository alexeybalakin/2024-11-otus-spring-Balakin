package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questionList = questionDao.findAll();

        var testResult = new TestResult(student);

        for (var question: questionList) {
            ioService.printFormattedLine("Question: %s", question.text());
            var answerList = question.answers();
            for (int j = 0; j < answerList.size(); j++) {
                var answer = answerList.get(j);
                ioService.printFormattedLine("    Answer #%s: %s", j + 1, answer.text());
            }
            var answer = ioService.readIntForRangeWithPrompt(1, answerList.size(),
                "Enter you answer (number): ", "Incorrect answer number! Please, try again.");
            var isAnswerValid = answerList.get(answer - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
