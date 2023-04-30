package com.guilhermecosta.todo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guilhermecosta.todo.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser_Id(Long id); 
    //O underline indica que voce quer procurar uma lista de tasks relacionadas a um usuario,
    //mas o id desse usuario esta DENTRO do modelo User
    
}
