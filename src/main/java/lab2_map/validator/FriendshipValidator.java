package lab2_map.validator;

public class FriendshipValidator {

    public void validate(Long u1, Long u2) {
        if (u1.equals(u2))
            throw new ValidationException("a user cannot befriend himself");
    }
}
