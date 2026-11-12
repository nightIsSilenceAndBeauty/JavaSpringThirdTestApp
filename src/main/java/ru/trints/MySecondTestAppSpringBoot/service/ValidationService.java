package ru.trints.MySecondTestAppSpringBoot.service;

import org.springframework.validation.BindingResult;
import ru.trints.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.trints.MySecondTestAppSpringBoot.exception.ValidationFailedException;

public interface ValidationService {

    void isValid(BindingResult bindingResult) throws ValidationFailedException;

    void checkSupportedUid(String uid) throws UnsupportedCodeException;
}
