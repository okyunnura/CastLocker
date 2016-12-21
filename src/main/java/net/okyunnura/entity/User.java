package net.okyunnura.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 200, nullable = false, unique = true)
	private String username;

	@Column(length = 500, nullable = false)
	private String password;

	@Column(length = 100, nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(nullable = false)
	private LocalDateTime expiredAt;

	@ManyToOne(fetch = FetchType.LAZY)
	private User parent;

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof User)) return false;
		return new EqualsBuilder().append(getId(), ((User) obj).getId()).isEquals();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [id=" + getId() + "]";
	}
}
