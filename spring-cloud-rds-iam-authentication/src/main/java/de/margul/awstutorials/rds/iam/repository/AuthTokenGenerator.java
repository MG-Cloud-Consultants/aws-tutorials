package de.margul.awstutorials.rds.iam.repository;

import java.security.InvalidParameterException;

import org.springframework.util.StringUtils;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.rds.auth.GetIamAuthTokenRequest;
import com.amazonaws.services.rds.auth.RdsIamAuthTokenGenerator;

public class AuthTokenGenerator {

    private String rdsDatabaseUser;
    private String rdsRegionName;
    private String rdsInstanceHostName;
    private int rdsInstancePort;

    private RdsIamAuthTokenGenerator tokenGenerator;

    public AuthTokenGenerator(String rdsDatabaseUser, String rdsRegionName, String rdsInstanceHostName,
            int rdsInstancePort) {
        this.rdsDatabaseUser = rdsDatabaseUser;
        this.rdsRegionName = rdsRegionName;
        this.rdsInstanceHostName = rdsInstanceHostName;
        this.rdsInstancePort = rdsInstancePort;

        validateMembers();
        initTokenGenerator();
    }

    private void validateMembers() {
        StringBuilder messageBuilder = new StringBuilder();

        if (StringUtils.isEmpty(rdsDatabaseUser)) {
            messageBuilder.append("Database user can not be blank.\n");
        }

        if (StringUtils.isEmpty(rdsRegionName)) {
            messageBuilder.append("Database region can not be blank.\n");
        }

        if (StringUtils.isEmpty(rdsInstanceHostName)) {
            messageBuilder.append("Database instance host name can not be blank.\n");
        }

        if (rdsInstancePort < 0) {
            messageBuilder.append("Database instance port can not be blank.\n");
        }

        String message = messageBuilder.toString();

        if (!StringUtils.isEmpty(message)) {
            throw new InvalidParameterException(message);
        }
    }

    private void initTokenGenerator() {
        this.tokenGenerator = RdsIamAuthTokenGenerator.builder().credentials(new DefaultAWSCredentialsProviderChain())
                .region(rdsRegionName).build();
    }

    public String generateAuthToken() {
        return tokenGenerator.getAuthToken(GetIamAuthTokenRequest.builder().hostname(rdsInstanceHostName)
                .port(rdsInstancePort).userName(rdsDatabaseUser).build());
    }

    public void setTokenGenerator(RdsIamAuthTokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }
}