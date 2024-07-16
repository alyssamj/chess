package server;

import com.google.gson.Gson;

public class Handler {
    private Handler() {};

    public static <T> T fromJson(String json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
    public static <T> String toJson(T type) {
        Gson gson = new Gson();
        return gson.toJson(type);
    }

}
