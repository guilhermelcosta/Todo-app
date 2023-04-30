package com.guilhermecosta.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guilhermecosta.todo.models.User;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // extends JpaRepository faz a ligacao entre o repositorio e uma entidade
    // Para que essa ligacao seja feita, precisamos passar como parametro
    // qual a entidade <User> e o tipo do seu identificador(ID), que e <Long>
    // Portanto, os parametros ficaram <User, Long>

    @Transactional(readOnly = true)
    User findByUsername (String username);
}
