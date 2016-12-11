package net.okyunnura.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;

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
