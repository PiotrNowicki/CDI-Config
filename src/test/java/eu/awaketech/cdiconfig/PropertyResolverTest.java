package eu.awaketech.cdiconfig;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PropertyResolverTest extends Arquillian {

    @Inject
    PropertyResolver cut;

    @Deployment
    public static Archive<?> createDeployment() throws IOException {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class).addClass(PropertyResolver.class);

        return archive;
    }

    @Test(dataProvider = "keyProvider")
    public void getValue(String key, String expected) {
        String actual = cut.getValue(key);

        assertThat(actual).isEqualTo(expected);
    }

    @DataProvider(name = "keyProvider")
    Object[][] dataProvider() {
        List<Object[]> data = new ArrayList<>();

        data.add(new Object[] { "myProp", "myVal" });
        data.add(new Object[] { "myProp2", "myVal2" });
        data.add(new Object[] { "myProp3", null });

        return data.toArray(new Object[0][0]);
    }
}
