package com.dawn.api.identity.service;

public interface UserClientService {

    boolean existsByRolesName(String roleName);

    boolean findByRoleName(String roleName);
}
