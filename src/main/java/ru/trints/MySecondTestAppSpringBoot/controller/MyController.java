package ru.trints.MySecondTestAppSpringBoot.controller;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ru.trints.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.trints.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.trints.MySecondTestAppSpringBoot.model.Codes;
import ru.trints.MySecondTestAppSpringBoot.model.ErrorCodes;
import ru.trints.MySecondTestAppSpringBoot.model.ErrorMessages;
import ru.trints.MySecondTestAppSpringBoot.model.Request;
import ru.trints.MySecondTestAppSpringBoot.model.Response;
import ru.trints.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.trints.MySecondTestAppSpringBoot.service.ValidationService;
import ru.trints.MySecondTestAppSpringBoot.util.DateTimeUtil;

@RestController
@Slf4j
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifySystemTimeService;
    private final ModifyResponseService modifyOperationUidService;

    @Autowired
    public MyController(
            ValidationService validationService,
            @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifySystemTimeService,
            @Qualifier("ModifyOperationUidResponseService") ModifyResponseService modifyOperationUidService
    ) {
        this.validationService = validationService;
        this.modifySystemTimeService = modifySystemTimeService;
        this.modifyOperationUidService = modifyOperationUidService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(
            @Valid @RequestBody Request request,
            BindingResult bindingResult
    ) {
        log.info("request: {}", request);
        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("Создан начальный response: {}", response);

        try {
            validationService.isValid(bindingResult);
            validationService.checkSupportedUid(request.getUid());
        } catch (ValidationFailedException e) {
            log.error("Не удалось проверить запрос: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("Response после ошибки валидации: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("Запрос содержит неподдерживаемый uid: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.info("Response после ошибки проверки uid: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при обработке запроса", e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("Response после непредвиденной ошибки: {}", response);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Изменение operationUid в response");
        modifyOperationUidService.modify(response);
        log.info("Изменение systemTime в response");
        modifySystemTimeService.modify(response);
        log.info("response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
