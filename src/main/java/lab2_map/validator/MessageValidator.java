package lab2_map.validator;

import lab2_map.domain.Message;

public class MessageValidator implements ValidationStrategy<Message> {

    @Override
    public void validate(Message message) {
        if (message.getSender() == null)
            throw new ValidationException("Sender cannot be null");

        if (message.getReceiver() == null)
            throw new ValidationException("Receiver cannot be null");

        if (message.getContent() == null || message.getContent().trim().isEmpty())
            throw new ValidationException("Message content cannot be empty");

        if (message.getContent().length() > 500)
            throw new ValidationException("Message content exceeds 500 characters");
    }
}
