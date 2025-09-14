package external.bootprofile.datasource.config;

import external.bootprofile.datasource.MyDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.List;

@Slf4j
//@Configuration
public class MyDataSourceValueConfig {
    @Value("${MyDataSourcePropertiesV3}")
    private String url;
    @Value("${MyDataSourcePropertiesV3}")
    private String username;
    @Value("${MyDataSourcePropertiesV3}")
    private String password;
    @Value("${my.datasource.etc.max-connection}")
    private int maxConnection;
    @Value("${my.datasource.etc.timeout}")
    private Duration timeout;
    @Value("${my.datasource.etc.options}")
    private List<String> options;

    @Bean
    public MyDataSource myDataSource1() {
        return new MyDataSource(url, username, password, maxConnection, timeout, options);
    }

    @Bean
    public MyDataSource myDataSource2(
            @Value("${MyDataSourcePropertiesV3}") String url,
            @Value("${MyDataSourcePropertiesV3}") String username,
            @Value("${MyDataSourcePropertiesV3}") String password,
            @Value("${my.datasource.etc.max-connection}") int maxConnection,
            @Value("${my.datasource.etc.timeout}") Duration timeout,
            @Value("${my.datasource.etc.options}") List<String> options) {
        return new MyDataSource(url, username, password, maxConnection, timeout, options);
    }
}