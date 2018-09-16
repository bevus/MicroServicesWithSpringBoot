package com.bevus.presentation;

import com.bevus.Service.TodoService;
import com.bevus.bean.Todo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoService service;

    @Before
    public void setUp() {
        List<Todo> mockList = Arrays.asList(
                new Todo(1, "Jack", "Learn Spring MVC", new Date(), false),
                new Todo(2, "Jack", "Learn Struts", new Date(), false));

        Todo mockTodo = new Todo(3, "Jack", "Learn Spring MVC", new Date(), false);

        when(service.retrieveTodos(anyString())).thenReturn(mockList);
        when(service.retrieveTodo(anyInt())).thenReturn(mockList.get(0));
        when(service.addTodo(anyString(), anyString(), isNull(), anyBoolean())).thenReturn(mockTodo);
    }

    @Test
    public void retrieveTodos() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/users/Jack/todos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String expected = "[" +
                "{id:1,user:Jack,desc:\"Learn Spring MVC\",done:false}," +
                "{id:2,user:Jack,desc:\"Learn Struts\",done:false}" +
                "]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void retrieveTodo() throws Exception {
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get("/users/Jack/todos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String expected = "{id:1,user:Jack,desc:\"Learn Spring MVC\",done:false}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

    }

    @Test
    public void createTodo() throws Exception {
        String todo = "{\"user\":\"Jack\",\"desc\":\"Learn Spring MVC\", \"done\":false}";

        mvc.perform(MockMvcRequestBuilders.post("/users/Jack/todos").content(todo).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/users/Jack/todos/" + 3)));
    }
}