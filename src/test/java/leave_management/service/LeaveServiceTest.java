package leave_management.service;

import leave_management.dto.LeaveRequestDto;
import leave_management.dto.LeaveResponseDto;
import leave_management.exception.ResourceNotFoundException;
import leave_management.model.LeaveRequest;
import leave_management.repository.LeaveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaveServiceTest {

    // This creates a fake version of our repository so we don't use real data in our unit tests
    @Mock
    private LeaveRepository leaveRepository;

    // This injects the fake repository into our real service
    @InjectMocks
    private LeaveService leaveService;

    private LeaveRequest sampleLeave;
    private LeaveRequestDto sampleDto;

    // This method runs before every single test to set up some fresh sample data
    @BeforeEach
    void setUp() {
        sampleLeave = new LeaveRequest();
        sampleLeave.setId(1L);
        sampleLeave.setEmployeeId(100L);
        sampleLeave.setLeaveType("SICK");
        sampleLeave.setStatus("PENDING");

        sampleDto = new LeaveRequestDto();
        sampleDto.setEmployeeId(100L);
        sampleDto.setLeaveType("SICK");
        sampleDto.setStartDate(LocalDate.now());
        sampleDto.setEndDate(LocalDate.now().plusDays(2));
        sampleDto.setReason("Fever");
    }

    @Test
    void testCreateLeave() {
        // Tell the fake repository what to do when save() is called
        when(leaveRepository.save(any(LeaveRequest.class))).thenReturn(sampleLeave);

        // Actually run the service method
        LeaveResponseDto response = leaveService.createLeave(sampleDto);

        // Verify the results
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("PENDING", response.getStatus());
        verify(leaveRepository, times(1)).save(any(LeaveRequest.class));
    }

    @Test
    void testGetLeaveById_Success() {
        when(leaveRepository.findById(1L)).thenReturn(Optional.of(sampleLeave));

        LeaveResponseDto response = leaveService.getLeaveById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void testGetLeaveById_NotFound() {
        when(leaveRepository.findById(1L)).thenReturn(Optional.empty());

        // Verify that the custom exception we created is thrown when an ID doesn't exist
        assertThrows(ResourceNotFoundException.class, () -> leaveService.getLeaveById(1L));
    }
}