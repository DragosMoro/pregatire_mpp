package test;

import model.User;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class RestUser {
    public static final String URL = "http://localhost:8080/users";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, User[].class));
    }

    public User getById(String id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), User.class));
    }

    public Integer create(User test) {
        return execute(() -> restTemplate.postForObject(URL, test, Integer.class));
    }

    public void update(User test) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, test.getId()), test);
            return null;
        });
    }

    public void delete(String id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }
}
