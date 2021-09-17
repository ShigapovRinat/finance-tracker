package sirius.tinkoff.financial.tracker.container;

import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgresContainer extends PostgreSQLContainer<CustomPostgresContainer> {
    private static final String IMAGE_VERSION = "postgres:11.1";
    private static final int EXPOSED_PORT = 5555;
    private static final String DB_NAME = "test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    private static CustomPostgresContainer customPostgresContainer;


    public CustomPostgresContainer() {
        super(IMAGE_VERSION);
    }

    public static CustomPostgresContainer getCustomPostgresContainerInstance() {
        if (customPostgresContainer == null) {
            return customPostgresContainer = extracted().withExposedPorts(EXPOSED_PORT)
                    .withDatabaseName(DB_NAME)
                    .withUsername(DB_USER)
                    .withPassword(DB_PASSWORD);
        }

        return customPostgresContainer;
    }

    private static CustomPostgresContainer extracted() {
        return new CustomPostgresContainer();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}