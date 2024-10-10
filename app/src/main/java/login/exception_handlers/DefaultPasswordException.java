package login.exception_handlers;

/**
 * COP 4078 Exercise: 5
 * File Name: DefaultPasswordException.java
 * 
 * This class is used when the DefaultPassword generation fails.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @apiNote Added in version 1.4.
 */
public class DefaultPasswordException extends Exception {
    public DefaultPasswordException(String message) {
        super(message);
    }
}