package com.dao;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToupiaohuodongDaoMapperTest {

    @Test
    void selectByIdForUpdateIsBound() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        try (InputStream in = getClass().getResourceAsStream("/mapper/ToupiaohuodongDao.xml")) {
            assertNotNull(in);
            new XMLMapperBuilder(in, configuration, "mapper/ToupiaohuodongDao.xml",
                    configuration.getSqlFragments()).parse();
        }
        assertTrue(configuration.hasStatement("com.dao.ToupiaohuodongDao.selectByIdForUpdate"));
    }
}
