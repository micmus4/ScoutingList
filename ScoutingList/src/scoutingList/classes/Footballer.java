package scoutingList.classes;

public final class Footballer {

    private final String name;
    private final String nationality;
    private final String manager;
    private final String dateOfBirth;
    private final String club;
    private final String preferredFoot;
    private final String marketValue;
    private final String weight;
    private final String height;
    private final String contractExpirationDate;

    public Footballer(String name, String nationality, String manager, String dateOfBirth, String club, String preferredFoot, String marketValue,
                      String weight, String height, String contractExpirationDate) {
        this.name = name;
        this.nationality = nationality;
        this.manager = manager;
        this.dateOfBirth = dateOfBirth;
        this.club = club;
        this.preferredFoot = preferredFoot;
        this.marketValue = marketValue;
        this.weight = weight;
        this.height = height;
        this.contractExpirationDate = contractExpirationDate;
    }

    public String getName() {
        return name;
    }


    public String getNationality() {
        return nationality;
    }


    public String getManager() {
        return manager;
    }


    public String getDateOfBirth() {
        return dateOfBirth;
    }


    public String getClub() {
        return club;
    }


    public String getPreferredFoot() {
        return preferredFoot;
    }


    public String getMarketValue() {
        return marketValue;
    }

    public String getWeight() {
        return weight;
    }


    public String getHeight() {
        return height;
    }

    public String getContractExpirationDate() {
        return contractExpirationDate;
    }

}
