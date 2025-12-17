package lab2_map.domain;

import java.time.LocalDate;

/**
 * Represents a human user in the DuckSocialNetwork.
 */
public class Person extends User {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String occupation;
    private double empathyLevel;

    public Person(Long id, String username, String email, String password,
                  String firstName, String lastName, LocalDate birthDate,
                  String occupation, double empathyLevel) {
        super(id, username, email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.occupation = occupation;
        this.empathyLevel = empathyLevel;
    }

    public void createEvent(Event event) {
        System.out.println(username + " created a new event: " + event.getName());
    }

    public double getEmpathyLevel() {
        return empathyLevel;
    }

    @Override
    public String toString() {
        return "Person: " + firstName + " " + lastName +
                " (" + occupation + ", empathy=" + empathyLevel + ")";
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public String getOccupation() {
        return occupation;
    }

}
