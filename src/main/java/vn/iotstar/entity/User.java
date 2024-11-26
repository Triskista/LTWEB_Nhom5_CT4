package vn.iotstar.entity;

import java.io.Serializable;
import java.sql.Date;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="USERS")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="UserId")
	private int userid;
	
	@Column(name="UserName", columnDefinition = "NVARCHAR(100) NOT NULL")
	private String username;
	
	@Column(name="Password", columnDefinition = "NVARCHAR(200) NOT NULL")
	private String password;
	
	@Column(name="Email", columnDefinition = "NVARCHAR(200) NOT NULL")
	private String email;
	
	@Column(name="Phone", columnDefinition = "NVARCHAR(200) NOT NULL")
	private String phone;
	
	@ManyToOne
    @JoinColumn(name = "RoleId", referencedColumnName = "RoleId")  
    private int roleid;
	
	@Column(name="Status")
	private int status;
}