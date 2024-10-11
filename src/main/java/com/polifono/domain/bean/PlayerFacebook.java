package com.polifono.domain.bean;

import org.json.JSONObject;

import lombok.Getter;

public class PlayerFacebook {

    @Getter private Long id;
    @Getter private String firstName;
    private Integer timezone;
    @Getter private String email;
    private Boolean verified;
    private String middleName;
    private String gender;
    @Getter private String lastName;
    private String link;
    private String locale;
    @Getter private String name;
    private String updatedTime;

    public PlayerFacebook(JSONObject jsonUser) {

        String[] fields = JSONObject.getNames(jsonUser);

        for (String field : fields) {
            switch (field) {
            case "id" -> id = jsonUser.getLong("id");
            case "first_name" -> firstName = jsonUser.getString("first_name");
            case "timezone" -> timezone = jsonUser.getInt("timezone");
            case "email" -> email = jsonUser.getString("email");
            case "verified" -> verified = jsonUser.getBoolean("verified");
            case "middle_name" -> middleName = jsonUser.getString("middle_name");
            case "gender" -> gender = jsonUser.getString("gender");
            case "last_name" -> lastName = jsonUser.getString("last_name");
            case "link" -> link = jsonUser.getString("link");
            case "locale" -> locale = jsonUser.getString("locale");
            case "name" -> name = jsonUser.getString("name");
            case "updated_time" -> updatedTime = jsonUser.getString("updated_time");
            }
        }
    }

    @Override
    public String toString() {
        return "UserFacebook [id=" + id + ", firstName=" + firstName
                + ", timezone=" + timezone + ", email=" + email + ", verified="
                + verified + ", middleName=" + middleName + ", gender="
                + gender + ", lastName=" + lastName + ", link=" + link
                + ", locale=" + locale + ", name=" + name + ", updatedTime="
                + updatedTime + "]";
    }
}
