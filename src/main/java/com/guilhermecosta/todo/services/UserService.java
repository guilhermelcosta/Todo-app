package com.guilhermecosta.todo.services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.guilhermecosta.todo.exceptions.AuthorizationException;
import com.guilhermecosta.todo.models.enums.ProfileEnum;
import com.guilhermecosta.todo.security.UserSpringSecurity;
import com.guilhermecosta.todo.services.exceptions.DataBindingViolationException;
import com.guilhermecosta.todo.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.guilhermecosta.todo.models.User;
import com.guilhermecosta.todo.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    //Para decodificar a senha do usuario
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // O service se comunica com os repositories, por isso
    // precisamos fazer as ligacoes
    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        UserSpringSecurity userSpringSecurity =authenticated();
        if (!Objects.nonNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId()))
            throw new AuthorizationException("Acesso negado");

        // O Optional faz com que, caso seja buscado um usuario que nao exista no BD
        // em vez do codigo dar erro, ele retorna vazio

        // Ele chama o repository pois estamos usando a notacao presente nele
        Optional<User> user = this.userRepository.findById(id);

        // Como existe a possibilidade de nao se encontrar o usuario buscado
        // a funcao .ElseThrow() joga um excecao caso isso aconteca. Alem disso,
        // em vez de usar new Exception, usar RunTimeException, pois a Exception padrao
        // para a execucao do programa, e a RunTimeExeception nao
        return user.orElseThrow(() -> new ObjectNotFoundException("Usuario nao encontrado"));
    }

    // Usar o transactional sempre que for fazer uma manipulacao do BD: inserir e
    // atualizar dado
    @Transactional
    public User createUser(User obj) {
        obj.setId(null); // Isso e para seguranca, caso um usuario mande um ID pera query, ele reseta ela
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword())); //encripta a senha antes de salvar no BD
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet())); // Para definir o tipo de usuario em sua criacao
        obj = this.userRepository.save(obj);

        return obj;
    }

    @Transactional
    public User updateUser(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword())); //encripta a senha antes de salvar no BD
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
            throw new DataBindingViolationException("Nao e possivel excluir usuario pois ele possui entidades relacionadas");
        }
    }

    public static UserSpringSecurity authenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}
