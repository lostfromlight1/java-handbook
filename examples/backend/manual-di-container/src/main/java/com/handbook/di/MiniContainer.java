package com.handbook.di;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Minimal IoC container: maps an interface type to an implementation class and builds
 * objects via single-arg constructor injection (teaches Spring's core idea in plain Java).
 */
public final class MiniContainer {
    private final Map<Class<?>, Class<?>> bindings = new HashMap<>();

    public <T> void bind(Class<T> iface, Class<? extends T> impl) {
        bindings.put(iface, impl);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        if (bindings.containsKey(type)) {
            Class<?> impl = bindings.get(type);
            return (T) createInstance(impl);
        }
        return (T) createWithConstructorInjection(type);
    }

    private Object createInstance(Class<?> impl) {
        try {
            Constructor<?> ctor = impl.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot instantiate " + impl.getName(), e);
        }
    }

    private Object createWithConstructorInjection(Class<?> type) {
        Constructor<?>[] ctors = type.getDeclaredConstructors();
        Constructor<?> ctor = ctors[0];
        Class<?>[] paramTypes = ctor.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            args[i] = get(paramTypes[i]);
        }
        try {
            ctor.setAccessible(true);
            return ctor.newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot construct " + type.getName(), e);
        }
    }

    public void registerSingleton(Class<?> type, Object instance) {
        Objects.requireNonNull(instance);
        bindings.put(type, instance.getClass());
    }
}
