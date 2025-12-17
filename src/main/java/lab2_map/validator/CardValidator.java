package lab2_map.validator;

import lab2_map.domain.Card;
import lab2_map.domain.Duck;

public class CardValidator implements ValidationStrategy<Card<Duck>> {

    @Override
    public void validate(Card<Duck> card) {

        if (card == null)
            throw new ValidationException("Card cannot be null");

        if( card.getName() == null || card.getName().isBlank())
            throw new ValidationException("Card name cannot be empty");

        if (card.getMembers() == null)
            throw new ValidationException("Card members list cannot be null");

        for (Duck d : card.getMembers()) {
            if (d == null)
                throw new ValidationException("Card contains a null duck");

            if (d.getSpeed() <= 0)
                throw new ValidationException("Duck speed must be positive");

            if (d.getEndurance() <= 0)
                throw new ValidationException("Duck endurance must be positive");
        }
    }
}
