package com.guilhermecosta.todo.services;

import com.guilhermecosta.todo.exceptions.AuthorizationException;
import com.guilhermecosta.todo.models.Task;
import com.guilhermecosta.todo.models.User;
import com.guilhermecosta.todo.models.enums.ProfileEnum;
import com.guilhermecosta.todo.repositories.TaskRepository;
import com.guilhermecosta.todo.security.UserSpringSecurity;
import com.guilhermecosta.todo.services.exceptions.DataBindingViolationException;
import com.guilhermecosta.todo.services.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    // O service se comunica com os repositories, por isso
    // precisamos fazer as ligacoes

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;
    // Recomendacao: quando precisar usar informacoes de outra entidade, nao usar o
    // seu repositorio, e sim o seu service

    public Task findById(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task))
            throw new AuthorizationException("Acesso negado!");

        return task;
    }

    public List<Task> findAllTasksByUser() {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        List<Task> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        return tasks;
    }

    @Transactional
    public Task createTask(Task obj) {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        User user = this.userService.findById(userSpringSecurity.getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);

        return obj;
    }

    @Transactional
    public Task updateTask(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());

        return this.taskRepository.save(newObj);
    }

    public void deleteTask(Long id) {
        findById(id);

        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Nao e possivel excluir tarefa pois ela possui entidades relacionadas");
        }
    }

    public boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
        return task.getUser().equals(userSpringSecurity.getId());
    }
}
