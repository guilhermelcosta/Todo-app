package com.guilhermecosta.todo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = Task.TABLE_NAME)
@AllArgsConstructor //Retira necessidade de construir, na mao, todos construtores
@NoArgsConstructor //Mesma coisa, mas para construtores sem argumentos
@Getter //Criar getters
@Setter //Criar setters
@EqualsAndHashCode //Criar equals and HashCode
public class Task {
    public static final String TABLE_NAME = "task";

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user; // Indicacao de que um unico usuario pode ter varias tarefas associadas

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "description", length = 144, nullable = false)
    @NotNull
    // Apesar de possuir o nullable = false, o NotNull garante que esse dado sera
    // barrado antes de chegar no BD
    @NotEmpty
    @Size(min = 1, max = 144)
    private String description;
}
