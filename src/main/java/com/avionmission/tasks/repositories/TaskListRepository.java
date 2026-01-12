package com.avionmission.tasks.repositories;

import com.avionmission.tasks.domain.entities.TaskList;
import com.avionmission.tasks.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, UUID> {
    
    List<TaskList> findByUser(User user);
    
    List<TaskList> findByUserId(UUID userId);
    
    Optional<TaskList> findByIdAndUser(UUID id, User user);
    
    Optional<TaskList> findByIdAndUserId(UUID id, UUID userId);
}
