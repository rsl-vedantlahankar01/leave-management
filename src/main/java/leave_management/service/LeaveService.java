package leave_management.service;

import leave_management.dto.LeaveRequestDto;
import leave_management.dto.LeaveResponseDto;
import leave_management.exception.ResourceNotFoundException;
import leave_management.model.LeaveRequest;
import leave_management.repository.LeaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;

    // This is the Constructor-based Dependency Injection required by the assignment
    public LeaveService(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    public List<LeaveResponseDto> getAllLeaves() {
        return leaveRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public LeaveResponseDto getLeaveById(Long id) {
        LeaveRequest leave = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with ID: " + id));
        return mapToResponseDto(leave);
    }

    public LeaveResponseDto createLeave(LeaveRequestDto dto) {
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(dto.getEmployeeId());
        leave.setLeaveType(dto.getLeaveType());
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setStatus("PENDING"); // Default status for a new request

        LeaveRequest savedLeave = leaveRepository.save(leave);
        return mapToResponseDto(savedLeave);
    }

    public LeaveResponseDto updateLeave(Long id, LeaveRequestDto dto) {
        LeaveRequest leave = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with ID: " + id));

        leave.setEmployeeId(dto.getEmployeeId());
        leave.setLeaveType(dto.getLeaveType());
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());

        if (dto.getStatus() != null) {
            leave.setStatus(dto.getStatus());
        }

        LeaveRequest updatedLeave = leaveRepository.save(leave);
        return mapToResponseDto(updatedLeave);
    }

    public void deleteLeave(Long id) {
        LeaveRequest leave = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with ID: " + id));
        leaveRepository.deleteById(leave.getId());
    }

    // A helper method to convert our database Model back into a DTO for the user
    private LeaveResponseDto mapToResponseDto(LeaveRequest leave) {
        LeaveResponseDto dto = new LeaveResponseDto();
        dto.setId(leave.getId());
        dto.setEmployeeId(leave.getEmployeeId());
        dto.setLeaveType(leave.getLeaveType());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setReason(leave.getReason());
        dto.setStatus(leave.getStatus());
        return dto;
    }
}