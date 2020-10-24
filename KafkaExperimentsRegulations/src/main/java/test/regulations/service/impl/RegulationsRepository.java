package test.regulations.service.impl;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.domain.AggregateNames;
import test.common.domain.IAggregateMementoBuilder;
import test.common.service.MementoBasedAggregateRepository;
import test.regulations.domain.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author s.smitienko
 */
@Component
public class RegulationsRepository extends MementoBasedAggregateRepository<Regulation, RegulationIdentifier> implements IRegulationRepository {

    @Override
    public Mono<Regulation> getByServiceId(ServiceId serviceId) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put(RegulationMementoBuilder.SERVICEID_FIELD, serviceId.id());
        filterMap.put(RegulationMementoBuilder.STATUS_FIELD, Regulation.Status.ACTIVE.name());

        return checkAndReturnUnique(serviceId.toString(), getByFilter(filterMap), false);
    }

    @Override
    protected Map<String, Object> buildUniquenessFilterMap(RegulationIdentifier identifier, IAggregateMementoBuilder<Regulation> builder) {
        Map<String, Object> result = super.buildUniquenessFilterMap(identifier, builder);
        result.put(RegulationMementoBuilder.DATE_FIELD, String.valueOf(identifier.date().getTime()));
        return result;
    }





    @Override
    protected String aggregateName() {
        return AggregateNames.REGULATIONS;
    }
}
