package lab2_map.validator;

import lab2_map.domain.Event;

public class EventValidator implements ValidationStrategy<Event> {

    @Override
    public void validate(Event e) {
        if (e.getName() == null || e.getName().isBlank())
            throw new ValidationException("event name cannot be empty");
    }
}
