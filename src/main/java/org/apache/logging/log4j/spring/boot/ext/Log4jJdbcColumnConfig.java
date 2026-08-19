package org.apache.logging.log4j.spring.boot.ext;

import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * Log4jJdbcColumnConfig class.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class Log4jJdbcColumnConfig {

	/**
	 * The name of the database column as it exists within the database table.
	 */
	private String column;
	/**
	 * The {@link PatternLayout} pattern to insert in this column. Mutually
	 * exclusive with {@code literal!=null} and {@code eventTimestamp=true}
	 */
	private String pattern;
	/**
	 * The literal value to insert into the column as-is without any quoting or
	 * escaping. Mutually exclusive with pattern!=null and eventTimestamp=true.
	 */
	private String literalValue;
	/**
	 * If "true", indicates that this column is a date-time column in which the
	 * event timestamp should be inserted. Mutually exclusive with pattern!=null and
	 * literal!=null.
	 */
	private boolean eventTimestamp = false;
	/**
	 * If {@code "true"}, indicates that the column is a Unicode String.
	 */
	private boolean unicode = true;
	/**
	 * If "true", indicates that the column is a character LOB (CLOB).
	 */
	private boolean clob = false;
	/** Gets the column. */

	public String getColumn() {
		return column;
	}
	/** Sets the column. */

	public void setColumn(String column) {
		this.column = column;
	}
	/** Gets the pattern. */

	public String getPattern() {
		return pattern;
	}
	/** Sets the pattern. */

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	/** Gets the literal value. */

	public String getLiteralValue() {
		return literalValue;
	}
	/** Sets the literal value. */

	public void setLiteralValue(String literalValue) {
		this.literalValue = literalValue;
	}
	/**
	 * <p>Is event timestamp.</p>
	 * @return the boolean
	 */

	public boolean isEventTimestamp() {
		return eventTimestamp;
	}
	/** Sets the event timestamp. */

	public void setEventTimestamp(boolean eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}
	/**
	 * <p>Is unicode.</p>
	 * @return the boolean
	 */

	public boolean isUnicode() {
		return unicode;
	}
	/** Sets the unicode. */

	public void setUnicode(boolean unicode) {
		this.unicode = unicode;
	}
	/**
	 * <p>Is clob.</p>
	 * @return the boolean
	 */

	public boolean isClob() {
		return clob;
	}
	/** Sets the clob. */

	public void setClob(boolean clob) {
		this.clob = clob;
	}
	/**
	 * <p>To column config.</p>
	 * @param configuration the configuration
	 * @return the column config
	 */

	public ColumnConfig toColumnConfig(final Configuration configuration) {
		return ColumnConfig.newBuilder().setConfiguration(configuration).setName(this.getColumn())
				.setPattern(this.getPattern()).setLiteral(this.getLiteralValue())
				.setEventTimestamp(this.isEventTimestamp()).setUnicode(this.isUnicode()).setClob(this.isClob()).build();
	}
	/**
	 * <p>To string.</p>
	 * @return the string
	 */

	@Override
	public String toString() {
		return "{ name=" + this.column + ",  literal=" + this.literalValue + ", timestamp=" + this.eventTimestamp
				+ " }";
	}

}
