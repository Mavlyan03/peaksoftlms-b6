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

//    public TestInnerPageResponse updateTest(Long id, TestRequest testRequest) {
//        Test test = testRepository.findById(id).orElseThrow(
//                () -> new NotFoundException("Тест на найден"));
//        testRepository.update(test.getId(), testRequest.getTestName());
//        List<Question> questions = new ArrayList<>();
//        List<Option> options = new ArrayList<>();
//        for(QuestionRequest questionRequest : testRequest.getQuestions()) {
//            Question question = new Question();
//            for(OptionRequest optionRequest : questionRequest.getOptions()) {
//                options.add(new Option(optionRequest));
//            }
//            question.setOptions(options);
//            questions.add(question);
//        }
//        test.setQuestion(questions);
//        testRepository.save(test);
//        return null;
//    }

    public SimpleResponse updateTest(Long id, TestRequest testRequest) {
        Test test = testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Тест на найден"));
        testRepository.update(test.getId(), testRequest.getTestName());
        List<Question> questions = new ArrayList<>();
        List<Option> options = new ArrayList<>();
        for(QuestionRequest questionRequest : testRequest.getQuestions()) {
            Question question = new Question(questionRequest);
            for(OptionRequest optionRequest : questionRequest.getOptions()) {
                options.add(new Option(optionRequest));
            }
            question.setOptions(options);
            questions.add(question);
        }
        test.setQuestion(questions);
        return new SimpleResponse("Test update successfully");
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
