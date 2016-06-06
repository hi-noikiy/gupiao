package domain;

public class Account {
	private int id;
	private String username;
	private String palType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPalType() {
		return palType;
	}
	public void setPalType(String palType) {
		this.palType = palType;
	}
	@Override
	public String toString() {
		return "Account [id=" + id + ", username=" + username + ", palType=" + palType + "]";
	}
}
