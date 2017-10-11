package org.apache.logging.log4j.spring.boot;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Markers {

	public static final Marker DB = MarkerFactory.getMarker("dblog"); // dblog就是上面MarkerFilter里的标记

}
