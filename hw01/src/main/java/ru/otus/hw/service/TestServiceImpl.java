package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        var questionList = questionDao.findAll();
        for (int i = 0; i < questionList.size(); i++) {
            var question = questionList.get(i);
            ioService.printFormattedLine("Question #%s: %s", i + 1, question.text());
            for (int j = 0; j < question.answers().size(); j++) {
                var answer = question.answers().get(j);
                ioService.printFormattedLine("    Answer #%s: %s", j + 1, answer.text());
            }
        }
    }
}
