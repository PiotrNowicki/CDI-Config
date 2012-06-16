package eu.awaketech.cdiconfig;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

/**
 * Produces {@link ConfigurationValue} annotated fields. It's responsible for supporting conversion between
 * types (mainly from String to any other type required by the user.)
 * 
 * <p>
 * These producers should not be interested where the fields are read from. It's the {@link PropertyResolver}
 * who is responsible for the configuration loading.
 * </p>
 * 
 * @see PropertyResolver
 * 
 * @author Piotr Nowicki
 * 
 */
public class ConfigurationValueProducer {

	@Inject
	PropertyResolver resolver;

	/**
	 * Main producer method - tries to find a property value using following keys:
	 * 
	 * <ol>
	 * <li><code>key</code> property of the {@link ConfigurationValue} annotation (if defined but no key is
	 * found - returns null),</li>
	 * <li>fully qualified field class name, e.g. <code>eu.awaketech.MyBean.myField</code> (if value is null,
	 * go along with the last resort),</li>
	 * <li>field name, e.g. <code>myField</code> for the example above (if the value is null, no can do -
	 * return null)</li>
	 * </ol>
	 * 
	 * @param ip
	 * @return value of the injected property or null if no value could be found.
	 */
	@Produces
	@ConfigurationValue
	public String getStringConfigValue(InjectionPoint ip) {
		
		// FIXME: String concatenation?
		String fqn = ip.getMember().getDeclaringClass().getName() + "." + ip.getMember().getName();

		// Trying with explicit key defined on the field
		String key = ip.getAnnotated().getAnnotation(ConfigurationValue.class).value();
		boolean isKeyDefined = !key.trim().isEmpty();

		boolean valueRequired = ip.getAnnotated().getAnnotation(ConfigurationValue.class).required();

		if (isKeyDefined) {
			return resolver.getValue(key);
		}

		// Falling back to fully-qualified field name resolving.
		key = fqn;
		String value = resolver.getValue(fqn);

		// No luck... so perhaps just the field name?
		if (value == null) {
			key = ip.getMember().getName();
			value = resolver.getValue(key);
		}

		// No can do - no value found but you've said it's required.
		if (value == null && valueRequired) {
			throw new IllegalStateException("No value defined for field: " + fqn
					+ " but field was marked as required.");
		}

		return value;
	}

	/**
	 * Produces {@link Double} type of property from {@link String} type.
	 * 
	 * <p>
	 * Will throw {@link NumberFormatException} if the value cannot be parsed into a {@link Double}
	 * </p>
	 * 
	 * @param ip
	 * @return value of the injected property or null if no value could be found.
	 * 
	 * @see ConfigurationValueProducer#getStringConfigValue(InjectionPoint)
	 */
	@Produces
	@ConfigurationValue
	public Double getDoubleConfigValue(InjectionPoint ip) {
		String value = getStringConfigValue(ip);

		return (value != null) ? Double.valueOf(value) : null;
	}

	/**
	 * Produces {@link Integer} type of property from {@link String} type.
	 * 
	 * <p>
	 * Will throw {@link NumberFormatException} if the value cannot be parsed into an {@link Integer}
	 * </p>
	 * 
	 * @param ip
	 * @return value of the injected property or null if no value could be found.
	 * 
	 * @see ConfigurationValueProducer#getStringConfigValue(InjectionPoint)
	 */
	@Produces
	@ConfigurationValue
	public Integer getIntegerConfigValue(InjectionPoint ip) {
		String value = getStringConfigValue(ip);

		return (value != null) ? Integer.valueOf(value) : null;
	}
}
