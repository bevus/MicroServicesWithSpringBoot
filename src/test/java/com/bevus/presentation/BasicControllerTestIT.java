package com.bevus.presentation;

import com.bevus.Application;
import com.bevus.bean.Todo;
import com.bevus.bean.WelcomeBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.Date;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicControllerTestIT {
    private static final String LOCAL_HOST = "http://localhost:";

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate template = new TestRestTemplate();

    @Test
    public void welcome() {
        ResponseEntity<String> response = template.getForEntity(createURL("/welcome"), String.class);
        assertThat(response.getBody(), equalTo("Hello World"));
    }

    @Test
    public void welcomeWithObject() {
        ResponseEntity<String> response = template.getForEntity(createURL("/welcome-with-object"), String.class);

        assertThat(response.getBody(), containsString("Hello World"));
    }

    @Test
    public void welcomeWithParameter() {
        ResponseEntity<WelcomeBean> response = template.getForEntity(createURL("/welcome-with-parameter/name/Buddy"), WelcomeBean.class);

        assertThat(response.getBody().getMessage(), containsString("Hello World, Buddy"));
    }

    @Test
    public void addTodo() {
        Todo todo = new Todo(-1, "Jill", "Learn Hibernate", new Date(), false);

        URI location = template.postForLocation(createURL("/users/Jill/todos"),todo);

        assertThat(location.getPath(), containsString("/users/Jill/todos/4"));
    }

    private String createURL(String uri) {
        return LOCAL_HOST + port + uri;
    }
}
