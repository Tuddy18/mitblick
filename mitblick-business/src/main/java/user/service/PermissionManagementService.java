package user.service;

import exception.BusinessException;
import exception.ExceptionCode;
import user.dao.PermissionPersistenceManager;
import user.dao.UserPersistenceManager;
import user.entities.Permission;
import user.entities.Role;
import user.entities.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Stateless
public class PermissionManagementService {

    @EJB
    private PermissionPersistenceManager permissionPersistenceManager;

    @EJB
    private UserPersistenceManager userPersistenceManager;


    public void createPermission(Permission permission) {
        permissionPersistenceManager.createPermission(permission);
    }

    public void createRole(Role role) {
        permissionPersistenceManager.createRole(role);
    }


    /**
     * Adds a role to a user. If the role type does not exist it will automatically add the new role type to the database.
     * If the user  already has the role, this method will do nothing.
     *
     * @param roleType
     * @param email
     */
    public void addRoleToUser(String roleType, String email) throws BusinessException {

        Optional<User> userOptional = userPersistenceManager.getUserByEmail(email);
        Role role = addRoleIfNotExists(roleType);
        if (!userOptional.isPresent()) {
            throw new BusinessException(ExceptionCode.EMAIL_NOT_FOUND);
        } else {
            User user = userOptional.get();
            //The adding gets done here
            normalizeUserRoles(user);
            user.getRoles().add(role);

        }

    }


    /**
     * Adds a permission to a role. If the permission type or role type don't exist it will automatically add them to the database.
     * If the role already has the permission this method will do nothing.
     *
     * @param permissionType
     * @param roleType
     */
    public void addPermissionToRole(String permissionType, String roleType) {

        Role role = addRoleIfNotExists(roleType);
        Permission permission = addPermissionIfNotExists(permissionType);
        normalizesRolePermissions(role);
        if (!role.getPermissions().contains(permission)) {
            role.getPermissions().add(permission);
        }
    }

    /**
     * Revokes (deletes) a role from a user. If the user doesn't have the role to be revoked, the method will do nothing.
     * If the specified roleType doesn't exist, nothing happens.
     *
     * @param roleType
     * @param email
     */
    public void revokeRoleFromUser(String roleType, String email) throws BusinessException {

        Optional<User> userOptional = userPersistenceManager.getUserByEmail(email);
        Optional<Role> roleOptional = permissionPersistenceManager.getRoleByType(roleType);
        if (roleOptional.isPresent()) {
            if (!userOptional.isPresent()) {
                throw new BusinessException(ExceptionCode.EMAIL_NOT_FOUND);
            } else {
                User user = userOptional.get();
                normalizeUserRoles(user);
                user.getRoles().remove(roleOptional.get());

            }
        }
    }


    /**
     * Revokes (deletes) a permission from a role. If the role doesn't have the permission to be revoked, the method will do nothing.
     * If the specified permissionType doesn't exist, nothing happens.
     *
     * @param permissionType
     * @param roleType
     */
    public void revokePermissionFromRole(String roleType, String permissionType) {
        Optional<Role> roleOptional = permissionPersistenceManager.getRoleByType(roleType);
        Optional<Permission> permissionOptional = permissionPersistenceManager.getPermissionByType(permissionType);

        if (permissionOptional.isPresent() && roleOptional.isPresent()) {
            normalizesRolePermissions(roleOptional.get());
            roleOptional.get().getPermissions().remove(permissionOptional.get());
        }
    }


    private void normalizeUserRoles(User user) {
        if (user.getRoles() == null) {
            user.setRoles(new LinkedList<>());
        }
    }

    private void normalizesRolePermissions(Role role) {
        if (role.getPermissions() == null) {
            role.setPermissions(new LinkedList<>());
        }
    }

    private Role addRoleIfNotExists(String roleType) {
        Optional<Role> roleOptional = permissionPersistenceManager.getRoleByType(roleType);
        Role role;
        if (!roleOptional.isPresent()) {
            role = new Role();
            role.setType(roleType);
            permissionPersistenceManager.createRole(role);
        } else {
            role = roleOptional.get();
        }
        return role;
    }

    private Permission addPermissionIfNotExists(String permissionType) {
        Optional<Permission> permissionOptional = permissionPersistenceManager.getPermissionByType(permissionType);

        Permission permission;
        if (!permissionOptional.isPresent()) {
            permission = new Permission();
            permission.setType(permissionType);
            permissionPersistenceManager.createPermission(permission);
        } else {
            permission = permissionOptional.get();
        }
        return permission;
    }

    public List<Permission> getPermissionsByRole(String roleType) throws BusinessException {
        Optional<Role> roleByType = permissionPersistenceManager.getRoleByType(roleType);
        if (roleByType.isPresent()) {
            return roleByType.get().getPermissions();
        } else {
            throw new BusinessException(ExceptionCode.ROLE_DOESNT_EXIST);
        }
    }

    public List<Permission> getAllPermissions() {
        return permissionPersistenceManager.getAllPermissions();
    }

    public List<Role> getAllRoles() {
        return permissionPersistenceManager.getAllRoles();
    }
}
