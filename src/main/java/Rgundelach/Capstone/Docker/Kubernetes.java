/**
 * @author rgundelach
 * @createdOn 2/4/2025 at 11:13 AM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.Docker;
 */
package Rgundelach.Capstone.Docker;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/API")
public class Kubernetes {
public void main() {
    try {
        ApiClient client = Config.defaultClient();
    }catch (Exception e){
        System.out.println(e);
    }
    System.out.println("Connected!");
    }
}

