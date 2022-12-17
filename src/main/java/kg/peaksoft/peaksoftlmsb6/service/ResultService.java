package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.PassTestRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.OptionResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.QuestionResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.ResultResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResultResponse;
import kg.peaksoft.peaksoftlmsb6.entity.*;
import kg.peaksoft.peaksoftlmsb6.entity.enums.QuestionType;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ResultService {

    private final ResultRepository resultRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final StudentRepository studentRepository;

    public StudentResultResponse passTest(PassTestRequest passTestRequest, Authentication authentication) {
        Test test = testRepository.findById(passTestRequest.getTestId()).orElseThrow(
                () -> {
                    log.error("Test with id {} not found", passTestRequest.getTestId());
                    throw new NotFoundException("Тест не найден");
                });
        User user = (User) authentication.getPrincipal();
        User user1 = userRepository.findById(user.getId()).orElseThrow(
                () -> {
                    log.error("User with id {} not found", user.getId());
                    throw new NotFoundException("Пользователь не найден");
                });
        List<QuestionResponse> mapper = new ArrayList<>();
        List<Question> map = new ArrayList<>();
        int countCorrectAnswer = 0;
//        int percent = 0;
        for (Map.Entry<Long, List<Long>> answer : passTestRequest.getAnswers().entrySet()) {
            Question question = questionRepository.findById(answer.getKey()).orElseThrow(
                    () -> {
                        log.error("Question with id {} not found", answer.getKey());
                        throw new NotFoundException("Вопрос не найден");
                    });
            Question question1 = new Question(question);
            if (question.getQuestionType().equals(QuestionType.SINGLETON)) {
                for (Long optionId : answer.getValue()) {
                    Option option = optionRepository.findById(optionId).orElseThrow(
                            () -> {
                                log.error("Option with id {} not found", optionId);
                                throw new NotFoundException("Вариант не найден");
                            });
                    if (option.getIsTrue().equals(true)) {
                        countCorrectAnswer++;
//                        percent = 100 % test.getQuestion().size();
                    }
                    question1.addOption(option);
                }
                map.add(question1);
            } else if (question.getQuestionType().equals(QuestionType.MULTIPLE)) {
                int countOfCorrect = 0;
                int counter = 0;
                for (Option o : question.getOptions()) {
                    Option option = optionRepository.findById(o.getId()).orElseThrow(
                            () -> {
                                log.error("Option with id {} not found", o.getId());
                                throw new NotFoundException("Вариант не найден");
                            });
                    if (option.getIsTrue().equals(true)) {
                        counter++;
                    }
                }
//                int point = 100 % test.getQuestion().size();
                Long duplicate = 0L;
                for (Long optionId : answer.getValue()) {
                    Option option = optionRepository.findById(optionId).orElseThrow(
                            () -> {
                                log.error("Option with id {} not found", optionId);
                                throw new NotFoundException("Вариант не найден");
                            });
                    if (question.getOptions().contains(option)) {
                        if (option.getId().equals(duplicate)) {
                            log.error("You couldn't chose one option more time");
                            throw new BadRequestException("Нельзя выбирать один вариант несколько раз");
                        }
                        if (option.getIsTrue().equals(true)) {
                            countOfCorrect++;
                        } else if(option.getIsTrue().equals(false)) {
                            countOfCorrect--;
                        }
                        question1.addOption(option);
                        duplicate = option.getId();
                    } else {
                        log.error("Option assigned to the other question");
                        throw new BadRequestException("Вариант относится к другому вопросу");
                    }
                }
                if (countOfCorrect < 0) {
                    countOfCorrect = 0;
                    countCorrectAnswer += 0;
                }
                if (countOfCorrect == 0) {
                    countCorrectAnswer += 0;
//                    percent += 0;
                } else if (countOfCorrect == counter) {
                    countCorrectAnswer++;
//                    percent += point;
                } else if (countOfCorrect < counter) {
                    countCorrectAnswer += 0;
//                    percent += (point % counter) * countOfCorrect;
                }
                map.add(question1);
            }
        }
        for (Question question : map) {
            QuestionResponse questionResponse = new QuestionResponse(question);
            List<OptionResponse> optionResponses = new ArrayList<>();
            for (Option option : question.getOptions()) {
                optionResponses.add(new OptionResponse(option));
            }
            questionResponse.setOptionResponses(optionResponses);
            mapper.add(questionResponse);
        }
        Student student = null;
        if (user1.getRole().equals(Role.STUDENT)) {
            student = studentRepository.findByUserId(user1.getId()).orElseThrow(
                    () -> {
                        log.error("Student with id {} not found", user1.getId());
                        throw new NotFoundException("Студент не найден");
                    });
            if(resultRepository.existsByStudentId(student.getId()) && resultRepository.existsByTestId(test.getId())) {
                throw new BadRequestException("Студент уже проходил тест");
            } else {
                if (test.getIsEnable().equals(true)) {
                    Results results = new Results(
                            test,
                            countCorrectAnswer,
                            test.getQuestion().size() - countCorrectAnswer,
                            LocalDate.now(),
                            countCorrectAnswer * 10,
                            student);
                    resultRepository.save(results);
                } else if (test.getIsEnable().equals(false)) {
                    log.error("You couldn't pass the test");
                    throw new BadRequestException("Вы не можете пройти тест");
                }
            }
        }
        log.info("User pass the test was successfully");
        return new StudentResultResponse("Набрано баллов "
                + countCorrectAnswer + " из " + test.getQuestion().size(), mapper);
    }

    public List<ResultResponse> getAllResults(Long id) {
        Test test = testRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Test with id {} not found", id);
                    throw new NotFoundException("Тест не найден");
                });
        log.info("Instructor get all results by id {} was successfully", id);
        return mapToResponse(resultRepository.findResultByTestId(test.getId()));
    }

    public ResultResponse getById(Long id) {
        Results results = resultRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Result with id {} not found", id);
                    throw new NotFoundException("Результат не найден");
                });
        log.info("Get result by id {} was successfully", id);
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