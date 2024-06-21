package dev.dubhe.sms.manager.util;

import java.util.function.Supplier;

public class DataUtil {
    public static <T> T validate(T t, Supplier<RuntimeException> throwableSupplier) {
        if (null == t) throw throwableSupplier.get();
        return t;
    }
}
