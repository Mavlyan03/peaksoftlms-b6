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
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final ResultRepository resultRepository;
    private final LessonRepository lessonRepository;

    public TestResponse createTest(TestRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> new NotFoundException(String.format("Lesson with id =%s not found",request.getLessonId())));
        Test test = convertToEntity(request);
        lesson.setTest(test);
        test.setLesson(lesson);
        testRepository.save(test);
        return new TestResponse(test.getId(), test.getTestName());
    }

    public TestInnerPageResponse getTestById(Long id) {
        return convertToResponse(testRepository.findById(id).orElseThrow(
                () -> new RuntimeException("not found")
        ));
    }

    public SimpleResponse deleteById(Long id) {
       Test test = testRepository.findById(id).orElseThrow(
               () -> new NotFoundException(String.format("Test with id =%s not found",id)));
       for(Results results : test.getResults()) {
           test.setResults(null);
           results.setTest(null);
           resultRepository.deleteById(results.getId());
       }
       testRepository.deleteTestById(id);
       return new SimpleResponse("Test deleted");
    }

    private TestInnerPageResponse convertToResponse(Test test) {
        TestInnerPageResponse testResponse = new TestInnerPageResponse(test.getId(), test.getTestName());
        List<QuestionResponse> questionResponses = new ArrayList<>();
        for(Question question : test.getQuestion()) {
            QuestionResponse questionResponse = new QuestionResponse(
                    question.getId(),
                    question.getQuestion(),
                    question.getQuestionType());
            List<OptionResponse> optionResponses = new ArrayList<>();
            for(Option option : question.getOptions()) {
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
        for(QuestionRequest questionRequest : request.getQuestions()) {
            Question question = new Question(questionRequest.getQuestion(),questionRequest.getQuestionType());
            if(question.getQuestionType().equals(QuestionType.SINGLETON)) {
                int counter = 0;
                for(OptionRequest optionRequest : questionRequest.getOptions()) {
                    Option option = new Option(optionRequest.getOption(),optionRequest.getIsTrue());
                    question.addOption(option);
                    if(optionRequest.getIsTrue().equals(true)) {
                        counter++;
                    }
                }
                if(counter > 1) {
                    throw new BadRequestException("Bad request");
                }
                if(counter < 1) {
                    throw new BadRequestException("Bad request");
                }
            } else {
                int counter = 0;
                for(OptionRequest optionRequest : questionRequest.getOptions()) {
                    Option option = new Option(optionRequest.getOption(),optionRequest.getIsTrue());
                    question.addOption(option);
                    if(optionRequest.getIsTrue().equals(true)) {
                        counter++;
                    }
                }
                if(counter < 1) {
                    throw new BadRequestException("Bad credentials");
                }
            }
            test.addQuestion(question);
        }
        return test;
    }

}
