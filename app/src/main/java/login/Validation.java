package login;

import login.exception_handlers.PasswordPolicyException;
import login.exception_handlers.PasswordValidationException;

/**
 * COP 4078 Exercise: 5
 * File Name: Validation.java
 * 
 * The Validation class handles validation of user input when logging
 * in to the program to ensure the program doesn't crash or get hacked. 
 * It checks for SQL Injection, Password Policy, and Interger Overflow.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @apiNote Added in version 1.1.
 * @apiNote Added validation for Cryptographer class in version 1.2.
 * @apiNote Removed Cryptographer class validations as Handler classes now use
 * this class to verify that information in version 1.4.
 */
public class Validation {
    //#region CONSTANTS
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 12;
    private static final int MIN_CODE_LENGTH     = 10;
    //#endregion CONSTANTS

    //#region SINGLETON PATTERN
    private static Validation _instance = null;

    private Validation() {}

    public static Validation GetInstance() {
        if(_instance == null) {
            _instance = new Validation();
        }
        return _instance;
    }
    //#endregion SINGLETON PATTERN

    //#region VALIDATION FUNCTIONS
    /**
     * Takes in the username from the UsernameHandler class and
     * checks it against various validation methods to ensure that the input is valid.
     * 
     * @param username Username from the UsernameHandler class.
     * @return {@code true} if the input is valid, {@code false} if otherwise.
     * @see UsernameHandler
     * @apiNote Moved from Login class in version 1.1 and updated logic
     * to work with new validation methods.
     * @apiNote Renamed from IsUsernameValid to ValidateUsername in version 1.4.
     * Rewrote all logic to simplify function.
     */
    public boolean ValidateUsername(String username) {
        String value = "Username";

        if(username.isEmpty()) {
            MessageHandler.PrintMessage(MessageHandler.EMPTY_INPUT, value);
            return false;
        }
        
        if(!SQLInjectionCheck(username)) {
            MessageHandler.PrintMessage(MessageHandler.SQL_INJECTION);
            return false;
        }

        return true;
    }

    /**
     * Takes in the password from the PasswordHandler class and
     * checks it against various validation methods to ensure that the input is valid.
     * 
     * @param password Password from the PasswordHandler class.
     * @return {@code true} if the input is valid, {@code false} if otherwise.
     * @see PasswordHandler
     * @apiNote Moved from Login class in version 1.1 and updated logic
     * to work with new validation methods.
     * @apiNote Renamed from IsPasswordValid to ValidatePassword in version 1.4.
     * Rewrote all logic to simplify function.
     */
    public void ValidatePassword(String password) throws PasswordValidationException {
        String value = "Password";

        // Check if password is empty.
        if(password.isEmpty()) {
            throw new PasswordValidationException(MessageHandler.EMPTY_INPUT, value);
        }
        
        // Check if password contains SQL chars.
        if(!SQLInjectionCheck(password)) {
            throw new PasswordValidationException(MessageHandler.SQL_INJECTION, value);
        }

        // Check if password adheres to policy.
        try {
            PasswordPolicyCheck(password);
        }
        catch(PasswordPolicyException e) {
            throw new PasswordValidationException(e.getMessage());
        }
    }

    /**
     * Takes in the MFA Code from the CodeHandler class and
     * checks it against various validation methods to ensure that the input is valid.
     * 
     * @param code MFA Code from the CodeHandler class.
     * @return {@code true} if the input is valid, {@code false} if otherwise.
     * @see CodeHandler
     * @apiNote Moved from Login class in version 1.1 and updated logic
     * to work with new validation methods.
     * @apiNote Updated in version 1.2 to use Integer.parseInt() method.
     * @apiNote Renamed from IsMFAValid to ValidateCode in version 1.4.
     * Rewrote all logic to simplify function.
     */
    public boolean ValidateCode(String code) {
        String value = "MFA Code";

        // Check if code is empty.
        if(code.isEmpty()) {
            MessageHandler.PrintMessage(MessageHandler.EMPTY_INPUT, value);
            return false;
        }

        // Check if code contains non-digits or overflows.
        if(!IntOverflowCheck(code)) {
            MessageHandler.PrintMessage(MessageHandler.INVALID_INPUT, value);
            return false;
        }

        // Check if code is less than 10 digits.
        if(code.length() < MIN_CODE_LENGTH) {
            MessageHandler.PrintMessage(MessageHandler.POLICY_FAILED, value);
            return false;
        }

        return true;
    }
    //#endregion VALIDATION FUNCTIONS

    //#region POLICY FUNCTIONS
    /**
     * Checks if user-entered data contains any SQL chars to protect against SQL Injection.
     * 
     * @param input User-entered data.
     * @return {@code true} if all checks passed, {@code false if otherwise}
     * @apiNote Added in version 1.1.
     * @apiNote Changed function to return a bool instead of a String in version 1.4.
     */
    private boolean SQLInjectionCheck(String input) {
        char[] illegalChars = {
            '/', '\\', '(', ')', '<', '>', '\'', '\"', 
            '+', '-', '=', ';', '|',
            '\n', '\r'
        };

        for(char c : input.toCharArray()) {
            for(char i : illegalChars) {
                if(c == i) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Takes in user password from {@link #ValidatePassword(String)}
     * and checks if it meets all policies for a good password.
     * 
     * @param input User password from {@link #ValidatePassword(String)}.
     * @return {@code true} if all checks passed, {@code false if otherwise}
     * @apiNote Added in version 1.1.
     * @apiNote Added check for only letters or numbers in password in version 1.2.
     * @apiNote Changed function to return a bool instead of a String in version 1.4.
     */
    private void PasswordPolicyCheck(String input) throws PasswordPolicyException {
        String value = "Password";

        // Check if password is between 8-12 chars (included).
        if(input.length() < MIN_PASSWORD_LENGTH ||
           input.length() > MAX_PASSWORD_LENGTH) {
            throw new PasswordPolicyException(MessageHandler.POLICY_FAILED, value);
        }

        // Check if password contains only valid characters (A-Z, a-z, 0-9)
        if(!input.matches("[A-Za-z0-9]+")) {
            throw new PasswordPolicyException(MessageHandler.POLICY_FAILED, value);
        }

        // Check for password policies.
        boolean hasUpper  = false;
        boolean hasLower  = false;
        boolean hasNumber = false;
        for(char c : input.toCharArray()) {
            if(Character.isUpperCase(c)) hasUpper      = true;
            else if(Character.isLowerCase(c)) hasLower = true;
            else if(Character.isDigit(c)) hasNumber    = true;
        }

        if(!hasUpper || !hasLower || !hasNumber) {
            throw new PasswordPolicyException(MessageHandler.POLICY_FAILED, value);
        }
    }

    /**
     * Takes in MFA Code from {@link #ValidateCode(String)}
     * and checks if it contains all digits and doesn't overflow.
     * 
     * @param input MFA Code from {@link #ValidateCode(String)}.
     * @return {@code true} if all checks passed, {@code false if otherwise}
     * @apiNote Added in version 1.1.
     * @apiNote Updated in version 1.2 to use built in Java function that checks for overflow.
     * @apiNote Changed function to return a bool instead of a String in version 1.4.
     * @implNote valueOf() function calls parseInt() with extra input validation checks.
     */
    private boolean IntOverflowCheck(String input) {
        try {
            Integer.valueOf(input);
            return true;
        } 
        catch(NumberFormatException e) {
            return false;
        }
    }
    //#endregion POLICY FUNCTIONS
}