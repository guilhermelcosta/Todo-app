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
import java.util.Objects;

@Entity
@Table(name = Task.TABLE_NAME)
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


    public Task() {
    }

    public Task(User user, Long id, String description) {
        this.user = user;
        this.id = id;
        this.description = description;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task user(User user) {
        setUser(user);
        return this;
    }

    public Task id(Long id) {
        setId(id);
        return this;
    }

    public Task description(String description) {
        setDescription(description);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(user, task.user) && Objects.equals(id, task.id) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, id, description);
    }
}
