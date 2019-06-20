package nl.utwente;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/*
This is needed for Jersey to work with our registered settings. It's magic
 */
public class Application extends ResourceConfig {
    public Application() {
        register(RolesAllowedDynamicFeature.class);
    }
}