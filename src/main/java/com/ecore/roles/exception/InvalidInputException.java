package com.ecore.roles.exception;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }

    public static class Membership {

        public static class ProvidedUserNotBelongsToTeam extends InvalidInputException {

            public ProvidedUserNotBelongsToTeam() {
                super("Invalid 'Membership' object. " +
                        "The provided user doesn't belong to the provided team.");
            }
        }

    }

}
