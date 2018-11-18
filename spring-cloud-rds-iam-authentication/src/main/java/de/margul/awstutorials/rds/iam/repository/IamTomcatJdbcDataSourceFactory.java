package de.margul.awstutorials.rds.iam.repository;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.aws.jdbc.datasource.DataSourceFactory;
import org.springframework.cloud.aws.jdbc.datasource.DataSourceInformation;
import org.springframework.cloud.aws.jdbc.datasource.support.DatabasePlatformSupport;
import org.springframework.cloud.aws.jdbc.datasource.support.StaticDatabasePlatformSupport;

public class IamTomcatJdbcDataSourceFactory extends PoolProperties implements DataSourceFactory {

    private String region;

    private DatabasePlatformSupport databasePlatformSupport = new StaticDatabasePlatformSupport();

    @Override
    public void setUsername(String username) {
        throw new UnsupportedOperationException("Username will be set at runtime!");
    }

    @Override
    public void setPassword(String password) {
        throw new UnsupportedOperationException("Password will be set at runtime!");
    }

    @Override
    public void setDriverClassName(String driverClassName) {
        throw new UnsupportedOperationException("Will be set at runtime!");
    }

    @Override
    public void setUrl(String url) {
        throw new UnsupportedOperationException("Will be set at runtime!");
    }
    
    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public org.apache.tomcat.jdbc.pool.DataSource createDataSource(DataSourceInformation dataSourceInformation) {

        PoolConfiguration configurationToUse = new PoolProperties();
        BeanUtils.copyProperties(this, configurationToUse);

        configurationToUse.setDriverClassName(
                this.databasePlatformSupport.getDriverClassNameForDatabase(dataSourceInformation.getDatabaseType()));
        configurationToUse.setUrl(this.databasePlatformSupport.getDatabaseUrlForDatabase(
                dataSourceInformation.getDatabaseType(), dataSourceInformation.getHostName(),
                dataSourceInformation.getPort(), dataSourceInformation.getDatabaseName()));
        configurationToUse.setUsername(dataSourceInformation.getUserName());

        String authToken = rdsAuthTokenGenerator(dataSourceInformation.getUserName(),
                dataSourceInformation.getHostName(), region).generateAuthToken();
        configurationToUse.setPassword(authToken);

        return new org.apache.tomcat.jdbc.pool.DataSource(configurationToUse);
    }

    @Override
    public void closeDataSource(DataSource dataSource) {
        if (dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource) {
            ((org.apache.tomcat.jdbc.pool.DataSource) dataSource).close();
        }
    }

    private AuthTokenGenerator rdsAuthTokenGenerator(String rdsDatabaseUser, String rdsInstanceHostName,
            String region) {
        return new AuthTokenGenerator(rdsDatabaseUser, region, rdsInstanceHostName, 3306);
    }
}