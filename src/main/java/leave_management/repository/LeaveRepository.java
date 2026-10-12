package leave_management.repository;

import leave_management.model.LeaveRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LeaveRepository {

    // Using ConcurrentHashMap for in-memory data storage as requested in the assignment
    private final ConcurrentHashMap<Long, LeaveRequest> database = new ConcurrentHashMap<>();

    // This helps us automatically generate a unique ID (1, 2, 3...) for new leave requests
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<LeaveRequest> findAll() {
        return new ArrayList<>(database.values());
    }

    public Optional<LeaveRequest> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    public LeaveRequest save(LeaveRequest leaveRequest) {
        if (leaveRequest.getId() == null) {
            // If the ID is null, it's a new request. Let's give it an ID.
            leaveRequest.setId(idGenerator.getAndIncrement());
        }
        // Save it into our in-memory map
        database.put(leaveRequest.getId(), leaveRequest);
        return leaveRequest;
    }

    public void deleteById(Long id) {
        database.remove(id);
    }
}