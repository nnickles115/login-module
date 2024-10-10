package login.exception_handlers;

//#region IMPORTS
import login.MessageHandler;
//#endregion IMPORTS

/**
 * COP 4078 Exercise: 5
 * File Name: PasswordValidationException.java
 * 
 * This class is used when the user-entered password fails to validate overall.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @apiNote Added in version 1.4.
 */
public class PasswordValidationException extends Exception {
    public PasswordValidationException(String message) {
        super(message);
    }

    public PasswordValidationException(String key, String value) {
        super(MessageHandler.GetExceptionMessage(key, value));
    }
}