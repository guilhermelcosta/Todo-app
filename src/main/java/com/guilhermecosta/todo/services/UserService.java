package com.guilhermecosta.todo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guilhermecosta.todo.models.User;
import com.guilhermecosta.todo.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    // O service se comunica com os repositories, por isso
    // precisamos fazer as ligacoes

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        // O Optional faz com que, caso seja buscado um usuario que nao exista no BD
        // em vez do codigo dar erro, ele retorna vazio

        // Ele chama o repository pois estamos usando a notacao presente nele
        Optional<User> user = this.userRepository.findById(id);

        // Como existe a possibilidade de nao se encontrar o usuario buscado
        // a funcao .ElseThrow() joga um excecao caso isso aconteca. Alem disso,
        // em vez de usar new Exception, usar RunTimeException, pois a Exception padrao
        // para a execucao do programa, e a RunTimeExeception nao
        return user.orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
    }

    // Usar o transactional sempre que for fazer uma manipulacao do BD: inserir e
    // atualizar dado
    @Transactional
    public User createUser(User obj) {
        obj.setId(null); // Isso e para seguranca, caso um usuario mande um ID pera query, ele reseta ela
        obj = this.userRepository.save(obj);

        return obj;
    }

    @Transactional
    public User updateUser(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        return this.userRepository.save(newObj);
    }

    public void deleteUser(Long id) {
        // Verifica se o usuario com determinado ID existe
        findById(id);

        // Verifica se existem tarefas relacionadas ao usuario, caso ele tenha, o
        // usuario nao pode ser deletado
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Nao e possivel excluir usuario pois ele possui entidades relacionadas");
        }
    }
}