package com.dawn.api.identity.service;

import com.dawn.api.identity.dto.RoleDTO;
import com.dawn.api.identity.dto.UserDTO;

public interface UserClientService {

    boolean existsByRolesName(String roleName);

    RoleDTO findByRoleName(String roleName);

    UserDTO findWithEmail(String email);
}
