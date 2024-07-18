package service.Requests_Responses;

public record LoginResult(String username, String authToken) {
//    public boolean isSuccess() {
//        return authToken != null && !authToken.isEmpty();
//    }
}
