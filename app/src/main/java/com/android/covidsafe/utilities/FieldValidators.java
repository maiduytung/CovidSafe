package com.android.covidsafe.utilities;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldValidators {

    public static Boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static Boolean isStringContainNumber(String text) {
        Pattern pattern = Pattern.compile(".*\\d.*");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static Boolean isStringLowerAndUpperCase(String text) {
        Pattern lowerCasePattern = Pattern.compile(".*[a-z].*");
        Pattern upperCasePattern = Pattern.compile(".*[A-Z].*");
        Matcher lowerCasePatterMatcher = lowerCasePattern.matcher(text);
        Matcher upperCasePatterMatcher = upperCasePattern.matcher(text);
        if (!lowerCasePatterMatcher.matches()) {
            return false;
        }
        return upperCasePatterMatcher.matches();
    }

    public static Boolean isStringContainSpecialCharacter(String text) {
        Pattern specialCharacterPattern = Pattern.compile("[^a-zA-Z0-9 ]");
        Matcher specialCharacterMatcher = specialCharacterPattern.matcher(text);
        return specialCharacterMatcher.find();
    }

}
