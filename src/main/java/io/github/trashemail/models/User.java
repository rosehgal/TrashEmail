package io.github.trashemail.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="UserEmailToChatIdMapping")
public class User {
	@Id
	@GeneratedValue
	private Integer id;
	private Integer chatId;

	@Column(unique = true)
	private String emailId;

	private String forwardsTo;
	
	public User() {}
	
	public User(Integer chatId, String emailId, String forwardsTo) {
		this.chatId = chatId;
		this.emailId = emailId;
		this.forwardsTo = forwardsTo;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getChatId() {
		return chatId;
	}
	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getForwardsTo() {
		return forwardsTo;
	}

	public void setForwardsTo(String forwardsTo) {
		this.forwardsTo = forwardsTo;
	}

	@Override
	public java.lang.String toString() {
		return this.emailId;
	}
}
