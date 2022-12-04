package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.response.ResultResponse;
import kg.peaksoft.peaksoftlmsb6.repository.ResultRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ResultServiceTest {

    @Autowired
    private ResultService resultService;

    @Autowired
    private ResultRepository resultRepository;

    @Test
    void passTest() {
    }

    @Test
    void getAllResults() {
        List<ResultResponse> results = resultService.getAllResults(1L);

        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    void getById() {
        ResultResponse response = resultService.getById(1L);

        assertNotNull(response);
        assertEquals(response.getId(), 1L);
    }
}