package login.exception_handlers;

//#region IMPORTS
import login.MessageHandler;
//#endregion IMPORTS

/**
 * COP 4078 Exercise: 5
 * File Name: PasswordPolicyException.java
 * 
 * This class is used when any of the password policies fail to validate.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @apiNote Added in version 1.4.
 */
public class PasswordPolicyException extends Exception {
    public PasswordPolicyException(String key, String value) {
        super(MessageHandler.GetExceptionMessage(key, value));
    }
}