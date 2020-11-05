package util;

import model.User;

public class UserSession {

    private static final ThreadLocal<User> context = new ThreadLocal<>();

    public static void setUser(User user) {
        context.set(user);
    }

    public static User getUser() {
        return context.get();
    }

    public static void removeUser() {
        context.remove();
    }
}
