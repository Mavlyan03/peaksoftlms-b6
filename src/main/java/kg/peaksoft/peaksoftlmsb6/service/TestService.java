package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.OptionRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.PassTestRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.QuestionRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.TestRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.*;
import kg.peaksoft.peaksoftlmsb6.entity.*;
import kg.peaksoft.peaksoftlmsb6.entity.enums.QuestionType;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.OptionRepository;
import kg.peaksoft.peaksoftlmsb6.repository.QuestionRepository;
import kg.peaksoft.peaksoftlmsb6.repository.StudentRepository;
import kg.peaksoft.peaksoftlmsb6.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final StudentRepository studentRepository;

    public TestResponse saveTest(TestRequest request) {
        Test test = testRepository.save(convertToEntity(request));
        return new TestResponse(test.getId(), test.getTestName());
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

//    private TestInnerPageResponse convertToResponse(Test test) {
//        TestInnerPageResponse testResponse = new TestInnerPageResponse(test.getId(), test.getTestName());
//        List<QuestionResponse> questionResponseList = new ArrayList<>();
//        for(Question question : test.getQuestion()) {
//            QuestionResponse questionResponse = new QuestionResponse(
//                    question.getId(),
//                    question.getQuestion(),
//                    question.getQuestionType());
//            List<OptionResponse> optionResponses = new ArrayList<>();
//            for(Option o : question.getOptions()) {
//                OptionResponse optionResponse = new OptionResponse(o.getId(),o.getOptionValue());
//                optionResponses.add(optionResponse);
//            }
//            questionResponse.setOptionResponses(optionResponses);
//            questionResponseList.add(questionResponse);
//        }
//        testResponse.setQuestions(questionResponseList);
//        return testResponse;
//    }

    private Test convertToEntity(TestRequest request) {
        Test test = new Test(request.getTestName());
        for(QuestionRequest questionRequest : request.getQuestions()) {
            Question question = new Question(questionRequest.getQuestion(),questionRequest.getQuestionType());
            if(question.getQuestionType().equals(QuestionType.SINGLETON)) {
                int counter = 0;
                for(OptionRequest optionRequest : questionRequest.getOptions()) {
                    Option option = new Option(optionRequest.getOption(),optionRequest.getIsTrue());
                    question.getOptions().add(option);
                    if(optionRequest.getIsTrue().equals(true)) {
                        counter++;
                    }
                }
                if(counter > 1) {
                    throw new BadRequestException("Bad credentials");
                }
            } else {
                for(OptionRequest optionRequest : questionRequest.getOptions()) {
                    Option option = new Option(optionRequest.getOption(),optionRequest.getIsTrue());
                    question.getOptions().add(option);
                }
            }
            test.getQuestion().add(question);
        }
        return test;
    }
//    private Test convertToEntity(TestRequest request) {
//        Test test = new Test(request.getTestName());
//        for(QuestionRequest q : request.getQuestions()) {
//            Question question = new Question(q.getQuestion(),q.getQuestionType());
//            for(OptionRequest o : q.getOptionRequests()) {
//                Option option = new Option(o.getOption(),o.getIsTrue());
//                question.getOptions().add(option);
//            }
//            test.getQuestion().add(question);
//        }
//        return test;
//    }

}
