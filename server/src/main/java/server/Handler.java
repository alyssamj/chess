package server;

import com.google.gson.Gson;

public class Handler {
    public Handler() {};

    public <T> Object fromJson(String json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
    public static <T> String toJson(T type) {
        Gson gson = new Gson();
        return gson.toJson(type);
    }

}
