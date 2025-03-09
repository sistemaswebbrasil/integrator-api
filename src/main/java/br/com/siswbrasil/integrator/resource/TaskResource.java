package br.com.siswbrasil.integrator.resource;

import java.util.List;

import br.com.siswbrasil.integrator.entity.Task;
import br.com.siswbrasil.integrator.service.TaskService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/api/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    TaskService taskService;

    @GET
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    @GET
    @Path("/{id}")
    public Task getTaskById(@PathParam("id") Long id) {
        return taskService.findById(id);
    }

    @POST
    public Response createTask(Task task) {
        Task savedTask = taskService.save(task);
        return Response.status(Status.CREATED).entity(savedTask).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTask(@PathParam("id") Long id, Task taskDetails) {
        Task updatedTask = taskService.update(id, taskDetails);
        return Response.ok(updatedTask).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") Long id) {
        taskService.deleteById(id);
        return Response.noContent().build();
    }
}
