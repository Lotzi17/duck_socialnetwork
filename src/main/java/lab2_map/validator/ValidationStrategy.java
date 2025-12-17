package lab2_map.validator;

import lab2_map.validator.ValidationException;

/**
 * validation strategy interface
 */
public interface ValidationStrategy<T> {
    void validate(T entity) throws ValidationException;
}
