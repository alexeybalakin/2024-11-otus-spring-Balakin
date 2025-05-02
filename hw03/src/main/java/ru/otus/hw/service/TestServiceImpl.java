package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {

            ioService.printFormattedLineLocalized("TestService.question", question.text());
            var answerList = question.answers();
            for (int j = 0; j < answerList.size(); j++) {
                var answer = answerList.get(j);
                ioService.printFormattedLineLocalized("TestService.answer", j + 1, answer.text());
            }
            var answer = ioService.readIntForRangeWithPromptLocalized(1, answerList.size(),
                "TestService.enter.you.answer", "TestService.incorrect.answer.number");
            var isAnswerValid = answerList.get(answer - 1).isCorrect();

            testResult.applyAnswer(question, isAnswerValid);
        }

        return testResult;
    }

}
