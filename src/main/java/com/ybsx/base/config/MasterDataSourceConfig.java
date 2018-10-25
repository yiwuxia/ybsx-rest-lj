package com.ybsx.base.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.redare.devframework.common.spring.db.MySqlJdbcHelper;
import com.redare.devframework.common.spring.db.SpringJdbcHelper;
import com.ybsx.base.yml.YmlConfig;



/*
 * By type-safe, you can't make any mistakes with the String value of the name of the package.
 *  If you specify an incorrect class, it will fail at compile time.
 */
@Configuration
//@MapperScan(basePackageClasses = MasterMapperPackage.class, sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {
	
	//private static final String MAPPER_LOCATION = "classpath:mybatis/mapper/master/**/*.xml";
	
	// 配置文件
	@Autowired
	private YmlConfig yml;
	
	/**
	 * 创建数据源
	 * @return
	 */
	@Bean
	@Primary
	public DataSource masterDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUsername(yml.datasource.username);
		dataSource.setPassword(yml.datasource.password);
		dataSource.setUrl(yml.datasource.url);
		dataSource.setDriverClassName(yml.datasource.driver);

		dataSource.setInitialSize(10);
		dataSource.setMaxActive(50);
		dataSource.setMinIdle(5);
		dataSource.setMaxWait(1000 * 10);
		// 检测连接是否有效
		dataSource.setValidationQuery("select 1");
		dataSource.setTestOnBorrow(true);
		dataSource.setTestOnReturn(true);
		
		dataSource.setTestWhileIdle(true);
		dataSource.setTimeBetweenEvictionRunsMillis(10 * 60 * 1000);
		// 启用PSCache
		dataSource.setMaxOpenPreparedStatements(10);
		return dataSource;
	}

	@Bean
	@Primary
	public DataSourceTransactionManager masterTransactionManager() {
		return new DataSourceTransactionManager(this.masterDataSource());
	}
	
	
	@Bean
	@Primary 
	public SpringJdbcHelper jdbcHelper(){
		MySqlJdbcHelper jdbcHelper=new MySqlJdbcHelper();
		jdbcHelper.setDataSource(this.masterDataSource());
		return jdbcHelper;
	}
	 
/*
	@Bean
	@Primary
	public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource ds) throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(ds);
		Resource[] resources = new PathMatchingResourcePatternResolver().getResources(MasterDataSourceConfig.MAPPER_LOCATION);
		sessionFactory.setMapperLocations(resources);
		return sessionFactory.getObject();
	}*/
}
