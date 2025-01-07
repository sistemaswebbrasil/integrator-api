package br.com.siswbrasil.integrator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.siswbrasil.integrator.entity.Task;
import br.com.siswbrasil.integrator.exception.TaskNotFoundException;
import br.com.siswbrasil.integrator.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Task update(Long id, Task taskDetails) {
        Task existingTask = findById(id);
        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setStatus(taskDetails.getStatus());
        return taskRepository.save(existingTask);
    }

    public void deleteById(Long id) {
        Task existingTask = findById(id);
        taskRepository.delete(existingTask);
    }
}

