package br.com.siswbrasil.integrator.service;

import java.util.List;

import br.com.siswbrasil.integrator.entity.Task;
import br.com.siswbrasil.integrator.repository.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.listAll();
    }

    public Task findById(Long id) {
        return taskRepository.findByIdOptional(id)
            .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }

    @Transactional
    public Task save(Task task) {
        taskRepository.persist(task);
        return task;
    }

    @Transactional
    public Task update(Long id, Task taskDetails) {
        Task existingTask = findById(id);
        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setStatus(taskDetails.getStatus());
        return existingTask;
    }

    @Transactional
    public void deleteById(Long id) {
        Task existingTask = findById(id);
        taskRepository.delete(existingTask);
    }
}
