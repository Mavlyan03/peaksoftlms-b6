package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.PassTestRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.ResultResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.*;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final StudentRepository studentRepository;

    public SimpleResponse passTest(PassTestRequest passTestRequest, Authentication authentication) {
        Test test = testRepository.findById(passTestRequest.getTestId()).orElseThrow(
                () -> new NotFoundException("Test not found"));
        User user = (User) authentication.getPrincipal();
        User user1 = userRepository.findById(user.getId()).orElseThrow(
                () -> new NotFoundException("User not found"));
        Integer amountOfCorrectAnswers = 0;
        for (Map.Entry<Long, List<Long>> answer : passTestRequest.getAnswers().entrySet()) {
            for (Long optionId : answer.getValue()) {
                Option option = optionRepository.findById(optionId).orElseThrow(
                        () -> new NotFoundException("Result not found"));
                if (option.getIsTrue()) {
                    amountOfCorrectAnswers++;
                }
            }
        }
        Student student = null;
        if (user1.getRole().equals(Role.STUDENT)) {
            student = studentRepository.findByUserId(user1.getId()).orElseThrow(
                    () -> new NotFoundException("Student not found"));
            if(test.getIsEnable().equals(true)) {
                Results results = new Results(
                        test,
                        LocalDate.now(),
                        amountOfCorrectAnswers,
                        test.getQuestion().size() + 1 - amountOfCorrectAnswers,
                        amountOfCorrectAnswers,
                        student);
                resultRepository.save(results);
            }
            else {
                new SimpleResponse("You can't pass the test");
            }
        }
        return new SimpleResponse("Набрано баллов "+amountOfCorrectAnswers+" из "+test.getQuestion().size());
    }

    public List<ResultResponse> getAllResults(Long id) {
        Test test = testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Test not found"));
        return mapToResponse(resultRepository.findResultByTestId(test.getId()));
    }

    public ResultResponse getById(Long id) {
        Results results = resultRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Result not found"));
        return resultRepository.getResult(results.getId());
    }

    private List<ResultResponse> mapToResponse(List<Results> results) {
        List<ResultResponse> resultResponses = new ArrayList<>();
        for (Results result : results) {
            resultResponses.add(new ResultResponse(result));
        }
        return resultResponses;
    }

}


