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
import java.util.*;

@RestController
@RequestMapping("/API")
public class Kubernetes {
    //To see working kubectl logs testpod -n capstone-server --container test-container
    ApiClient client = null;
    final String WORKING_NAMESPACE = "capstone-server";
    final String HANDLER = "runc";
public void main() {
    if(client == null) {
        start();
    }
    System.out.println("Connected!");
    System.out.println("Starting Job");
    NameSpaceExists();
    RuntimeNameExists();
    CreatePersistentVolume();
    createPersistentVolumeClaim();

    createPod();

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
                .image("itzg/minecraft-server")
                .imagePullPolicy("Always")
                // .imagePullPolicy("IfNotPresent")//FIND IMAGE ID
                .addPortsItem(new V1ContainerPort().containerPort(32887).protocol("TCP"))
                .addPortsItem(new V1ContainerPort().containerPort(32887).protocol("UDP"))
                .addEnvItem(new V1EnvVar().name("EULA").value("TRUE")).volumeMounts(Collections.singletonList(
                        new V1VolumeMount()
                                .name("data-persistent-volume")  // Volume name (should match pod volume name)
                                .mountPath("/data")   // Mount path inside the container
                ));


        Map<String, Quantity> overheadResources = new HashMap<>();
        overheadResources.put("cpu",new Quantity("1000m"));
        overheadResources.put("memory",new Quantity("2Gi"));

        //chatGPT Volume
        V1Volume volume = new V1Volume()
                .name("data-persistent-volume")
                .persistentVolumeClaim(new V1PersistentVolumeClaimVolumeSource()
                        .claimName("data"));

        V1PodSpec podSpec = new V1PodSpec()
                .addContainersItem(container)
                .addVolumesItem(volume)
                .nodeName("desktop-control-plane")
                .overhead(overheadResources)
                .runtimeClassName("runtime")
                ;//.overhead(overheadResources);


        V1Pod pod = new V1Pod()
                .apiVersion("v1")
                .kind("Pod")//make service later idk mannnn
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
        overheadResources.put("cpu",new Quantity("1000m"));
        overheadResources.put("memory",new Quantity("2Gi"));
        //overheadResources.put("storage",new Quantity("1Gi"));
        overhead.setPodFixed(overheadResources);

        V1RuntimeClass runtimeClass = new V1RuntimeClass()
                .metadata(new V1ObjectMeta().name("runtime"))
                .kind("RuntimeClass")
                .handler(HANDLER)
                .overhead(overhead);
        try {

            //api.createRuntimeClass(runtimeClass).execute();
            V1RuntimeClassList list = api.listRuntimeClass().execute();
            try{
                if(list.getItems().get(0).getHandler().equals(HANDLER)){
                    return true;
                }
            }catch (IndexOutOfBoundsException e){
                api.createRuntimeClass(runtimeClass).execute();

            }
        } catch (ApiException e) {
            System.out.println(e);
        }


        return true;
}

//ChatGPT Method
    public void createPersistentVolumeClaim(){

        CoreV1Api api = new CoreV1Api(client);

        // Step 2: Define the PVC Metadata
        V1ObjectMeta metadata = new V1ObjectMeta()
                .name("data") // Name of the PVC
                .namespace(WORKING_NAMESPACE);

        // Step 3: Define PVC Spec
        V1PersistentVolumeClaimSpec pvcSpec = new V1PersistentVolumeClaimSpec()
                .accessModes(Collections.singletonList("ReadWriteOnce")) // Access Mode
                .resources(new V1VolumeResourceRequirements()
                        .requests(Collections.singletonMap("storage", new Quantity("10Gi"))))
                .volumeName("data-persistent-volume"); // Requested Storage

        // Step 4: Create PVC object
        V1PersistentVolumeClaim pvc = new V1PersistentVolumeClaim()
                .apiVersion("v1")
                .kind("PersistentVolumeClaim")
                .metadata(metadata)
                .spec(pvcSpec);

        // Step 5: Create PVC in Kubernetes
        V1PersistentVolumeClaim createdPvc = null;
        try {
            createdPvc = api.createNamespacedPersistentVolumeClaim(WORKING_NAMESPACE, pvc).execute();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println("PVC Created: " + createdPvc.getMetadata().getName());
    }

    public void CreatePersistentVolume(){
        CoreV1Api api = new CoreV1Api(client);

        V1PersistentVolume volume = new V1PersistentVolume()
                .apiVersion("v1")
                .kind("PersistentVolume")
                .spec(new V1PersistentVolumeSpec()
                        .capacity(Collections.singletonMap("storage", new Quantity("10Gi")))
                        .accessModes(Collections.singletonList("ReadWriteOnce"))
                        .persistentVolumeReclaimPolicy("Retain")
                        .hostPath(new V1HostPathVolumeSource().path("/mnt/data"))
                        .claimRef(new V1ObjectReference().name("data").namespace(WORKING_NAMESPACE)))
                .metadata(new V1ObjectMeta().namespace(WORKING_NAMESPACE).name("data-persistent-volume"));
        try {
            api.createPersistentVolume(volume).execute();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        ;// Access Mode
    }
}

