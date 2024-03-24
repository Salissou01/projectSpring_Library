package com.app.biblio.security;
public class JwtResponse {
    private String token;

    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public JwtResponse(String token) {
        this.token = token;
    }

    // Getters and setters
    // You can generate them automatically using your IDE or write them manually
}
