package vn.iotstar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User {
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

    public User(String username, String password, String email, String phone, Role role, Boolean status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }
}
