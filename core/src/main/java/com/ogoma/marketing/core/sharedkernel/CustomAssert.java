package com.ogoma.marketing.core.sharedkernel;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A clean, expressive utility for domain-level assertions.
 * Bounded to RuntimeException to prevent checked exception boilerplate.
 */
public final class CustomAssert {

    // Prevent instantiation
    private CustomAssert() {}

    /**
     * Asserts a boolean expression to be true.
     */
    public static <X extends RuntimeException> void isTrue(boolean expression, Supplier<? extends X> exceptionSupplier) {
        if (!expression) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Asserts a boolean expression to be true (typically used for internal state validation).
     */
    public static <X extends RuntimeException> void state(boolean expression, Supplier<? extends X> exceptionSupplier) {
        if (!expression) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Asserts that an object is not null.
     */
    public static <X extends RuntimeException> void notNull(Object object, Supplier<? extends X> exceptionSupplier) {
        if (object == null) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Asserts that an object is null.
     */
    public static <X extends RuntimeException> void isNull(Object object, Supplier<? extends X> exceptionSupplier) {
        if (object != null) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Asserts that a string is not empty (must have a length greater than 0).
     */
    public static <X extends RuntimeException> void hasLength(String text, Supplier<? extends X> exceptionSupplier) {
        if (text == null || text.isEmpty()) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Asserts that a string has actual text (is not null, not empty, and not whitespace-only).
     */
    public static <X extends RuntimeException> void hasText(String text, Supplier<? extends X> exceptionSupplier) {
        if (text == null || text.isBlank()) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Asserts that a string does not contain a specific substring.
     */
    public static <X extends RuntimeException> void doesNotContain(String textToSearch, String substring, Supplier<? extends X> exceptionSupplier) {
        if (textToSearch != null && substring != null && textToSearch.contains(substring)) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Asserts that an array is not empty (not null and has at least one element).
     */
    public static <X extends RuntimeException> void notEmpty(Object[] array, Supplier<? extends X> exceptionSupplier) {
        if (array == null || array.length == 0) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Asserts that a collection is not empty.
     */
    public static <X extends RuntimeException> void notEmpty(Collection<?> collection, Supplier<? extends X> exceptionSupplier) {
        if (collection == null || collection.isEmpty()) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Asserts that a map is not empty.
     */
    public static <X extends RuntimeException> void notEmpty(Map<?, ?> map, Supplier<? extends X> exceptionSupplier) {
        if (map == null || map.isEmpty()) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Asserts that an array contains no null elements.
     */
    public static <X extends RuntimeException> void noNullElements(Object[] array, Supplier<? extends X> exceptionSupplier) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw exceptionSupplier.get();
                }
            }
        }
    }

    /**
     * Asserts that a collection contains no null elements.
     */
    public static <X extends RuntimeException> void noNullElements(Collection<?> collection, Supplier<? extends X> exceptionSupplier) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw exceptionSupplier.get();
                }
            }
        }
    }

    /**
     * Asserts that the provided object is an instance of the provided class.
     */
    public static <X extends RuntimeException> void isInstanceOf(Class<?> type, Object obj, Supplier<? extends X> exceptionSupplier) {
        notNull(type, exceptionSupplier);
        if (!type.isInstance(obj)) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Asserts that subType is assignable to superType.
     */
    public static <X extends RuntimeException> void isAssignable(Class<?> superType, Class<?> subType, Supplier<? extends X> exceptionSupplier) {
        notNull(superType, exceptionSupplier);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw exceptionSupplier.get();
        }
    }
}
