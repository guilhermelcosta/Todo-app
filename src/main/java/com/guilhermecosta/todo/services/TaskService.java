package com.guilhermecosta.todo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guilhermecosta.todo.models.Task;
import com.guilhermecosta.todo.models.User;
import com.guilhermecosta.todo.repositories.TaskRepository;

import jakarta.transaction.Transactional;

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
        Optional<Task> task = this.taskRepository.findById(id);

        return task.orElseThrow(() -> new RuntimeException("Tarefa nao encontrata"));
    }

    public List<Task> findAllTasksByUserId(Long userId) {
        List<Task> tasks = this.taskRepository.findByUser_Id(userId);
        return tasks;
    }

    @Transactional
    public Task createTask(Task obj) {
        User user = this.userService.findById(obj.getUser().getId()); // Ele procura o ID do usuario, como ele esta
                                                                      // relacoinado ao atributo user da tabela task, e
                                                                      // preciso usar o obj.getUser();
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
            throw new RuntimeException("Nao e possivel excluir tarefa pois ela possui entidades relacionadas");
        }
    }
}
