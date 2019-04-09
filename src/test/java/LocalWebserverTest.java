import com.nimbusframework.nimbuscore.testing.LocalNimbusDeployment;

public class LocalWebserverTest {

    public static void main(String[] args) {
        LocalNimbusDeployment deployment = LocalNimbusDeployment.getNewInstance("com.allerton.nimbusExample");
    }

}
