package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {

    public static void injectObject(Object target, String fieldName, Object toInject) {
        try {
            boolean wasPrivate = false;

            final Field declaredField = target.getClass().getDeclaredField(fieldName);

            if (!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
                wasPrivate = true;
            }

            declaredField.set(target, toInject);
            if (wasPrivate) {
                declaredField.setAccessible(false);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

}
