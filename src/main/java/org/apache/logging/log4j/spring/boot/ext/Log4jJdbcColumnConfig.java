package org.apache.logging.log4j.spring.boot.ext;

import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;

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

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getLiteralValue() {
		return literalValue;
	}

	public void setLiteralValue(String literalValue) {
		this.literalValue = literalValue;
	}

	public boolean isEventTimestamp() {
		return eventTimestamp;
	}

	public void setEventTimestamp(boolean eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	public boolean isUnicode() {
		return unicode;
	}

	public void setUnicode(boolean unicode) {
		this.unicode = unicode;
	}

	public boolean isClob() {
		return clob;
	}

	public void setClob(boolean clob) {
		this.clob = clob;
	}

	public ColumnConfig toColumnConfig(final Configuration configuration) {
		return ColumnConfig.newBuilder().setConfiguration(configuration).setName(this.getColumn())
				.setPattern(this.getPattern()).setLiteral(this.getLiteralValue())
				.setEventTimestamp(this.isEventTimestamp()).setUnicode(this.isUnicode()).setClob(this.isClob()).build();
	}

	@Override
	public String toString() {
		return "{ name=" + this.column + ",  literal=" + this.literalValue + ", timestamp=" + this.eventTimestamp
				+ " }";
	}

}
