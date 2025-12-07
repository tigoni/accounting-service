package com.pezesha.taskproject.accounting_service.internal.utils;

public class Utils {
    public static String normalizeAccountName(String accountName) {
        if (accountName == null) {
            return null;
        }
        return accountName.replace('-', ' ');
    }
}
