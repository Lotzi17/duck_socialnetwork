package lab2_map.domain;

/**
 * Represents a duck user in the DuckSocialNetwork.
 * Ducks can swim, fly, participate in events, and belong to a card.
 */
public class Duck extends User implements Zburator, Inotator {

    private UserType type;
    private double speed;
    private double endurance;
    private Card<Duck> card;

    public Duck(Long id, String username, String email, String password,
                UserType type, double speed, double endurance) {
        super(id, username, email, password);
        this.type = type;
        this.speed = speed;
        this.endurance = endurance;
    }

    public UserType getType() {
        return type;
    }

    public double getSpeed() {
        return speed;
    }

    public double getEndurance() {
        return endurance;
    }

    public Card<Duck> getCard() {
        return card;
    }

    public void setCard(Card<Duck> card) {
        this.card = card;
    }

    @Override
    public void fly() {
        if (type == UserType.FLYING || type == UserType.FLYING_AND_SWIMMING)
            System.out.println(username + " is flying gracefully!");
        else
            System.out.println(username + " cannot fly!");
    }

    @Override
    public void swim() {
        if (type == UserType.SWIMMING || type == UserType.FLYING_AND_SWIMMING)
            System.out.println(username + " is swimming at speed " + speed);
        else
            System.out.println(username + " cannot swim!");
    }

    @Override
    public String toString() {
        return "Duck: " + username +
                " [" + type +
                ", speed=" + speed +
                ", endurance=" + endurance +
                "]";
    }
}
