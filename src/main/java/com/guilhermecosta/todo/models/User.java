package com.guilhermecosta.todo.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = User.TABLE_NAME)
@AllArgsConstructor //Retira necessidade de construir, na mao, todos construtores
@NoArgsConstructor //Mesma coisa, mas para construtores sem argumentos
@Getter //Criar getters
@Setter //Criar setters
@EqualsAndHashCode //Criar equals and HashCode
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
    @NotNull(groups = {CreateUser.class, UpdateUser.class})
    @NotEmpty(groups = {CreateUser.class, UpdateUser.class})
    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 8, max = 50)
    // Como aqui estamos tratando de senha, e ela pode ser atualizada, precisamos
    // que essas verificacoes tambem sejam feitas quando o usuario for atualizar a
    // sua senha
    private String password;

    // Mesma notacao da classe Task, que indica que um usuario vai ter varias Tasks
    // O parametro "user" indica o nome da variavel que vai mapear essa relacao na
    // classe Task (linha 23)
    @OneToMany(mappedBy = "user")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //Para quando buscar um usuario, nao vir SEMPRE todas as tasks, apenas se eu pedir isso
    private List<Task> tasks = new ArrayList<Task>();
}
