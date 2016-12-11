package net.okyunnura.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
public class User {

	public enum Role {
		UPLOAD,
		DOWNLOAD
	}

	@Id
	private String username;

	@Column
	private String password;

	@Column
	@Enumerated(EnumType.STRING)
	private Role role;
}
