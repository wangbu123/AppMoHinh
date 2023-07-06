package com.example.foodapp.Support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckEmail {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern pattern;
    private Matcher matcher;

    public CheckEmail() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }
    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
