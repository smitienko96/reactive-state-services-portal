/*
 * This file is generated by jOOQ.
 */
package test.requests.persistence.db.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import test.requests.persistence.db.Applications;
import test.requests.persistence.db.Indexes;
import test.requests.persistence.db.Keys;
import test.requests.persistence.db.tables.records.ApplicationSectionsRecord;


/**
 * Секции заявлений
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ApplicationSections extends TableImpl<ApplicationSectionsRecord> {

    private static final long serialVersionUID = 378772079;

    /**
     * The reference instance of <code>applications.application_sections</code>
     */
    public static final ApplicationSections APPLICATION_SECTIONS = new ApplicationSections();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ApplicationSectionsRecord> getRecordType() {
        return ApplicationSectionsRecord.class;
    }

    /**
     * The column <code>applications.application_sections.id</code>. ID секции заявления
     */
    public final TableField<ApplicationSectionsRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('applications.application_sections_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "ID секции заявления");

    /**
     * The column <code>applications.application_sections.application_id</code>. ID заявления
     */
    public final TableField<ApplicationSectionsRecord, Long> APPLICATION_ID = createField("application_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "ID заявления");

    /**
     * The column <code>applications.application_sections.name</code>. Название секции
     */
    public final TableField<ApplicationSectionsRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(50), this, "Название секции");

    /**
     * The column <code>applications.application_sections.description</code>. Описание секции
     */
    public final TableField<ApplicationSectionsRecord, String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.VARCHAR(255), this, "Описание секции");

    /**
     * The column <code>applications.application_sections.document_code</code>. Код документа
     */
    public final TableField<ApplicationSectionsRecord, String> DOCUMENT_CODE = createField("document_code", org.jooq.impl.SQLDataType.VARCHAR(50), this, "Код документа");

    /**
     * The column <code>applications.application_sections.document_designation</code>. Назначение документа
     */
    public final TableField<ApplicationSectionsRecord, String> DOCUMENT_DESIGNATION = createField("document_designation", org.jooq.impl.SQLDataType.VARCHAR(100), this, "Назначение документа");

    /**
     * The column <code>applications.application_sections.document_id</code>. ID документа, связанного с секцией
     */
    public final TableField<ApplicationSectionsRecord, Long> DOCUMENT_ID = createField("document_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("'-1'::integer", org.jooq.impl.SQLDataType.BIGINT)), this, "ID документа, связанного с секцией");

    /**
     * Create a <code>applications.application_sections</code> table reference
     */
    public ApplicationSections() {
        this(DSL.name("application_sections"), null);
    }

    /**
     * Create an aliased <code>applications.application_sections</code> table reference
     */
    public ApplicationSections(String alias) {
        this(DSL.name(alias), APPLICATION_SECTIONS);
    }

    /**
     * Create an aliased <code>applications.application_sections</code> table reference
     */
    public ApplicationSections(Name alias) {
        this(alias, APPLICATION_SECTIONS);
    }

    private ApplicationSections(Name alias, Table<ApplicationSectionsRecord> aliased) {
        this(alias, aliased, null);
    }

    private ApplicationSections(Name alias, Table<ApplicationSectionsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("Секции заявлений"));
    }

    public <O extends Record> ApplicationSections(Table<O> child, ForeignKey<O, ApplicationSectionsRecord> key) {
        super(child, key, APPLICATION_SECTIONS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Applications.APPLICATIONS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.APPLICATION_SECTIONS_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<ApplicationSectionsRecord, Long> getIdentity() {
        return Keys.IDENTITY_APPLICATION_SECTIONS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<ApplicationSectionsRecord> getPrimaryKey() {
        return Keys.APPLICATION_SECTIONS_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ApplicationSectionsRecord>> getKeys() {
        return Arrays.<UniqueKey<ApplicationSectionsRecord>>asList(Keys.APPLICATION_SECTIONS_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ApplicationSectionsRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ApplicationSectionsRecord, ?>>asList(Keys.APPLICATION_SECTIONS__FK_APPLICATIONS_SECTIONS);
    }

    public test.requests.persistence.db.tables.Applications applications() {
        return new test.requests.persistence.db.tables.Applications(this, Keys.APPLICATION_SECTIONS__FK_APPLICATIONS_SECTIONS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationSections as(String alias) {
        return new ApplicationSections(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationSections as(Name alias) {
        return new ApplicationSections(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ApplicationSections rename(String name) {
        return new ApplicationSections(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ApplicationSections rename(Name name) {
        return new ApplicationSections(name, null);
    }
}
