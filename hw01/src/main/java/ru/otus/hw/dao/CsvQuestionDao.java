package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        var inputStream = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName());
        if (inputStream == null) {
            throw new QuestionReadException("Can't read the file with questions");
        }
        var inputStreamReader = new InputStreamReader(inputStream);
        var listDto = new CsvToBeanBuilder<QuestionDto>(inputStreamReader)
                .withType(QuestionDto.class)
                .withSkipLines(1)
                .withSeparator(';')
                .build()
                .parse();
        return listDto.stream().map(QuestionDto::toDomainObject).toList();
    }
}
