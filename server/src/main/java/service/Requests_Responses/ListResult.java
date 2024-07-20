package service.Requests_Responses;

import model.GameData;

import java.util.Collection;

public record ListResult(ArrayListResult[] games, String message) {
}
