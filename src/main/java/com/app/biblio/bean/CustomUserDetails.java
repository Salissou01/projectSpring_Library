package com.app.biblio.bean;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
    private String telephone;
    private String cin;
    private boolean enabled;
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Long id;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String email, String telephone, String cin, boolean enabled,Long id) {
        super(username, password, authorities);
        this.email = email;
        this.telephone = telephone;
        this.cin = cin;
        this.enabled = enabled; 
    }

    // Getters et setters pour email, telephone, cin et enabled
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
