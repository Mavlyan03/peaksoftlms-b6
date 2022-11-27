package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.OptionRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.QuestionRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.TestRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.*;
import kg.peaksoft.peaksoftlmsb6.entity.*;
import kg.peaksoft.peaksoftlmsb6.entity.enums.QuestionType;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final ResultRepository resultRepository;
    private final LessonRepository lessonRepository;

    public SimpleResponse isEnable(Long testId) {
        Test test = testRepository.findById(testId).orElseThrow(
                () -> new NotFoundException("Тест не найден"));
        test.setIsEnable(!test.getIsEnable());
        List<Results> results = resultRepository.findResultByTestId(test.getId());
        return new SimpleResponse(String.format("%s ответов",results.size()));
    }

    public TestResponse createTest(TestRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> new NotFoundException("Урок не найден"));
        Test test = null;
        if (lesson.getTest() == null) {
            test = convertToEntity(request);
            lesson.setTest(test);
            test.setLesson(lesson);
        } else {
            throw new BadRequestException("У урока уже есть тест");
        }
        testRepository.save(test);
        return new TestResponse(test.getId(), test.getTestName());
    }

    public TestInnerPageResponse getTestById(Long id) {
        return convertToResponse(testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Тест не найден")
        ));
    }

    public SimpleResponse deleteById(Long id) {
        Test test = testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Тест не найден"));
        for (Results results : test.getResults()) {
            test.setResults(null);
            results.setTest(null);
            resultRepository.deleteById(results.getId());
        }
        for(Question question : test.getQuestion()) {
            for(Option option : question.getOptions()) {
                question.setOptions(null);
                optionRepository.deleteById(option.getId());
            }
            test.setQuestion(null);
            questionRepository.deleteById(question.getId());
        }
        testRepository.deleteTestById(test.getId());
        return new SimpleResponse("Тест удалён");
    }

    public TestInnerPageResponse updateTest(Long id, TestRequest testRequest) {
        Test test = testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Тест на найден"));
        testRepository.update(test.getId(), testRequest.getTestName());
        Question question1 = null;
        Option option1 = null;
        for(Question question : test.getQuestion()) {
            question1 = questionRepository.findById(question.getId()).orElseThrow(
                    () -> new NotFoundException(String.format("Question with id = %s not found", question.getId())));
            for (Option option : question.getOptions()) {
                option1 = optionRepository.findById(option.getId()).orElseThrow(
                        () -> new NotFoundException(String.format("Option with id = %s not found", option.getId())));

            }
            for (QuestionRequest questionRequest : testRequest.getQuestions()) {
                assert question1 != null;
                questionRepository.update(
                        question1.getId(),
                        questionRequest.getQuestion(),
                        questionRequest.getQuestionType());
                for (OptionRequest optionRequest : questionRequest.getOptions()) {
                    assert option1 != null;
                    optionRepository.update(
                            option1.getId(),
                            optionRequest.getOption(),
                            optionRequest.getIsTrue());
                }
            }
        }
        testRepository.save(test);
        return convertUpdateToResponse(id, test);
    }

    private TestInnerPageResponse convertUpdateToResponse(Long id, Test test) {
        Test tests = testRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Test with id = %s not found",id)));
        TestInnerPageResponse innerPageResponse = new TestInnerPageResponse(tests.getId(), test.getTestName());
        List<QuestionResponse> questionResponses = new ArrayList<>();
        List<OptionResponse> optionResponses = new ArrayList<>();
        for(Question question : test.getQuestion()) {
            QuestionResponse questionResponse = new QuestionResponse(
                    question.getId(),
                    question.getQuestion(),
                    question.getQuestionType());
            for(Option option : question.getOptions()) {
                OptionResponse optionResponse = new OptionResponse(
                        option.getId(),
                        option.getOptionValue());
                optionResponses.add(optionResponse);
            }
            questionResponse.setOptionResponses(optionResponses);
            questionResponses.add(questionResponse);
        }
        innerPageResponse.setQuestions(questionResponses);
        return innerPageResponse;
    }

    private TestInnerPageResponse convertToResponse(Test test) {
        TestInnerPageResponse testResponse = new TestInnerPageResponse(test.getId(), test.getTestName());
        List<QuestionResponse> questionResponses = new ArrayList<>();
        for (Question question : test.getQuestion()) {
            QuestionResponse questionResponse = new QuestionResponse(
                    question.getId(),
                    question.getQuestion(),
                    question.getQuestionType());
            List<OptionResponse> optionResponses = new ArrayList<>();
            for (Option option : question.getOptions()) {
                OptionResponse optionResponse = new OptionResponse(option.getId(), option.getOptionValue());
                optionResponses.add(optionResponse);
            }
            questionResponse.setOptionResponses(optionResponses);
            questionResponses.add(questionResponse);
        }
        testResponse.setQuestions(questionResponses);
        return testResponse;
    }


    private Test convertToEntity(TestRequest request) {
        Test test = new Test(request.getTestName());
        test.setIsEnable(false);
        for (QuestionRequest questionRequest : request.getQuestions()) {
            Question question = new Question(questionRequest.getQuestion(), questionRequest.getQuestionType());
            if (question.getQuestionType().equals(QuestionType.SINGLETON)) {
                int counter = 0;
                for (OptionRequest optionRequest : questionRequest.getOptions()) {
                    Option option = new Option(optionRequest.getOption(), optionRequest.getIsTrue());
                    question.addOption(option);
                    if (optionRequest.getIsTrue().equals(true)) {
                        counter++;
                    }
                }
                if (counter > 1) {
                    throw new BadRequestException("Вы должны написать лишь один правильный вариант");
                }
                if (counter < 1) {
                    throw new BadRequestException("Вы должны написать лишь один правильный вариант");
                }
            } else {
                int counter = 0;
                for (OptionRequest optionRequest : questionRequest.getOptions()) {
                    Option option = new Option(optionRequest.getOption(), optionRequest.getIsTrue());
                    question.addOption(option);
                    if (optionRequest.getIsTrue().equals(true)) {
                        counter++;
                    }
                }
                if (counter < 1) {
                    throw new BadRequestException("Вы должны написать минимум один правильный вариант");
                }
            }
            test.addQuestion(question);
        }
        return test;
    }
}
