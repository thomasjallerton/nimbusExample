import models.Website;
import testing.LocalNimbusDeployment;

public class LocalWebserverTest {

    public static void main(String[] args) {
        LocalNimbusDeployment deployment = LocalNimbusDeployment.getNewInstance(Website.class);
        deployment.startWebserver("NimbusExampleWebsite", 8080);
    }

}
