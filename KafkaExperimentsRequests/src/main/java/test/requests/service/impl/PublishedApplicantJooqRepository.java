package test.requests.service.impl;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import test.requests.domain.IPublishedApplicantsRepository;
import test.requests.domain.published.PublishedAddress;
import test.requests.domain.published.PublishedIndividual;
import test.requests.domain.published.PublishedOrganization;
import test.requests.persistence.db.tables.Addresses;

import static test.requests.persistence.db.Tables.*;

@Component
public class PublishedApplicantJooqRepository implements IPublishedApplicantsRepository {

    @Autowired
    private DSLContext dslContext;

    @Override
    public Flux<PublishedIndividual> getIndividuals() {
        Addresses livingAddresses = ADDRESSES.as("living");
        Addresses registrationAdresses = ADDRESSES.as("registration");
        Flux<Record> records = Flux.fromStream(
                dslContext.select(DSL.asterisk()).from(INDIVIDUALS)
                        .innerJoin(livingAddresses).on(INDIVIDUALS.LIVING_ADDRESS_ID.eq(livingAddresses.ID))
                        .innerJoin(registrationAdresses).on(INDIVIDUALS.REGISTRATION_ADDRESS_ID.eq(registrationAdresses.ID))
                        .fetchStream());

        return records.map(r -> {
            PublishedIndividual individual = new PublishedIndividual();

            individual.setSnils(r.get(INDIVIDUALS.IDENTIFIER));
            individual.setFirstName(r.get(INDIVIDUALS.FIRST_NAME));
            individual.setLastName(r.get(INDIVIDUALS.LAST_NAME));
            individual.setPatronymic(r.get(INDIVIDUALS.PATRONYMIC));

            individual.setLivingAddress(fetchAddress(r, "living"));
            individual.setRegistrationAddress(fetchAddress(r, "registration"));

            return individual;
        });
    }

    private PublishedAddress fetchAddress(Record record, String alias) {
        Addresses addressesTable = ADDRESSES.as(alias);

        PublishedAddress address = new PublishedAddress();
        address.setRegion(record.get(addressesTable.REGION));
        address.setCity(record.get(addressesTable.CITY));
        address.setStreet(record.get(addressesTable.STREET));

        return address;
    }

    @Override
    public Flux<PublishedOrganization> getOrganizations() {
        Addresses actualAdresses = ADDRESSES.as("actual");
        Addresses legalAdresses = ADDRESSES.as("legal");
        Flux<Record> records = Flux.fromStream(dslContext.select(DSL.asterisk()).from(ORGANIZATIONS)
                .innerJoin(actualAdresses).on(actualAdresses.ID.eq(ORGANIZATIONS.ACTUAL_ADDRESS_ID))
                .innerJoin(legalAdresses).on(legalAdresses.ID.eq(ORGANIZATIONS.LEGAL_ADDRESS_ID))
                .fetchStream());
        return records.map(r -> {
            PublishedOrganization organization = new PublishedOrganization();

            organization.setInn(r.get(ORGANIZATIONS.INN));
            organization.setShortName(r.get(ORGANIZATIONS.ORG_NAME_SHORT));
            organization.setLongName(r.get(ORGANIZATIONS.ORG_NAME_FULL));

            organization.setActualAddress(fetchAddress(r, "actual"));
            organization.setLegalAddress(fetchAddress(r, "legal"));

            return organization;
        });
    }
}
