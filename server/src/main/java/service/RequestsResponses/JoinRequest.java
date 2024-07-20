package service.RequestsResponses;

public record JoinRequest(String authToken, String playerColor, Integer gameID) {
}
