package test;

import model.User;

public class TestRest {

    private final static RestUser usersClient = new RestUser();
    public static void main(String[] args) {
        User user = new User("user1", "user1");
        try {
            show(() -> user.setId(usersClient.create(user)));
            show(() -> System.out.println(user));

            show(() -> {
                User[] res = usersClient.getAll();
                for (User u : res) {
                    System.out.println(u);
                }
            });
            user.setName("user2");
            usersClient.update(user);
            show(() -> System.out.println(usersClient.getById(user.getId().toString())));
            usersClient.delete(user.getId().toString());
            show(() -> {
                User[] res = usersClient.getAll();
                for (User u : res) {
                    System.out.println(u);
                }
            });
        } catch(Exception ex){
            System.out.println("Exception ... " + ex.getMessage());
        }
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            System.out.println("RestClientException" + e);
        }
    }
}