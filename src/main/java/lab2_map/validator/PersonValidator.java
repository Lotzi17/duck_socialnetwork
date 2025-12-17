package lab2_map.validator;

import lab2_map.domain.Person;

public class PersonValidator implements ValidationStrategy<Person> {

    @Override
    public void validate(Person p) {
        if (p.getFirstName() == null || p.getFirstName().isBlank())
            throw new ValidationException("first name invalid");

        if (p.getLastName() == null || p.getLastName().isBlank())
            throw new ValidationException("last name invalid");

        if (p.getEmpathyLevel() < 0 || p.getEmpathyLevel() > 100)
            throw new ValidationException("empathy must be between 0 and 100");

        if (p.getBirthDate() == null)
            throw new ValidationException("birth date cannot be null");
    }
}
