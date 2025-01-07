package br.com.siswbrasil.integrator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.siswbrasil.integrator.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}

