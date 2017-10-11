/**
 * <p>Coyright (R) 2014 正方软件股份有限公司。<p>
 */
package org.apache.logging.log4j.spring.boot;

public class Log4j2ColumnConfig {

	private String column;
	private String pattern;
	private String literalValue;
	private boolean eventTimestamp = false;
	private boolean unicode = true;
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

	@Override
	public String toString() {
		return "{ name=" + this.column + ",  literal=" + this.literalValue
				+ ", timestamp=" + this.eventTimestamp + " }";
	}

}
