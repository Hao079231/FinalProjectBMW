package Beans;

import java.sql.Date;

public class Users {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String role;

    // Constructors
    public Users(int userId, String username, String password, String email, String phone, String address, String role) {
        this.userId= userId;
        this.username=username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address= address;
        this.role= role;
    }

	public Users() {}
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	
}
