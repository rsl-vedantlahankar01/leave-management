package leave_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import leave_management.dto.LeaveRequestDto;
import leave_management.dto.LeaveResponseDto;
import leave_management.service.LeaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// This annotation tells Spring Boot to only load the web layer and this specific controller for testing
@WebMvcTest(LeaveController.class)
public class LeaveControllerTest {

    // MockMvc simulates HTTP requests (GET, POST, etc.)
    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper converts our Java objects into JSON text format for the web request
    @Autowired
    private ObjectMapper objectMapper;

    // We mock the service layer so we don't need the real repository
    @MockBean
    private LeaveService leaveService;

    private LeaveRequestDto requestDto;
    private LeaveResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new LeaveRequestDto();
        requestDto.setEmployeeId(100L);
        requestDto.setLeaveType("VACATION");
        requestDto.setStartDate(LocalDate.now());
        requestDto.setEndDate(LocalDate.now().plusDays(5));
        requestDto.setReason("Family trip");

        responseDto = new LeaveResponseDto();
        responseDto.setId(1L);
        responseDto.setEmployeeId(100L);
        responseDto.setLeaveType("VACATION");
        responseDto.setStatus("PENDING");
    }

    @Test
    void testCreateLeave() throws Exception {
        // Tell the mock service how to behave
        Mockito.when(leaveService.createLeave(any(LeaveRequestDto.class))).thenReturn(responseDto);

        // Simulate a POST request to /leaves
        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetLeaveById() throws Exception {
        Mockito.when(leaveService.getLeaveById(1L)).thenReturn(responseDto);

        // Simulate a GET request to /leaves/1
        mockMvc.perform(get("/leaves/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.employeeId").value(100L));
    }
}