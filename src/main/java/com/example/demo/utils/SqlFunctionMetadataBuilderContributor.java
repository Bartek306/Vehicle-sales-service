package com.example.demo.utils;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.BooleanType;


public class SqlFunctionMetadataBuilderContributor implements MetadataBuilderContributor {
    @Override
    public void contribute(MetadataBuilder metadataBuilder) {
        metadataBuilder.applySqlFunction("search",
                new SQLFunctionTemplate(BooleanType.INSTANCE,
                        "to_tsvector(title || ' ' || description) @@ to_tsquery(?1)"));
    }
}
