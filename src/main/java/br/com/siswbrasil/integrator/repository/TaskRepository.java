package br.com.siswbrasil.integrator.repository;

import br.com.siswbrasil.integrator.entity.Task;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskRepository implements PanacheRepository<Task> {        
    
    public Task findByTitle(String title) {
        return find("title", title).firstResult();
    }
}
