package eu.awaketech.cdiconfig;

import static java.lang.annotation.RetentionPolicy.*;
import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Defines method / variable that should be injected with value read from some arbitrary resource (e.g. from
 * <code>properties</code> file.)
 * 
 * @author Piotr Nowicki
 * 
 */
@Qualifier
@Retention(RUNTIME)
@Target({ TYPE, METHOD, FIELD, PARAMETER })
public @interface ConfigurationValue {
	/**
	 * Key that will be searched when injecting the value.
	 */
	@Nonbinding
	String value() default "";

	/**
	 * Defines if value for the given key must be defined.
	 */
	@Nonbinding
	boolean required() default true;
}
