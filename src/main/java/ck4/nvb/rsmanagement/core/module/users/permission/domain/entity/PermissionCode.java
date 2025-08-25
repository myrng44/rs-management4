package ck4.nvb.rsmanagement.core.module.users.permission.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionCode {
    // Order Management
    CREATE_ORDER("CREATE_ORDER"),
    VIEW_ORDER("VIEW_ORDER"),
    UPDATE_ORDER("UPDATE_ORDER"),

    // Product Management
    CREATE_PRODUCT("CREATE_PRODUCT"),
    VIEW_PRODUCT("VIEW_PRODUCT"),
    UPDATE_PRODUCT("UPDATE_PRODUCT"),
    DELETE_PRODUCT("DELETE_PRODUCT"),

    // User Management
    CREATE_USER("CREATE_USER"),
    VIEW_USER("VIEW_USER"),
    UPDATE_USER("UPDATE_USER"),
    DELETE_USER("DELETE_USER"),

    // Category Management
    CREATE_CATEGORY("CREATE_CATEGORY"),
    VIEW_CATEGORY("VIEW_CATEGORY"),
    UPDATE_CATEGORY("UPDATE_CATEGORY"),
    DELETE_CATEGORY("DELETE_CATEGORY"),

    // Customer Management
    CREATE_CUSTOMER("CREATE_CUSTOMER"),
    VIEW_CUSTOMER("VIEW_CUSTOMER"),
    UPDATE_CUSTOMER("UPDATE_CUSTOMER"),
    DELETE_CUSTOMER("DELETE_CUSTOMER"),

    // Payment operations
    CREATE_PAYMENT("CREATE_PAYMENT"),
    VIEW_PAYMENT("VIEW_PAYMENT"),
    UPDATE_PAYMENT("UPDATE_PAYMENT"),
    DELETE_PAYMENT("DELETE_PAYMENT"),

    // Voucher Management
    CREATE_VOUCHER("CREATE_VOUCHER"),
    VIEW_VOUCHER("VIEW_VOUCHER"),
    UPDATE_VOUCHER("UPDATE_VOUCHER"),
    DELETE_VOUCHER("DELETE_VOUCHER"),

    // Store Management
    CREATE_STORE("CREATE_STORE"),
    UPDATE_STORE("UPDATE_STORE"),
    DELETE_STORE("DELETE_STORE"),
    VIEW_STORE_REPORT("VIEW_STORE_REPORT"),
    MANAGE_STORE_SETTINGS("MANAGE_STORE_SETTINGS");

    private final String code;

    public static PermissionCode fromCode(String code) {
        for (PermissionCode permission : values()) {
            if (permission.code.equals(code)) {
                return permission;
            }
        }
        throw new IllegalArgumentException("Unknown permission code: " + code);
    }
}
