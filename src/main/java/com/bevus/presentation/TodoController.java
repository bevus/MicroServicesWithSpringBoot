package com.bevus.presentation;

import com.bevus.Service.TodoService;
import com.bevus.bean.Todo;
import com.bevus.exception.TodoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/users/{name}/todos")
    public List<Resource<Todo>> retrieveTodos(@PathVariable String name) {
        return todoService.retrieveTodos(name).stream()
                .map(this::todoResource)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/users/{name}/todos/{id}")
    public Resource<Todo> retrieveTodo(@PathVariable String name, @PathVariable int id) {
        Todo todo = todoService.retrieveTodo(id);
        if (todo == null) {
            throw new TodoNotFoundException("Todo not found");
        }
        Resource<Todo> todoResource = new Resource<>(todo);
        ControllerLinkBuilder linkToRetrieveTodos = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).retrieveTodos(name));
        todoResource.add(linkToRetrieveTodos.withRel("parent"));
        return todoResource;
    }

    @PostMapping("/users/{name}/todos")
    ResponseEntity add(@PathVariable String name, @RequestBody Todo todo) {
        Todo createdTodo = todoService.addTodo(name, todo.getDesc(), todo.getTargetDate(), todo.getDone());
        if (createdTodo == null) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdTodo.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    // HATEOAS : generation _self link for a todo
    private Resource<Todo> todoResource(Todo todo) {
        ControllerLinkBuilder linkToTodo = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).retrieveTodo(todo.getUser(), todo.getId()));
        Resource<Todo> todoResource = new Resource<>(todo);
        todoResource.add(linkToTodo.withSelfRel());
        return todoResource;
    }
}