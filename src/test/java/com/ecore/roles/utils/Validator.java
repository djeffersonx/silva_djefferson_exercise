package com.ecore.roles.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class Validator {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static <T> Set<ConstraintViolation<T>> validate(T object) {
        return validatorFactory.getValidator().validate(object);
    }

}
