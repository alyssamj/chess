package service.Requests_Responses;

public record JoinRequest(String authToken, String playerColor, Integer gameID) {
}
