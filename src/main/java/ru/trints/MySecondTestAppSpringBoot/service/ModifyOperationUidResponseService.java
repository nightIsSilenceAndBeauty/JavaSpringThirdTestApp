package ru.trints.MySecondTestAppSpringBoot.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import ru.trints.MySecondTestAppSpringBoot.model.Response;

@Service
@Qualifier("ModifyOperationUidResponseService")
@Slf4j
public class ModifyOperationUidResponseService implements ModifyResponseService {
    @Override
    public Response modify(Response response) {
        String previousOperationUid = response.getOperationUid();
        response.setOperationUid(UUID.randomUUID().toString());
        log.info("operationUid изменён: {} -> {}", previousOperationUid, response.getOperationUid());

        return response;
    }
}
