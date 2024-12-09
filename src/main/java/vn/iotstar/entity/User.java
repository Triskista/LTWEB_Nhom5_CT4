package vn.iotstar.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Users")
public class User implements UserDetails {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "username", columnDefinition = "nvarchar(255)")
	@NotEmpty(message = "khong duoc rong")
    private String username;

    @Column(name = "password", columnDefinition = "nvarchar(255)")
	@NotEmpty(message = "khong duoc rong")
    private String password;

    @Column(name = "email", columnDefinition = "nvarchar(255)")
	@NotEmpty(message = "khong duoc rong")
    private String email;

    @Column(name = "phone", columnDefinition = "nvarchar(10)")
	@NotEmpty(message = "khong duoc rong")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean status;

	/*
	 * public User(String username, String password, String email, String phone,
	 * Role role, Boolean status) { this.username = username; this.password =
	 * password; this.email = email; this.phone = phone; this.role = role;
	 * this.status = status; }
	 */
    
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	return List.of(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


	public User(Integer userId, String username, String password, String email, String phone, Role role, Boolean status) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.role = role;
		this.status = status;
	}
    
    
}
