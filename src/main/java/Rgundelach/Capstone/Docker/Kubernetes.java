/**
 * @author rgundelach
 * @createdOn 2/4/2025 at 11:13 AM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.Docker;
 */
package Rgundelach.Capstone.Docker;

import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Watch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.IOException;

@RestController
@RequestMapping("/API")
public class Kubernetes {
public void main() {
    try {
       // System.setProperty("KUBECONFIG", "C:\\Users\\rgundelach\\.kube\\config");
        ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader("C:\\Users\\rgundelach\\.kube\\config"))).build(); //ChatGPT
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1PodList list = api.listPodForAllNamespaces().execute();
        for (V1Pod item : list.getItems()) {
            System.out.println(item.getMetadata().getName());
        }
    }catch (Exception e){
        System.out.println(e);
    }
    System.out.println("Connected!");
    }
}

