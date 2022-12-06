package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.InstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.UpdateInstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.InstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Instructor;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.InstructorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class InstructorServiceTest {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private InstructorService instructorService;


    @Test
    void createInstructor() {
        InstructorRequest request = new InstructorRequest(
                "FirstName", "LastName",
                "2020303", "ins@gmail.com",
                "Java", "Instructor");

        InstructorResponse instructor = instructorService.createInstructor(request);

        assertNotNull(instructor);
        assertEquals(instructor.getFullName(), request.getFirstName() + " " + request.getLastName());
        assertEquals(instructor.getEmail(), request.getEmail());
        assertEquals(instructor.getSpecialization(), request.getSpecialization());
        assertEquals(instructor.getPhoneNumber(), request.getPhoneNumber());
    }

    @Test
    void updateInstructor() {
        UpdateInstructorRequest request = new UpdateInstructorRequest(
                "first lastname", "004005",
                "mentor@gmail.com", "developer");
        Instructor instructor = instructorRepository.findById(1L).orElseThrow(
                () -> new NotFoundException("Instructor not found"));
        InstructorResponse response = instructorService.updateInstructor(instructor.getId(), request);

        assertNotNull(response);
        assertEquals(response.getFullName(), request.getFullName());
        assertEquals(response.getEmail(), request.getEmail());
        assertEquals(response.getSpecialization(), request.getSpecialization());
        assertEquals(response.getPhoneNumber(), request.getPhoneNumber());
    }

    @Test
    void deleteInstructorById() {
        SimpleResponse simpleResponse = instructorService.deleteInstructorById(1L);

        assertNotNull(simpleResponse);
        assertThatThrownBy(() -> instructorService.getById(1L)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Инструктор не найден");
    }

    @Test
    void getAllInstructors() {
        List<InstructorResponse> allInstructors = instructorService.getAllInstructors();

        assertEquals(2, allInstructors.size());
    }

    @Test
    void getById() {
        InstructorResponse instructor = instructorService.getById(1L);

        assertNotNull(instructor);
        assertEquals(instructor.getId(), 1L);
    }
}