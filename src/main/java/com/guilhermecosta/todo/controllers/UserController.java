package com.guilhermecosta.todo.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.guilhermecosta.todo.models.User;
import com.guilhermecosta.todo.models.User.CreateUser;
import com.guilhermecosta.todo.models.User.UpdateUser;
import com.guilhermecosta.todo.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Validated // Ele deve validar qualquer tipo de operacao realizada pelo service
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}") // colocar id entre chaves para indicar que ela e uma variavel e nao um texto
    public ResponseEntity<User> findById(@PathVariable Long id) { // PathVariable indica que a variavel passada como
                                                                  // parametro e o parametro da funcao
        // ResponseEntity serve para tratar os dados que serao retornados para o
        // front-end
        User obj = this.userService.findById(id);

        return ResponseEntity.ok().body(obj); // Converte o obj (User) para ResponseEntity
    }

    @PostMapping
    @Validated(CreateUser.class) // valida as informacoes de criacao da classe User
    public ResponseEntity<Void> createUser(@Valid @RequestBody User obj) {
        // @Valid indica que e ESTE dado que queremos validar
        // @RequestBody para indicar que estamos buscando informacoes do body

        this.userService.createUser(obj);

        // Converte o contexto do user para adicionar o path a ele, e transformar em URI
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated(UpdateUser.class)
    public ResponseEntity<Void> updateUser(@Valid @RequestBody User obj, @PathVariable Long id) {
        obj.setId(id);
        obj = this.userService.updateUser(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
