package test.requests.service.impl;

import org.jooq.Record;
import org.jooq.RecordMapper;
import test.requests.domain.Status;
import test.requests.domain.published.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static test.requests.persistence.db.Tables.*;

public class PublishedApplicationViewRecordMapper implements RecordMapper<Record, PublishedApplicationView> {

    private PublishedApplicationView applicationView;

    private Map<Long, PublishedApplicationSection> applicationSectionMap = new HashMap<>();

    @Override
    public PublishedApplicationView map(Record record) {

        if (applicationView == null) {
            applicationView = createPublishedApplicationView(record);
        }
        addOrUpdateSection(applicationView, record);

        return applicationView;
    }

    private PublishedApplicationView createPublishedApplicationView(Record record) {
        String number = record.get(APPLICATIONS_.NUMBER);
        Status status = Status.valueOf(record.get(APPLICATIONS_.STATUS));
        String applicationType = record.get(APPLICATIONS_.APPLICATION_TYPE);
        String applicantId = record.get(INDIVIDUALS.IDENTIFIER);
        String operatorId = record.get(APPLICATIONS_.CREATOR_ID);

        PublishedApplicationView result = new PublishedApplicationView();

        result.setNumber(number);
        result.setStatus(new PublishedApplicationStatus(status.name(), status.readable()));
        result.setApplicationType(applicationType);
        result.setApplicantId(applicantId);
        result.setCreatorId(operatorId);

        return result;
    }

    private void addOrUpdateSection(PublishedApplicationView applicationView, Record record) {
        long sectionId = record.get(APPLICATION_SECTIONS.ID);

        PublishedApplicationSection section = applicationSectionMap.get(sectionId);
        if (section == null) {

            section = createSection(record);

            applicationView.getSections().add(section);
            applicationSectionMap.put(sectionId, section);
        }

        section.getDocumentFields().add(createField(record));
    }

    private PublishedApplicationSection createSection(Record record) {
        PublishedApplicationSection section = new PublishedApplicationSection();

        PublishedApplicationMetadata metadata = new PublishedApplicationMetadata();
        metadata.setCode(record.get(APPLICATION_SECTIONS.NAME));
        metadata.setName(record.get(APPLICATION_SECTIONS.NAME));
        metadata.setDescription(record.get(APPLICATION_SECTIONS.DESCRIPTION));

        section.setSectionMetadata(metadata);
        section.setDocumentCode(record.get(APPLICATION_SECTIONS.DOCUMENT_CODE));

        return section;
    }

    private PublishedApplicationField createField(Record record) {
        PublishedApplicationField field = new PublishedApplicationField();

        PublishedFieldMetadata metadata = new PublishedFieldMetadata();
        metadata.setName(record.get(APPLICATION_DOCUMENT_FIELDS.NAME));
        metadata.setCode(record.get(APPLICATION_DOCUMENT_FIELDS.NAME));
        metadata.setDescription(record.get(APPLICATION_DOCUMENT_FIELDS.DESCRIPTION));

        DocumentFieldType fieldType = DocumentFieldType.valueOf(
                record.get(APPLICATION_DOCUMENT_FIELDS.FIELD_TYPE));
        metadata.setFieldType(fieldType);

        if (fieldType == DocumentFieldType.OPTIONS) {
            String[] options = record.get(
                    APPLICATION_DOCUMENT_FIELDS.FIELD_OPTIONS).split(";");
            metadata.setFieldOptions(Arrays.asList(options));
        }

        field.setFieldMetadata(metadata);
        field.setFieldValue(record.get(APPLICATION_DOCUMENT_FIELDS.VALUE));

        return field;
    }



}
