package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.OptionRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.PassTestRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.QuestionRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.TestRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.*;
import kg.peaksoft.peaksoftlmsb6.entity.*;
import kg.peaksoft.peaksoftlmsb6.entity.enums.QuestionType;
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

    public TestResponse save(TestRequest request) {
        return null;
    }

//    public TestResponse save(TestRequest request) {
//        Test test = testRepository.save(convertToEntity(request));
//        return new TestResponse(test.getId(),test.getTestName());
//    }


//
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

            }
        }
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

//    public ResultResponse passTest(PassTestRequest testRequest, Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
//        Integer countOfCorrectAnswers = 0;
//        Test test = testRepository.findById(testRequest.getTestId()).orElseThrow(
//                () -> new NotFoundException(String.format("Test with id =%s not found",testRequest.getTestId())));
//        for(Map.Entry<Long, List<Long>> answer : testRequest.getAnswers().entrySet()) {
//            for(Long optionId : answer.getValue()) {
//                Option option = optionRepository.findById(optionId).orElseThrow(
//                        () -> new NotFoundException(String.format("Option with id =%s not found")));
//                if(option.getIsTrue()) {
//                    countOfCorrectAnswers++;
//                }
//            }
//        }
//        Student student = studentRepository.findByEmail(user.getEmail()).orElseThrow(
//                () -> new NotFoundException(String.format("Student with email =%s not found",user.getEmail())));
//        Results results = new Results();
//    }

//        Student student = studentRepository.findStudentByAuthInfoEmail(authInfo.getEmail());
//        Result result = new Result(countOfCorrectAnswers,test.getQuestions().size()+1-countOfCorrectAnswers,
//                countOfCorrectAnswers,student);
//        resultRepository.save(result);
//        return new ResultResponse(student.getFirstName(), countOfCorrectAnswers,
//                test.getQuestions().size()-countOfCorrectAnswers,countOfCorrectAnswers);
//    }
}
