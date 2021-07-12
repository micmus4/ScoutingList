package scoutingList.classes;

public class Account {

    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final String login;
    private final String password;

    public Account(String firstName, String lastName, String emailAddress, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.login = login;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
