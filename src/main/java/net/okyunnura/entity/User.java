package net.okyunnura.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class User {

	public enum Role {
		UPLOAD,
		DOWNLOAD
	}

	@Id
	private String username;

	@Column(nullable=false)
	private String password;

	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(nullable=false)
	private LocalDateTime expiredAt;
}
