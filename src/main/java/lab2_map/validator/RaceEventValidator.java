package lab2_map.validator;

import lab2_map.domain.Duck;
import lab2_map.domain.RaceEvent;

import java.util.List;

public class RaceEventValidator implements ValidationStrategy<RaceEvent> {

    @Override
    public void validate(RaceEvent event) {

        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new ValidationException("RaceEvent name cannot be empty");
        }

        if (event.getDistances() == null || event.getDistances().isEmpty()) {
            throw new ValidationException("RaceEvent must have at least one distance");
        }

        for (Double d : event.getDistances()) {
            if (d == null || d <= 0) {
                throw new ValidationException("Distance values must be positive");
            }
        }

        if (event.getMaxParticipants() <= 0) {
            throw new ValidationException("Max participants must be greater than 0");
        }

        List<Duck> participants = event.getParticipants();
        if (participants == null || participants.isEmpty()) {
            throw new ValidationException("RaceEvent must have at least one eligible swimming duck");
        }

        if (participants.size() < event.getMaxParticipants()) {
            throw new ValidationException("Not enough ducks to fill all race lanes");
        }
    }
}
