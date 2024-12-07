package vn.iotstar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(name = "roleName", columnDefinition = "nvarchar(255)")
	@NotEmpty(message = "khong duoc rong")
    private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
