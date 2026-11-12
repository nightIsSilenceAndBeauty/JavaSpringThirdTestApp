package ru.trints.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import ru.trints.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.trints.MySecondTestAppSpringBoot.exception.ValidationFailedException;

@Service
@Slf4j
public class RequestValidationService implements ValidationService {

    @Override
    public void isValid(BindingResult bindingResult) throws ValidationFailedException {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            log.error("Ошибки валидации запроса: {}", errors);

            throw new ValidationFailedException(errors);
        }
    }

    @Override
    public void checkSupportedUid(String uid) throws UnsupportedCodeException {
        if ("123".equals(uid)) {
            log.error("Обнаружен неподдерживаемый uid: {}", uid);

            throw new UnsupportedCodeException("uid 123 не поддерживается нашей системой");
        }
    }
}
