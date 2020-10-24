package test.requests.service.impl;

import org.springframework.stereotype.Component;
import test.requests.domain.OperatorId;
import test.requests.service.IOperatorService;

@Component
public class SingletonOperatorService implements IOperatorService {

    @Override
    public OperatorId getCurrentOperator() {
        return new OperatorId("1234");
    }

    public SingletonOperatorService() {
    }


}
