package com.viettelpost.core.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.util.Map;

public class Base2DAO {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected Map<String, Object> execute(String schema, String pkg, String procedureName, Map<String, Object> params) {
        SimpleJdbcCall caller = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName(schema)
                .withCatalogName(pkg)
                .withProcedureName(procedureName);
        return caller.execute(new MapSqlParameterSource().addValues(params));
    }
}
