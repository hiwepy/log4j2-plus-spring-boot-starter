package org.apache.logging.log4j.spring.boot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.logging.log4j.spring.boot.ext.Log4jJdbcAppenderProperties;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 */
@ConfigurationProperties(Log4jJdbcProperties.PREFIX)
public class Log4jJdbcProperties implements BeanClassLoaderAware {

	public static final String PREFIX = "logging.log4j.jdbc";

	/**
	 * Whether to enable log4j jdbc.
	 */
	private boolean enabled = true;

	private ClassLoader classLoader;

	/**
	 * Name of the datasource. Default to "testdb" when using an embedded database.
	 */
	private String name;

	/**
	 * Whether to generate a random datasource name.
	 */
	private boolean generateUniqueName;

	/**
	 * Fully qualified name of the connection pool implementation to use. By default, it
	 * is auto-detected from the classpath.
	 */
	private Class<? extends DataSource> type;

	/**
	 * Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
	 */
	private String driverClassName;

	/**
	 * JDBC URL of the database.
	 */
	private String url;

	/**
	 * Login username of the database.
	 */
	private String username;

	/**
	 * Login password of the database.
	 */
	private String password;

	private String uniqueName;
	
	/**
	 * Whether use current Context. if false the LoggerContext appropriate for the caller of this method is
	 * returned. For example, in a web application if the caller is a class in
	 * WEB-INF/lib then one LoggerContext may be returned and if the caller is a
	 * class in the container's classpath then a different LoggerContext may be
	 * returned. If true then only a single LoggerContext will be returned.
	 **/
	private boolean currentContext = false;

	private List<Log4jJdbcAppenderProperties> appenders = new ArrayList<Log4jJdbcAppenderProperties>();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Log4jJdbcAppenderProperties> getAppenders() {
		return appenders;
	}

	public void setAppenders(List<Log4jJdbcAppenderProperties> appenders) {
		this.appenders = appenders;
	}

	public boolean isCreateDataSource() {
		return this.url != null || this.username != null;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	/**
	 * Initialize a {@link DataSourceBuilder} with the state of this instance.
	 * @return a {@link DataSourceBuilder} initialized with the customizations defined on
	 * this instance
	 */
	public DataSourceBuilder<?> initializeDataSourceBuilder() {
		return DataSourceBuilder.create(getClassLoader()).type(getType())
				.driverClassName(determineDriverClassName()).url(determineUrl())
				.username(determineUsername()).password(determinePassword());
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isGenerateUniqueName() {
		return this.generateUniqueName;
	}

	public void setGenerateUniqueName(boolean generateUniqueName) {
		this.generateUniqueName = generateUniqueName;
	}

	public Class<? extends DataSource> getType() {
		return this.type;
	}

	public void setType(Class<? extends DataSource> type) {
		this.type = type;
	}

	/**
	 * Determine the driver to use based on this configuration and the environment.
	 * @return the driver to use
	 */
	public String determineDriverClassName() {
		if (StringUtils.hasText(this.driverClassName)) {
			Assert.state(driverClassIsLoadable(), () -> "Cannot load driver class: " + this.driverClassName);
			return this.driverClassName;
		}
		String driverClassName = null;
		if (StringUtils.hasText(this.url)) {
			driverClassName = DatabaseDriver.fromJdbcUrl(this.url).getDriverClassName();
		}
		return driverClassName;
	}
	
	private boolean driverClassIsLoadable() {
		try {
			ClassUtils.forName(this.driverClassName, null);
			return true;
		}
		catch (UnsupportedClassVersionError ex) {
			// Driver library has been compiled with a later JDK, propagate error
			throw ex;
		}
		catch (Throwable ex) {
			return false;
		}
	}

	/**
	 * Return the configured url or {@code null} if none was configured.
	 * @return the configured url
	 * @see #determineUrl()
	 */
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Determine the url to use based on this configuration and the environment.
	 * @return the url to use
	 */
	public String determineUrl() {
		if (StringUtils.hasText(this.url)) {
			return this.url;
		}
		if (!StringUtils.hasText(url)) {
			throw new BeanCreationException("Failed to determine suitable jdbc url");
		}
		return url;
	}

	/**
	 * Determine the name to used based on this configuration.
	 * @return the database name to use or {@code null}
	 */
	public String determineDatabaseName() {
		if (this.generateUniqueName) {
			if (this.uniqueName == null) {
				this.uniqueName = UUID.randomUUID().toString();
			}
			return this.uniqueName;
		}
		if (StringUtils.hasLength(this.name)) {
			return this.name;
		}
		return null;
	}

	/**
	 * Return the configured username or {@code null} if none was configured.
	 * @return the configured username
	 * @see #determineUsername()
	 */
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Determine the username to use based on this configuration and the environment.
	 * @return the username to use
	 */
	public String determineUsername() {
		if (StringUtils.hasText(this.username)) {
			return this.username;
		}
		if (EmbeddedDatabaseConnection.isEmbedded(determineDriverClassName())) {
			return "sa";
		}
		return null;
	}
	

	/**
	 * Return the configured password or {@code null} if none was configured.
	 * @return the configured password
	 * @see #determinePassword()
	 */
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Determine the password to use based on this configuration and the environment.
	 * @return the password to use
	 */
	public String determinePassword() {
		if (StringUtils.hasText(this.password)) {
			return this.password;
		}
		if (EmbeddedDatabaseConnection.isEmbedded(determineDriverClassName())) {
			return "";
		}
		return null;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public boolean isCurrentContext() {
		return currentContext;
	}

	public void setCurrentContext(boolean currentContext) {
		this.currentContext = currentContext;
	}
	
}
