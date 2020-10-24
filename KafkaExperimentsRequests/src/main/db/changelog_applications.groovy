databaseChangeLog() {
    changeSet(id: 'create-schema', author: 's.smitienko') {
        sql(stripComments: true, splitStatements: false, endDelimiter: ';') {
            "create schema if not exists applications"
        }
    }
    changeSet(id: 'applications-core-no-references', author: 's.smitienko') {
        createTable(schemaName: 'applications', tableName: 'applications', remarks: 'Заявления') {
            column(name: 'id', type: 'BIGINT', autoIncrement: true, remarks: 'ID заявления') {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: 'number', type: 'VARCHAR(10)', remarks: 'Номер заявления') {
                constraints(nullable: false, unique: true)
            }
            column(name: 'status', type: 'VARCHAR(10)', remarks: 'Статус заявления') {
                constraints(nullable: false)
            }
            column(name: 'applicant_id', type: 'BIGINT', remarks: 'ID заявителя') {
                constraints(nullable: false)
            }
            column(name: 'application_type', type: 'VARCHAR(10)', remarks: 'Тип заявления')
            column(name: 'created', type: 'DATETIME', remarks: 'Дата и время создания') {
                constraints(nullable: false)
            }
            column(name: 'changed', type: 'DATETIME', remarks: 'Дата и время последней модификации') {
                constraints(nullable: false)
            }
            column(name: 'creator_id', type: 'VARCHAR(25)', remarks: 'ID оператора, зарегистрировавшего заявление') {
                constraints(nullable: false)
            }
            column(name: 'updater_id', type: 'VARCHAR(25)', remarks: 'ID оператора, изменивашего заявление последним') {
                constraints(nullable: false)
            }
        }
        createTable(schemaName: 'applications', tableName: 'application_sections', remarks: 'Секции заявлений') {
            column(name: 'id', type: 'BIGINT', autoIncrement: true, remarks: 'ID секции заявления') {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: 'application_id', type: 'BIGINT', remarks: 'ID заявления') {
                constraints(nullable: false)
            }
            column(name: 'name', type: 'VARCHAR(50)', remarks: 'Название секции') {
                constraints(nullable: false)
            }
            column(name: 'description', type: 'VARCHAR(255)', remarks: 'Описание секции')
            column(name: 'document_code', type: 'VARCHAR(50)', remarks: 'Код документа') {
                constraints(nullable: false)
            }
            column(name: 'document_designation', type: 'VARCHAR(100)', remarks: 'Назначение документа')
        }
        createTable(schemaName: 'applications', tableName: 'application_document_fields', remarks: 'Поля документов заявления') {
            column(name: 'id', type: 'BIGINT', autoIncrement: true, remarks: 'ID поля документов') {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: 'section_id', type: 'BIGINT', remarks: 'ID секции заявления') {
                constraints(nullable: false)
            }
            column(name: 'name', type: 'VARCHAR(50)', remarks: 'Название поля документа') {
                constraints(nullable: false)
            }
            column(name: 'description', type: 'VARCHAR(255)', remarks: 'Описание поля документа')
            column(name: 'field_type', type: 'VARCHAR(50)', remarks: 'Тип поля документа') {
                constraints(nullable: false)
            }
            column(name: 'field_options', type: 'VARCHAR(255)', remarks: 'Возможные значения полей документа') {
                constraints(nullable: false)
            }
            column(name: 'value', type: 'VARCHAR(255)', remarks: 'Значение поля документа')
        }
        createTable(schemaName: 'applications', tableName: 'individuals', remarks: 'Заявители - физические лица') {
            column(name: 'id', type: 'BIGINT', autoIncrement: true, remarks: 'ID заявителя') {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: 'identifier', type: 'VARCHAR(20)', remarks: 'Идентификатор заявителя') {
                constraints(nullable: false)
            }
            column(name: 'last_name', type: 'VARCHAR(100)', remarks: 'Фамилия заявителя')
            column(name: 'first_name', type: 'VARCHAR(100)', remarks: 'Имя заявителя')
            column(name: 'patronymic', type: 'VARCHAR(100)', remarks: 'Отчество заявителя')
            column(name: 'passport_no', type: 'VARCHAR(12)', remarks: 'Номер паспорта')
            column(name: 'reg_date', type: 'DATE', remarks: 'Дата регистрации заявителя')
            column(name: 'living_address_id', type: 'BIGINT', remarks: 'ID фактического адреса') {
                constraints(nullable: false)
            }
            column(name: 'registration_address_id', type: 'BIGINT', remarks: 'ID адреса регистрации') {
                constraints(nullable: false)
            }
        }
        createTable(schemaName: 'applications', tableName: 'organizations', remarks: 'Заявители - юридические лица') {
            column(name: 'id', type: 'BIGINT', autoIncrement: true, remarks: 'ID заявителя') {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: 'identifier', type: 'VARCHAR(20)', remarks: 'Идентификатор заявителя') {
                constraints(nullable: false)
            }
            column(name: 'org_name_short', type: 'VARCHAR(150)', remarks: 'Краткое наименование заявителя')
            column(name: 'org_name_full', type: 'VARCHAR(255)', remarks: 'Полное наименование заявителя')
            column(name: 'inn', type: 'VARCHAR(20)', remarks: 'ИНН заявителя')
            column(name: 'ogrn', type: 'VARCHAR(20)', remarks: 'ОГРН заявителя')
            column(name: 'reg_date', type: 'DATE', remarks: 'Дата регистрации заявителя')
            column(name: 'actual_address_id', type: 'BIGINT', remarks: 'ID фактического адреса') {
                constraints(nullable: false)
            }
            column(name: 'legal_address_id', type: 'BIGINT', remarks: 'ID юридического адреса') {
                constraints(nullable: false)
            }
        }
        createTable(schemaName: 'applications', tableName: 'addresses', remarks: 'Адреса заявителей') {
            column(name: 'id', type: 'BIGINT', autoIncrement: true, remarks: 'ID адреса заявителя') {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: 'region', type: 'VARCHAR(25)', remarks: 'Регион')
            column(name: 'city', type: 'VARCHAR(25)', remarks: 'Город')
            column(name: 'street', type: 'VARCHAR(25)', remarks: 'Улица')
            column(name: 'building', type: 'VARCHAR(5)', remarks: 'Дом')
            column(name: 'apartment', type: 'VARCHAR(5)', remarks: 'Квартира/Помещение')
        }
    }
    changeSet(id: 'applications-core-foreignkeys', author: 's.smitienko') {

        addForeignKeyConstraint(constraintName: 'fk_applications_sections', baseTableSchemaName: 'applications',
                baseTableName: 'application_sections', baseColumnNames: 'application_id',
                referencedTableSchemaName: 'applications', referencedTableName: 'applications',
                referencedColumnNames: 'id', onDelete: 'CASCADE', onUpdate: 'RESTRICT')

        addForeignKeyConstraint(constraintName: 'fk_sections_document_fields', baseTableSchemaName: 'applications',
                baseTableName: 'application_document_fields', baseColumnNames: 'section_id',
                referencedTableSchemaName: 'applications', referencedTableName: 'application_sections',
                referencedColumnNames: 'id', onDelete: 'CASCADE', onUpdate: 'RESTRICT')

        addForeignKeyConstraint(constraintName: 'fk_applications_individuals', baseTableSchemaName: 'applications',
                baseTableName: 'applications', baseColumnNames: 'applicant_id',
                referencedTableSchemaName: 'applications', referencedTableName: 'individuals',
                referencedColumnNames: 'id', onDelete: 'CASCADE', onUpdate: 'RESTRICT')

        addForeignKeyConstraint(constraintName: 'fk_applications_organizations', baseTableSchemaName: 'applications',
                baseTableName: 'applications', baseColumnNames: 'applicant_id',
                referencedTableSchemaName: 'applications', referencedTableName: 'organizations',
                referencedColumnNames: 'id', onDelete: 'CASCADE', onUpdate: 'RESTRICT')

        addForeignKeyConstraint(constraintName: 'fk_applicant_address_living', baseTableSchemaName: 'applications',
                baseTableName: 'individuals', baseColumnNames: 'living_address_id', referencedTableSchemaName: 'applications',
                referencedTableName: 'addresses', referencedColumnNames: 'id', onDelete: 'CASCADE', onUpdate: 'RESTRICT')

        addForeignKeyConstraint(constraintName: 'fk_applicant_address_registration', baseTableSchemaName: 'applications',
                baseTableName: 'individuals', baseColumnNames: 'registration_address_id', referencedTableSchemaName: 'applications',
                referencedTableName: 'addresses', referencedColumnNames: 'id', onDelete: 'CASCADE', onUpdate: 'RESTRICT')

    }
    changeSet(id: 'applications-core-add-documentid', author: 's.smitienko') {
        addColumn(tableName: 'application_sections', schemaName: 'applications') {
            column(name: 'document_id', type: 'BIGINT', defaultValue: '-1', remarks: 'ID документа, связанного с секцией') {
                constraints(nullable: false)
            }
        }
    }
    changeSet(id: 'applications-core-loosen-constraints-1', author: 's.smitienko') {
        dropNotNullConstraint(tableName: 'application_sections', schemaName: 'applications', columnName: 'name')
        dropNotNullConstraint(tableName: 'application_sections', schemaName: 'applications', columnName: 'document_code')
        dropNotNullConstraint(tableName: 'application_document_fields', schemaName: 'applications', columnName: 'field_options')
    }
    changeSet(id: 'individuals-add-passport-data', author: 's.smitienko') {
        addColumn(tableName: 'individuals', schemaName: 'applications') {
            column(name: 'passport_series', type: 'VARCHAR(12)', remarks: 'Серия паспорта')
            column(name: 'passport_issue_date', type: 'DATE', remarks: 'Дата выдачи паспорта')
            column(name: 'passport_issue_authority', type: 'VARCHAR(50)', remarks: 'Наименование органа, выдавшего паспорт')
            column(name: 'active', type: 'boolean', defaultValue: 'false', remarks: 'Статус заявителя в системе (активен/неактивен)')
        }
    }
    changeSet(id: 'add-primary-indexes', author: 's.smitienko') {
        createIndex(indexName: 'idx_individual_identifier', schemaName: 'applications', tableName: 'individuals', unique: true) {
            column(name: 'identifier', type: 'VARCHAR(20)')
        }
        createIndex(indexName: 'idx_organization_identifier', schemaName: 'applications', tableName: 'organizations', unique: true) {
            column(name: 'identifier', type: 'VARCHAR(20)')
        }
        createIndex(indexName: 'idx_application_number', schemaName: 'applications', tableName: 'applications', unique: true) {
            column(name: 'number', type: 'VARCHAR(10)')
        }
    }
}