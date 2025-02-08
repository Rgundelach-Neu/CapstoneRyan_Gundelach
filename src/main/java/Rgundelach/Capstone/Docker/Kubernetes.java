/**
 * @author rgundelach
 * @createdOn 2/4/2025 at 11:13 AM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.Docker;
 */
package Rgundelach.Capstone.Docker;

import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.NodeV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Name;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/API")
public class Kubernetes {
    ApiClient client = null;
    final String WORKING_NAMESPACE = "capstone-server";
public void main() {
    if(client == null) {
        start();
    }
    System.out.println("Connected!");
    System.out.println("Starting Job");
    RuntimeNameExists();
    createPod();
    NameSpaceExists();
    }

    //Connects Kubernetes
    public void start(){
        try {
            // System.setProperty("KUBECONFIG", "C:\\Users\\rgundelach\\.kube\\config");
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader("C:\\Users\\rgundelach\\.kube\\config"))).build(); //ChatGPT
            Configuration.setDefaultApiClient(client);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void createPod(){
    //ChatGPT for template
    CoreV1Api api = new CoreV1Api(client);
        V1ObjectMeta metaData = new V1ObjectMeta()
                .name("testpod").namespace(WORKING_NAMESPACE);

        V1Container container = new V1Container().name("test-container")
                .image("itzg/minecraft-server") //FIND IMAGE ID
                .addPortsItem(new V1ContainerPort().containerPort(25565))
                .addEnvItem(new V1EnvVar().name("EULA").value("TRUE"));



        V1PodSpec podSpec = new V1PodSpec()
                .addContainersItem(container)
                .runtimeClassName("runtime");


        V1Pod pod = new V1Pod()
                .apiVersion("v1")
                .kind("Pod")//make service later
                .metadata(metaData)
                .spec(podSpec);

        try {
           V1Pod createdPod = api.createNamespacedPod(WORKING_NAMESPACE,pod).execute();
            System.out.println("Created Pod: "+createdPod.getMetadata().getName());
        } catch (ApiException e) {
            System.out.println(e);
        }
    }
    public boolean NameSpaceExists(){
        CoreV1Api api = new CoreV1Api(client);

        try {
            V1NamespaceList namespacesList = api.listNamespace().execute();
           // System.out.println(namespacesList);
            for (V1Namespace namespace : namespacesList.getItems()) {
                if(namespace.getMetadata().getName().equals(WORKING_NAMESPACE)){
                    return true;
                }
            }
            V1Namespace NewNamespace = new V1Namespace()
                    .kind("Namespace")
                    .apiVersion("v1")
                    .metadata(new V1ObjectMeta().name(WORKING_NAMESPACE));
            api.createNamespace(NewNamespace).execute();
            return true;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean RuntimeNameExists(){
        NodeV1Api api = new NodeV1Api(client);
        V1Overhead overhead = new V1Overhead();
        //ChatGPT code for overheadResources
        Map<String, Quantity> overheadResources = new HashMap<>();
        overheadResources.put("cpu",new Quantity("4000m"));
        overheadResources.put("memory",new Quantity("2000m"));
        overhead.setPodFixed(overheadResources);

        V1RuntimeClass runtimeClass = new V1RuntimeClass()
                .metadata(new V1ObjectMeta().name("runtime"))
                .kind("RuntimeClass")
                .handler("runc")
                .overhead(overhead);
        try {

            //api.createRuntimeClass(runtimeClass).execute();
            V1RuntimeClassList list = api.listRuntimeClass().execute();

            if(list.getItems().get(0).getHandler().equals("runc")){
                return true;
            }else {
                api.createRuntimeClass(runtimeClass).execute();
            }
        } catch (ApiException e) {
            System.out.println(e);
        }


        return true;
    }
}

