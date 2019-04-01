import testing.LocalNimbusDeployment;

public class LocalWebserverTest {

    public static void main(String[] args) {
        LocalNimbusDeployment deployment = LocalNimbusDeployment.getNewInstance("com.allerton.nimbusExample");
        deployment.startAllWebservers(8080);
        //deployment.startWebserver("NimbusExampleWebsite", 8080);
    }
}
