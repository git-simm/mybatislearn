package simm.learning.web.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

import static simm.learning.web.config.Master2DataSourceConfig.getDataSource;

/**
 * 测试库一
 */
@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = Master1DataSourceConfig.PACKAGE, sqlSessionFactoryRef = "master1SqlSessionFactory")
public class Master1DataSourceConfig {
    // 精确到 master 目录，以便跟其他数据源隔离
    static final String PACKAGE = "simm.learning.biz.mapper.authority";
    static final String MAPPER_LOCATION = "classpath:mapper/authority/*.xml";

    @Value("${spring.write.one.url}")
    private String url;

    @Value("${spring.write.one.username}")
    private String user;

    @Value("${spring.write.one.password}")
    private String password;

    @Value("${spring.write.one.driverClassName}")
    private String driverClass;

    @Bean(name = "master1DataSource")
    @Primary
    public DataSource masterDataSource() {
        return getDataSource(driverClass, url, user, password);
    }

    @Bean(name = "master1TransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(masterDataSource());
    }

    @Bean(name = "master1SqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("master1DataSource") DataSource masterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(Master1DataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
