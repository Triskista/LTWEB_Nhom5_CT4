package vn.iotstar.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.iotstar.entity.Role;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterUserModel {
    private String email;
    private String password;
    private String userName;
    private String phone;
    private Role role;
    private Boolean status;
    
}
