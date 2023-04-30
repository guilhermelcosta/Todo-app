package com.guilhermecosta.todo.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = User.TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "user";

    public interface CreateUser {
    }

    public interface UpdateUser {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "user_name", length = 100, nullable = false, unique = true)
    @NotNull(groups = CreateUser.class)
    @NotEmpty(groups = CreateUser.class)
    @Size(groups = CreateUser.class, min = 2, max = 100)
    // O groups define que, quando da criacao de um novo usuario, seria verificada
    // as condicoes de NotNull pela funcao CreatUser
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    @NotNull(groups = { CreateUser.class, UpdateUser.class })
    @NotEmpty(groups = { CreateUser.class, UpdateUser.class })
    @Size(groups = { CreateUser.class, UpdateUser.class }, min = 8, max = 50)
    // Como aqui estamos tratando de senha, e ela pode ser atualizada, precisamos
    // que essas verificacoes tambem sejam feitas quando o usuario for atualizar a
    // sua senha
    private String password;

    @OneToMany(mappedBy = "user")
    // Mesma notacao da classe Task, que indica que um usuario vai ter varias Tasks
    // O parametro "user" indica o nome da variavel que vai mapear essa relacao na
    // classe Task (linha 23)
    private List<Task> tasks = new ArrayList<Task>();

    public User() {
    }

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore // Quando voce bsucar um usuario, nao retornar sempre todas as taks, isso
                // poderia diminuir performance
    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username)
                && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }

}
