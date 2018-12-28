package com.veeville.farm.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant C on 26/09/18.
 * this class contain different country name with its code which will used in mobile number verification
 */
public class Countries {
    public String countryName;
    public String countryCode;

    private Countries(String countryCode, String countryName) {
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public Countries() {

    }

    public List<Countries> getCountries() {

        List<Countries> countries = new ArrayList<>();
        countries.add(new Countries("+93", "Afghanistan"));
        countries.add(new Countries("+61", "Australia"));
        countries.add(new Countries("+880", "Bangladesh"));
        countries.add(new Countries("+61", "Australia"));
        countries.add(new Countries("+975", "Bhutan"));
        countries.add(new Countries("+55", "Brazil"));
        countries.add(new Countries("+1", "Canada"));
        countries.add(new Countries("+86", "China"));
        countries.add(new Countries("+33", "France"));
        countries.add(new Countries("+49", "Germany"));
        countries.add(new Countries("+91", "India"));
        countries.add(new Countries("+62", "Indonesia"));
        countries.add(new Countries("+972", "Israel"));
        countries.add(new Countries("+39", "Italy"));
        countries.add(new Countries("+81", "Japan"));
        countries.add(new Countries("+60", "Malaysia"));
        countries.add(new Countries("+52", "Mexico"));
        countries.add(new Countries("+977", "Nepal"));
        countries.add(new Countries("+64", "New Zealand"));
        countries.add(new Countries("+850", "North Korea"));
        countries.add(new Countries("+92", "Pakistan"));
        countries.add(new Countries("+351", "Portugal"));
        countries.add(new Countries("+7", "Russia"));
        countries.add(new Countries("+966", "Saudi Arabia"));
        countries.add(new Countries("+65", "Singapore"));
        countries.add(new Countries("+27", "South Africa"));
        countries.add(new Countries("+82", "South Korea"));
        countries.add(new Countries("+94", "Sri Lanka"));
        countries.add(new Countries("+41", "Switzerland"));
        countries.add(new Countries("+66", "Thailand"));
        countries.add(new Countries("+44", "United Kingdom"));
        countries.add(new Countries("+1", "United States of America"));

        return countries;
    }
}
