package lab2_map.validator;

import lab2_map.domain.User;

public class UserValidator implements ValidationStrategy<User> {

    @Override
    public void validate(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank())
            throw new ValidationException("username cannot be empty");

        if (user.getEmail() == null || !user.getEmail().contains("@"))
            throw new ValidationException("invalid email");

        if (user.getPassword() == null || user.getPassword().length() < 3)
            throw new ValidationException("password too short");
    }
}
