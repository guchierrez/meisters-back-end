package com.gutierrez.marcelino.test.meisters.controllers;

import com.gutierrez.marcelino.test.meisters.domain.todo.RequestTodo;
import com.gutierrez.marcelino.test.meisters.domain.todo.RequestTodoUpdate;
import com.gutierrez.marcelino.test.meisters.domain.todo.Todo;
import com.gutierrez.marcelino.test.meisters.domain.todo.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/todos")
public class TodoController {
    @Autowired
    private TodoRepository repository;

    @GetMapping
    public ResponseEntity getAllTodos() {
        var allTodos = repository.findAll();
        return ResponseEntity.ok(allTodos);
    }

    @PostMapping
    public ResponseEntity createTodo(@RequestBody @Valid RequestTodo data) {
        Todo newTodo = new Todo(data);
        repository.save(newTodo);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity updateTodo(@PathVariable String id, @RequestBody RequestTodoUpdate data) {
        Optional<Todo> optionalTodo = repository.findById(id);

        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();

            // Check if the 'title' field is provided and update it if not null
            if (data.title() != null) {
                todo.setTitle(data.title());
            }

            // Check if the 'description' field is provided and update it if not null
            if (data.description() != null) {
                todo.setDescription(data.description());
            }

                todo.setCompleted(data.completed());

            repository.save(todo);
            return ResponseEntity.ok(todo);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTodo(@PathVariable String id) {
        Optional<Todo> optionalTodo = repository.findById(id);
        if (optionalTodo.isPresent()) {
            repository.deleteById(id);
        return ResponseEntity.noContent().build();
        } else {
            throw new EntityNotFoundException();
        }
    }
}
