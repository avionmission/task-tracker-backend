package com.avionmission.tasks.repositories;

import com.avionmission.tasks.domain.entities.Task;
import com.avionmission.tasks.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByTaskListId(UUID taskListId);
    Optional<Task> findByTaskListIdAndId(UUID taskListId, UUID id);
    void deleteByTaskListIdAndId(UUID taskListId, UUID id);
    
    @Query("SELECT t FROM Task t WHERE t.taskList.user.id = :userId")
    List<Task> findByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT t FROM Task t WHERE t.taskList.user = :user")
    List<Task> findByUser(@Param("user") User user);
    
    @Query("SELECT t FROM Task t WHERE t.taskList.id = :taskListId AND t.taskList.user.id = :userId")
    List<Task> findByTaskListIdAndUserId(@Param("taskListId") UUID taskListId, @Param("userId") UUID userId);
    
    @Query("SELECT t FROM Task t WHERE t.id = :taskId AND t.taskList.user.id = :userId")
    Optional<Task> findByIdAndUserId(@Param("taskId") UUID taskId, @Param("userId") UUID userId);
}
