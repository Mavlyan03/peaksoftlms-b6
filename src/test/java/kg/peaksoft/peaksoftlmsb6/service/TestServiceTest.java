package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.OptionRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.QuestionRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.TestRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.TestInnerPageResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.TestResponse;
import kg.peaksoft.peaksoftlmsb6.entity.enums.QuestionType;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.TestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TestServiceTest {

    @Autowired
    private TestService testService;

    @Autowired
    private TestRepository testRepository;

    @Test
    void isEnable() {
        kg.peaksoft.peaksoftlmsb6.entity.Test test = testRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Test not found"));
        boolean before = test.getIsEnable();
        SimpleResponse enable = testService.isEnable(1L);
        kg.peaksoft.peaksoftlmsb6.entity.Test after = testRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Test not found"));

        assertNotNull(enable);
        assertNotEquals(after.getIsEnable(), before);
    }

    @Test
    void createTest() {
        TestRequest request = new TestRequest();
        request.setTestName("Spring Boot");
        request.setLessonId(7L);
        List<QuestionRequest> questions = new ArrayList<>();
        List<OptionRequest> options = new ArrayList<>();

        QuestionRequest questionRequest = new QuestionRequest();
        OptionRequest optionRequest = new OptionRequest();

        questionRequest.setQuestion("What is Spring?");
        questionRequest.setQuestionType(QuestionType.SINGLETON);

        optionRequest.setOption("Framework");
        optionRequest.setIsTrue(true);

        options.add(optionRequest);
        questionRequest.setOptions(options);
        questions.add(questionRequest);
        request.setQuestions(questions);

        TestResponse test = testService.createTest(request);

        assertNotNull(test);
        assertEquals(test.getTestName(), request.getTestName());
    }

    @Test
    void getTestById() {
        TestInnerPageResponse test = testService.getTestById(1L);

        assertNotNull(test);
        assertEquals(test.getId(), 1L);
    }

    @Test
    void deleteById() {
        SimpleResponse simpleResponse = testService.deleteById(1L);

        assertNotNull(simpleResponse);
        assertThatThrownBy(() -> testService.getTestById(1L)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void updateTest() {
        TestRequest request = new TestRequest();
        request.setTestName("Spring Boot");
        request.setLessonId(7L);
        List<QuestionRequest> questions = new ArrayList<>();
        List<OptionRequest> options = new ArrayList<>();

        QuestionRequest questionRequest = new QuestionRequest();
        OptionRequest optionRequest = new OptionRequest();

        questionRequest.setQuestion("What is Spring?");
        questionRequest.setQuestionType(QuestionType.SINGLETON);

        optionRequest.setOption("Framework");
        optionRequest.setIsTrue(true);

        options.add(optionRequest);
        questionRequest.setOptions(options);
        questions.add(questionRequest);
        request.setQuestions(questions);

        kg.peaksoft.peaksoftlmsb6.entity.Test test = testRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Test not found"));
        TestInnerPageResponse response = testService.updateTest(test.getId(), request);

        assertNotNull(response);
        assertEquals(response.getName(), request.getTestName());
        assertEquals(response.getQuestions().listIterator().next().getQuestion(),
                request.getQuestions().listIterator().next().getQuestion());
        assertEquals(response.getQuestions().listIterator().next().getQuestionType(),
                request.getQuestions().listIterator().next().getQuestionType());
        assertEquals(response.getQuestions().listIterator().next().getOptionResponses().listIterator().next().getOptionValue(),
                request.getQuestions().listIterator().next().getOptions().listIterator().next().getOption());
        assertEquals(response.getQuestions().listIterator().next().getOptionResponses().listIterator().next().getIsTrue(),
                request.getQuestions().listIterator().next().getOptions().listIterator().next().getIsTrue());
    }
}