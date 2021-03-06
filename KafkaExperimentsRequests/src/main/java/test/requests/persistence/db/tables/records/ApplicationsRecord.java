/*
 * This file is generated by jOOQ.
 */
package test.requests.persistence.db.tables.records;


import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;

import test.requests.persistence.db.tables.Applications;


/**
 * Заявления
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ApplicationsRecord extends UpdatableRecordImpl<ApplicationsRecord> implements Record9<Long, String, String, Long, String, Timestamp, Timestamp, String, String> {

    private static final long serialVersionUID = 72584281;

    /**
     * Setter for <code>applications.applications.id</code>. ID заявления
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>applications.applications.id</code>. ID заявления
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>applications.applications.number</code>. Номер заявления
     */
    public void setNumber(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>applications.applications.number</code>. Номер заявления
     */
    public String getNumber() {
        return (String) get(1);
    }

    /**
     * Setter for <code>applications.applications.status</code>. Статус заявления
     */
    public void setStatus(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>applications.applications.status</code>. Статус заявления
     */
    public String getStatus() {
        return (String) get(2);
    }

    /**
     * Setter for <code>applications.applications.applicant_id</code>. ID заявителя
     */
    public void setApplicantId(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>applications.applications.applicant_id</code>. ID заявителя
     */
    public Long getApplicantId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>applications.applications.application_type</code>. Тип заявления
     */
    public void setApplicationType(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>applications.applications.application_type</code>. Тип заявления
     */
    public String getApplicationType() {
        return (String) get(4);
    }

    /**
     * Setter for <code>applications.applications.created</code>. Дата и время создания
     */
    public void setCreated(Timestamp value) {
        set(5, value);
    }

    /**
     * Getter for <code>applications.applications.created</code>. Дата и время создания
     */
    public Timestamp getCreated() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>applications.applications.changed</code>. Дата и время последней модификации
     */
    public void setChanged(Timestamp value) {
        set(6, value);
    }

    /**
     * Getter for <code>applications.applications.changed</code>. Дата и время последней модификации
     */
    public Timestamp getChanged() {
        return (Timestamp) get(6);
    }

    /**
     * Setter for <code>applications.applications.creator_id</code>. ID оператора, зарегистрировавшего заявление
     */
    public void setCreatorId(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>applications.applications.creator_id</code>. ID оператора, зарегистрировавшего заявление
     */
    public String getCreatorId() {
        return (String) get(7);
    }

    /**
     * Setter for <code>applications.applications.updater_id</code>. ID оператора, изменивашего заявление последним
     */
    public void setUpdaterId(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>applications.applications.updater_id</code>. ID оператора, изменивашего заявление последним
     */
    public String getUpdaterId() {
        return (String) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<Long, String, String, Long, String, Timestamp, Timestamp, String, String> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<Long, String, String, Long, String, Timestamp, Timestamp, String, String> valuesRow() {
        return (Row9) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Applications.APPLICATIONS_.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Applications.APPLICATIONS_.NUMBER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Applications.APPLICATIONS_.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return Applications.APPLICATIONS_.APPLICANT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Applications.APPLICATIONS_.APPLICATION_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return Applications.APPLICATIONS_.CREATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field7() {
        return Applications.APPLICATIONS_.CHANGED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return Applications.APPLICATIONS_.CREATOR_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return Applications.APPLICATIONS_.UPDATER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component4() {
        return getApplicantId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getApplicationType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component6() {
        return getCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component7() {
        return getChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getCreatorId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component9() {
        return getUpdaterId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getApplicantId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getApplicationType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value6() {
        return getCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value7() {
        return getChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getCreatorId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getUpdaterId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationsRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationsRecord value2(String value) {
        setNumber(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationsRecord value3(String value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationsRecord value4(Long value) {
        setApplicantId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationsRecord value5(String value) {
        setApplicationType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationsRecord value6(Timestamp value) {
        setCreated(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationsRecord value7(Timestamp value) {
        setChanged(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationsRecord value8(String value) {
        setCreatorId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationsRecord value9(String value) {
        setUpdaterId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationsRecord values(Long value1, String value2, String value3, Long value4, String value5, Timestamp value6, Timestamp value7, String value8, String value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ApplicationsRecord
     */
    public ApplicationsRecord() {
        super(Applications.APPLICATIONS_);
    }

    /**
     * Create a detached, initialised ApplicationsRecord
     */
    public ApplicationsRecord(Long id, String number, String status, Long applicantId, String applicationType, Timestamp created, Timestamp changed, String creatorId, String updaterId) {
        super(Applications.APPLICATIONS_);

        set(0, id);
        set(1, number);
        set(2, status);
        set(3, applicantId);
        set(4, applicationType);
        set(5, created);
        set(6, changed);
        set(7, creatorId);
        set(8, updaterId);
    }
}
