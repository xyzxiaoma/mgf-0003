package com.campus.club.utils;

public class UserContext {
    private static final ThreadLocal<Long> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> username = new ThreadLocal<>();
    private static final ThreadLocal<String> role = new ThreadLocal<>();

    public static void setUserId(Long id) {
        userId.set(id);
    }

    public static Long getUserId() {
        return userId.get();
    }

    public static void setUsername(String name) {
        username.set(name);
    }

    public static String getUsername() {
        return username.get();
    }

    public static void setRole(String r) {
        role.set(r);
    }

    public static String getRole() {
        return role.get();
    }

    public static void clear() {
        userId.remove();
        username.remove();
        role.remove();
    }

    public static boolean isAdmin() {
        return "ADMIN".equals(role.get());
    }
}
