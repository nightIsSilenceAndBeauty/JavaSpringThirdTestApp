package ru.trints.MySecondTestAppSpringBoot.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import ru.trints.MySecondTestAppSpringBoot.model.Response;
import ru.trints.MySecondTestAppSpringBoot.util.DateTimeUtil;

@Service
@Qualifier("ModifySystemTimeResponseService")
@Slf4j
public class ModifySystemTimeResponseService implements ModifyResponseService {
    @Override
    public Response modify(Response response) {
        response.setSystemTime(DateTimeUtil.getCustomFormat().format(new Date()));
        log.info("systemTime обновлён: {}", response.getSystemTime());

        return response;
    }
}
