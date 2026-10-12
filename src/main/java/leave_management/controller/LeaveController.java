package leave_management.controller;

import jakarta.validation.Valid;
import leave_management.dto.LeaveRequestDto;
import leave_management.dto.LeaveResponseDto;
import leave_management.service.LeaveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    // Constructor-based Dependency Injection[cite: 2]
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // 1. Retrieve all leave requests[cite: 1]
    @GetMapping
    public ResponseEntity<List<LeaveResponseDto>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    // 2. Retrieve a leave request by ID[cite: 1]
    @GetMapping("/{id}")
    public ResponseEntity<LeaveResponseDto> getLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getLeaveById(id));
    }

    // 3. Create a new leave request[cite: 1]
    // The @Valid annotation triggers the validation rules we set in the DTO[cite: 2]
    @PostMapping
    public ResponseEntity<LeaveResponseDto> createLeave(@Valid @RequestBody LeaveRequestDto dto) {
        LeaveResponseDto createdLeave = leaveService.createLeave(dto);
        return new ResponseEntity<>(createdLeave, HttpStatus.CREATED);
    }

    // 4. Update an existing leave request[cite: 1]
    @PutMapping("/{id}")
    public ResponseEntity<LeaveResponseDto> updateLeave(@PathVariable Long id, @Valid @RequestBody LeaveRequestDto dto) {
        return ResponseEntity.ok(leaveService.updateLeave(id, dto));
    }

    // 5. Delete a leave request[cite: 2]
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.noContent().build();
    }
}