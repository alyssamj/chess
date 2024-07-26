package requestsandresponses;

public record JoinRequest(String authToken, String playerColor, Integer gameID) {
}
