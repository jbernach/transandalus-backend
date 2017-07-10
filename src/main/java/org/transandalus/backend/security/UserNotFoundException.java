package org.transandalus.backend.security;

/**
 * Created by JoseMaria on 10/07/2017.
 */
public class UserNotFoundException extends IllegalStateException {

    public UserNotFoundException(String s) {
        super(s);
    }
}
