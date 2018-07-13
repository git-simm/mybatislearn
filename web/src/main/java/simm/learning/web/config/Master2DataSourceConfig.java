package simm.learning.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 测试数据库二
 */
@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = Master2DataSourceConfig.PACKAGE, sqlSessionFactoryRef = "master2SqlSessionFactory")
public class Master2DataSourceConfig {
    // 精确到 master 目录，以便跟其他数据源隔离
    static final String PACKAGE = "simm.learning.biz.mapper.master2";
    static final String MAPPER_LOCATION = "classpath:mapper/master2/*.xml";

    @Value("${spring.read.two.url}")
    private String url;

    @Value("${spring.read.two.username}")
    private String user;

    @Value("${spring.read.two.password}")
    private String password;

    @Value("${spring.read.two.driverClassName}")
    private String driverClass;

    @Bean(name = "master2DataSource")
    public DataSource master2DataSource() {
        return getDataSource(driverClass, url, user, password);
    }

    static DataSource getDataSource(String driverClass, String url, String user, String password) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "master2TransactionManager")
    public DataSourceTransactionManager master2TransactionManager() {
        return new DataSourceTransactionManager(master2DataSource());
    }

    @Bean(name = "master2SqlSessionFactory")
    public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("master2DataSource") DataSource master2DataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(master2DataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(Master2DataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}