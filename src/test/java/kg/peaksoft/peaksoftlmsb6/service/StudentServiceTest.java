package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.GroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.StudentExcelRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Group;
import kg.peaksoft.peaksoftlmsb6.entity.Student;
import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import kg.peaksoft.peaksoftlmsb6.repository.StudentRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository mockStudentRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private GroupRepository mockGroupRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private JavaMailSender mockJavaMailSender;

    private StudentService studentServiceUnderTest;

    @BeforeEach
    void setUp() {
        studentServiceUnderTest = new StudentService(mockStudentRepository, mockPasswordEncoder, mockGroupRepository,
                mockUserRepository, mockJavaMailSender);
    }

    @Test
    void testImportExcel() throws Exception {
        // Setup
        final MultipartFile multipartFile = null;

        // Configure GroupRepository.findById(...).
        final GroupRequest request = new GroupRequest();
        request.setGroupName("groupName");
        request.setDescription("description");
        request.setDateOfStart(LocalDate.of(2020, 1, 1));
        request.setImage("image");
        final Optional<Group> group = Optional.of(new Group(request));
        when(mockGroupRepository.findById(0L)).thenReturn(group);

        when(mockStudentRepository.existsByUserEmail("email")).thenReturn(false);
        when(mockPasswordEncoder.encode("rawPassword")).thenReturn("encode");

        // Configure StudentRepository.saveAll(...).
        final StudentExcelRequest studentExcelRequest = new StudentExcelRequest();
        studentExcelRequest.setIndexRow(0);
        studentExcelRequest.setName("name");
        studentExcelRequest.setLastName("lastName");
        studentExcelRequest.setPhoneNumber("phoneNumber");
        studentExcelRequest.setStudyFormat(StudyFormat.ONLINE);
        studentExcelRequest.setEmail("email");
        final List<Student> students = List.of(new Student(studentExcelRequest, "encode"));
        when(mockStudentRepository.saveAll(List.of(new Student(new StudentExcelRequest(), "encode"))))
                .thenReturn(students);

        // Run the test
        final SimpleResponse result = studentServiceUnderTest.importExcel(0L, multipartFile);

        // Verify the results
        verify(mockStudentRepository).saveAll(List.of(new Student(new StudentExcelRequest(), "encode")));
    }

    @Test
    void testImportExcel_GroupRepositoryReturnsAbsent() {
        // Setup
        final MultipartFile multipartFile = null;
        when(mockGroupRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> studentServiceUnderTest.importExcel(0L, multipartFile))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testImportExcel_ThrowsIOException() {
        // Setup
        final MultipartFile multipartFile = null;

        // Configure GroupRepository.findById(...).
        final GroupRequest request = new GroupRequest();
        request.setGroupName("groupName");
        request.setDescription("description");
        request.setDateOfStart(LocalDate.of(2020, 1, 1));
        request.setImage("image");
        final Optional<Group> group = Optional.of(new Group(request));
        when(mockGroupRepository.findById(0L)).thenReturn(group);

        when(mockStudentRepository.existsByUserEmail("email")).thenReturn(false);
        when(mockPasswordEncoder.encode("rawPassword")).thenReturn("encode");

        // Configure StudentRepository.saveAll(...).
        final StudentExcelRequest studentExcelRequest = new StudentExcelRequest();
        studentExcelRequest.setIndexRow(0);
        studentExcelRequest.setName("name");
        studentExcelRequest.setLastName("lastName");
        studentExcelRequest.setPhoneNumber("phoneNumber");
        studentExcelRequest.setStudyFormat(StudyFormat.ONLINE);
        studentExcelRequest.setEmail("email");
        final List<Student> students = List.of(new Student(studentExcelRequest, "encode"));
        when(mockStudentRepository.saveAll(List.of(new Student(new StudentExcelRequest(), "encode"))))
                .thenReturn(students);

        // Run the test
        assertThatThrownBy(() -> studentServiceUnderTest.importExcel(0L, multipartFile))
                .isInstanceOf(IOException.class);
        verify(mockStudentRepository).saveAll(List.of(new Student(new StudentExcelRequest(), "encode")));
    }

    @Test
    void testImportExcel_ThrowsMessagingException() {
        // Setup
        final MultipartFile multipartFile = null;

        // Configure GroupRepository.findById(...).
        final GroupRequest request = new GroupRequest();
        request.setGroupName("groupName");
        request.setDescription("description");
        request.setDateOfStart(LocalDate.of(2020, 1, 1));
        request.setImage("image");
        final Optional<Group> group = Optional.of(new Group(request));
        when(mockGroupRepository.findById(0L)).thenReturn(group);

        when(mockStudentRepository.existsByUserEmail("email")).thenReturn(false);
        when(mockPasswordEncoder.encode("rawPassword")).thenReturn("encode");

        // Configure StudentRepository.saveAll(...).
        final StudentExcelRequest studentExcelRequest = new StudentExcelRequest();
        studentExcelRequest.setIndexRow(0);
        studentExcelRequest.setName("name");
        studentExcelRequest.setLastName("lastName");
        studentExcelRequest.setPhoneNumber("phoneNumber");
        studentExcelRequest.setStudyFormat(StudyFormat.ONLINE);
        studentExcelRequest.setEmail("email");
        final List<Student> students = List.of(new Student(studentExcelRequest, "encode"));
        when(mockStudentRepository.saveAll(List.of(new Student(new StudentExcelRequest(), "encode"))))
                .thenReturn(students);

        // Run the test
        assertThatThrownBy(() -> studentServiceUnderTest.importExcel(0L, multipartFile))
                .isInstanceOf(MessagingException.class);
        verify(mockStudentRepository).saveAll(List.of(new Student(new StudentExcelRequest(), "encode")));
    }
}
