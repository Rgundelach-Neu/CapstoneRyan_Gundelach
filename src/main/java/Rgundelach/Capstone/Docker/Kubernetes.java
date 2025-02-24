/**
 * @author rgundelach
 * @createdOn 2/4/2025 at 11:13 AM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.Docker;
 */
package Rgundelach.Capstone.Docker;

import Rgundelach.Capstone.PageControllers.Servers.ServerInformation;
import com.google.gson.reflect.TypeToken;
import com.sun.jdi.event.ExceptionEvent;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.*;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.*;
import org.apache.catalina.Server;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Node;

import javax.naming.Name;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/API")
public class Kubernetes {
    public Kubernetes(){
        start();
    }
    //To see working kubectl logs testpod -n capstone-server --container test-container
    ApiClient client = null;
    final String WORKING_NAMESPACE = "capstone-server";
    final String HANDLER = "runc";
    private void main() {
    if (client == null) {
        start();
    }
    String PVCName ="server-persistent-volume-claim";
    String PVName ="server-persistent-volume";
    System.out.println("Connected!");
    System.out.println("Starting Job");
    NameSpaceExists();
    //allIngresPolicy();
    RuntimeNameExists("temp");
    CreatePersistentVolume(PVName,PVCName);
    createPersistentVolumeClaim(PVCName,PVName);
   // createPod();

}
    public void CreateServer(String PVCName, String PVName,String PodName,String RuntimeName,int PortNumber){
        NameSpaceExists();
        RuntimeNameExists(RuntimeName);
        CreatePersistentVolume(PVName,PVCName);
        createPersistentVolumeClaim(PVCName,PVName);
        createPod(PodName,PortNumber,PVCName,PVName,RuntimeName);
    }
    public void CreateServer(ServerInformation info){
        String PVCName = info.getPodName()+"-volume-claim";
        String PVName = info.getPodName()+"-volume";
        String RuntimeName =info.getPodName()+"-runtime";
        NameSpaceExists();

        RuntimeNameExists(RuntimeName);
        CreatePersistentVolume(PVName,PVCName);
        createPersistentVolumeClaim(PVCName,PVName);
        //createPod(info.getPodName(),info.getPortNumber(),PVCName,PVName,RuntimeName);
        createServer(info.getPodName(),info.getPortNumber(),PVCName,PVName,RuntimeName);
         createService(info.getPodName(), info.getPortNumber());
        //allIngresPolicy(info.getPortNumber(),info.getPodName());
    }
    public void DeleteServer(String ServerName){
        String PVCName =ServerName+"-volume-claim";
        String PVName =ServerName+"-volume";
        String RuntimeName =ServerName+"-runtime";
        deleteServer(ServerName);
        deleteService(ServerName);
        deletePV(PVName);
        deletePVC(PVCName);
        deleteRuntime(RuntimeName);
    }

    //Connects Kubernetes
    private void start(){
        try {
            // System.setProperty("KUBECONFIG", "C:\\Users\\rgundelach\\.kube\\config");
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader("C:\\Users\\rgundelach\\.kube\\config"))).build(); //ChatGPT
            Configuration.setDefaultApiClient(client);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    private void createPod(String PodName,int portNumber, String PVCName,String PVName, String RuntimeName){

    //ChatGPT for template
    CoreV1Api api = new CoreV1Api(client);
        V1ObjectMeta metaData = new V1ObjectMeta()
                .name(PodName).namespace(WORKING_NAMESPACE);

        //Container For MC Server
        V1Container container = new V1Container().name("test-container")
                .image("itzg/minecraft-server")
                .imagePullPolicy("Always")
                // .imagePullPolicy("IfNotPresent")//FIND IMAGE ID
                .addPortsItem(new V1ContainerPort().hostPort(portNumber).protocol("TCP"))
                .addPortsItem(new V1ContainerPort().hostPort(portNumber).protocol("UDP"))
                .addEnvItem(new V1EnvVar().name("EULA").value("TRUE")).volumeMounts(Collections.singletonList(
                        new V1VolumeMount()
                                .name(PVName)  // Volume name (should match pod volume name)
                                .mountPath("/"+PVName)   // Mount path inside the container
                ));


        Map<String, Quantity> overheadResources = new HashMap<>();
        overheadResources.put("cpu",new Quantity("1000m"));
        overheadResources.put("memory",new Quantity("2Gi"));

        //chatGPT Volume
        V1Volume volume = new V1Volume()
                .name(PVName)
                .persistentVolumeClaim(new V1PersistentVolumeClaimVolumeSource()
                        .claimName(PVCName));

        V1PodSpec podSpec = new V1PodSpec()
                .addContainersItem(container)
                .addVolumesItem(volume)
                .nodeName("kind-control-plane")
                .overhead(overheadResources)
                .runtimeClassName(RuntimeName)
                ;//.overhead(overheadResources);


        V1Pod pod = new V1Pod()
                .apiVersion("v1")
                .kind("Pod")//make service later idk mannnn //Depolyment!
                .metadata(metaData)
                .spec(podSpec);


        try {

           V1Pod createdPod = api.createNamespacedPod(WORKING_NAMESPACE,pod).execute();

            System.out.println("Created Pod: "+createdPod.getMetadata().getName());
        } catch (ApiException e) {
            System.out.println(e);
        }
    }
    private boolean NameSpaceExists(){
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
    private boolean RuntimeNameExists(String RuntimeName){
        NodeV1Api api = new NodeV1Api(client);
        try{
         V1RuntimeClassList runtimeClassList =  api.listRuntimeClass().execute();
         for (V1RuntimeClass runtime : runtimeClassList.getItems()) {
             if(runtime.getMetadata().getName().equals(RuntimeName)){
                 return true;
             }
         }
        }catch (Exception e){}
        V1Overhead overhead = new V1Overhead();
        //ChatGPT code for overheadResources
        Map<String, Quantity> overheadResources = new HashMap<>();
        overheadResources.put("cpu",new Quantity("1000m"));
        overheadResources.put("memory",new Quantity("2Gi"));
        //overheadResources.put("storage",new Quantity("1Gi"));
        overhead.setPodFixed(overheadResources);

        V1RuntimeClass runtimeClass = new V1RuntimeClass()
                .metadata(new V1ObjectMeta().name(RuntimeName))
                .kind("RuntimeClass")
                .handler(HANDLER)
                .overhead(overhead);
        try {

            //api.createRuntimeClass(runtimeClass).execute();
            V1RuntimeClass runtimeCreated = api.createRuntimeClass(runtimeClass).execute();
            System.out.println("Runtime Created: " + runtimeCreated.getMetadata().getName());
        } catch (ApiException e) {
            System.out.println(e);
        }


        return true;
}

//ChatGPT Method
    private void createPersistentVolumeClaim(String PVCName,String PVName){

        CoreV1Api api = new CoreV1Api(client);
        try {
            V1PersistentVolumeClaimList PVCList = api.listNamespacedPersistentVolumeClaim(WORKING_NAMESPACE).execute();
            // System.out.println(namespacesList);
            for (V1PersistentVolumeClaim pvc : PVCList.getItems()) {
                if(pvc.getMetadata().getName().equals(PVCName)){
                    return;
                }
            }
        }
            catch(Exception e){}

        // Step 2: Define the PVC Metadata
        V1ObjectMeta metadata = new V1ObjectMeta()
                .name(PVCName) // Name of the PVC
                .namespace(WORKING_NAMESPACE);

        // Step 3: Define PVC Spec
        V1PersistentVolumeClaimSpec pvcSpec = new V1PersistentVolumeClaimSpec()
                .accessModes(Collections.singletonList("ReadWriteOnce")) // Access Mode
                .resources(new V1VolumeResourceRequirements()
                        .requests(Collections.singletonMap("storage", new Quantity("10Gi"))))
                .volumeName(PVName); // Requested Storage

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

    private void CreatePersistentVolume(String PVName,String PVCName){
        CoreV1Api api = new CoreV1Api(client);

        try {
            V1PersistentVolumeList PVList = api.listPersistentVolume().execute();
            // System.out.println(namespacesList);
            for (V1PersistentVolume pvc : PVList.getItems()) {
                if(pvc.getMetadata().getName().equals(PVName)){
                    return;
                }
            }
        }
        catch(Exception e){}

        V1PersistentVolume volume = new V1PersistentVolume()
                .apiVersion("v1")
                .kind("PersistentVolume")
                .spec(new V1PersistentVolumeSpec()
                        .capacity(Collections.singletonMap("storage", new Quantity("10Gi")))
                        .accessModes(Collections.singletonList("ReadWriteOnce"))
                        .persistentVolumeReclaimPolicy("Retain")
                        .hostPath(new V1HostPathVolumeSource().path("/mnt/"+PVName))
                        .claimRef(new V1ObjectReference().name(PVCName).namespace(WORKING_NAMESPACE)))
                .metadata(new V1ObjectMeta().namespace(WORKING_NAMESPACE).name(PVName));
        try {
            api.createPersistentVolume(volume).execute();
            System.out.println("Persistent Volume Created");
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        ;// Access Mode
    }

    private void createServer(String ServerName,int portNumber, String PVCName,String PVName, String RuntimeName){

        //ChatGPT for template
        AppsV1Api api = new AppsV1Api(client);
        V1ObjectMeta metaData = new V1ObjectMeta()
                .name(ServerName).namespace(WORKING_NAMESPACE)
                .labels(Collections.singletonMap("app",ServerName));

        //Container For MC Server
        V1Container container = new V1Container().name("test-container")
                .image("itzg/minecraft-server:latest")
                .imagePullPolicy("IfNotPresent")
                // .imagePullPolicy("IfNotPresent")//FIND IMAGE ID
                .addPortsItem(new V1ContainerPort().containerPort(25565).name(ServerName+"-tcp").protocol("TCP").hostPort(portNumber).protocol("TCP"))
                .addPortsItem(new V1ContainerPort().containerPort(25565).name(ServerName+"-udp").protocol("UDP").hostPort(portNumber).protocol("UDP"))
                .addEnvItem(new V1EnvVar().name("EULA").value("TRUE"))
                .addEnvItem(new V1EnvVar().name("query.port").value(Integer.toString(portNumber)))
                .volumeMounts(Collections.singletonList(
                        new V1VolumeMount()
                                .name(PVName)  // Volume name (should match pod volume name)
                                .mountPath("/"+PVName)   // Mount path inside the container
                ));


        Map<String, Quantity> overheadResources = new HashMap<>();
        overheadResources.put("cpu",new Quantity("1000m"));
        overheadResources.put("memory",new Quantity("2Gi"));

        //chatGPT Volume
        V1Volume volume = new V1Volume()
                .name(PVName)
                .persistentVolumeClaim(new V1PersistentVolumeClaimVolumeSource()
                        .claimName(PVCName));

        V1DeploymentSpec deploymentSpec = new V1DeploymentSpec()
                .replicas(1)
                .selector(new V1LabelSelector().matchLabels(Collections.singletonMap("app",ServerName)))
                .template(
                    new V1PodTemplateSpec().metadata(new V1ObjectMeta()
                                    .name(ServerName+"-replica")
                                    .namespace(WORKING_NAMESPACE)
                                    .labels(Collections.singletonMap("app",ServerName))
                                )
                            .spec(new V1PodSpec().addContainersItem(container)
                                    .addVolumesItem(volume)
                                    .nodeName("kind-control-plane")
                                    .overhead(overheadResources)
                                    .runtimeClassName(RuntimeName))
        );


        V1Deployment deployment = new V1Deployment()
                .apiVersion("apps/v1")
                .kind("Deployment")//make service later idk mannnn //Depolyment!
                .metadata(metaData)
                .spec(deploymentSpec);


        try {

            V1Deployment createdDeployment = api.createNamespacedDeployment(WORKING_NAMESPACE,deployment).execute();
            System.out.println("Created Deployment: "+createdDeployment.getMetadata().getName());

        } catch (ApiException e) {
            System.out.println(e);
        }
    }
    private void createService(String ServerName,int portNumber){
        CoreV1Api api = new CoreV1Api(client);
        try{
            for (V1Service service : api.listNamespacedService(WORKING_NAMESPACE).execute().getItems()) {
                if(service.getMetadata().getName().equals(ServerName+"-service")){
                    return;
                }
            }
        }catch (Exception e){}
        V1Service service = new V1Service()
                .apiVersion("v1")
                .kind("Service")
                .metadata(new V1ObjectMeta().namespace(WORKING_NAMESPACE).name(ServerName+"-service"))
                .spec(new V1ServiceSpec()
                          .addPortsItem(
                                new V1ServicePort()
                                        .nodePort(portNumber)
                                        .port(25565)
                                        .targetPort(new IntOrString(25565))
                                        .protocol("TCP")
                                        .name(ServerName+"-tcp"))
                        .addPortsItem(
                                new V1ServicePort()
                                        .nodePort(portNumber)
                                        .port(25565).targetPort(new IntOrString(25565))
                                        .protocol("UDP")
                                        .name(ServerName+"-udp"))
                        .ipFamilyPolicy("SingleStack")
                        .selector(Collections.singletonMap("app",ServerName))
                        .type("NodePort")


                );

        V1Service createdService = null;
        try {
            createdService = api.createNamespacedService(WORKING_NAMESPACE,service).execute();
            System.out.println("Created Service: "+createdService.getMetadata().getName());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

    }
    public List<String[]> GetServerInformation(){

           List<String[]> ServerInfo = new ArrayList<>();
        try {
           CoreV1Api api = new CoreV1Api(client);
           AppsV1Api apiDeploy = new AppsV1Api(client);

           V1ServiceList ServiceInfo;
           V1DeploymentList Deploymentinfo;
           try {
               ServiceInfo = api.listNamespacedService(WORKING_NAMESPACE).execute();
               Deploymentinfo = apiDeploy.listNamespacedDeployment(WORKING_NAMESPACE).execute();
           } catch (ApiException e) {
               throw new RuntimeException(e);
           }
           for (int i = 0; i < Deploymentinfo.getItems().size(); i++) {
               String[] ServerInfoItem = {
                       Deploymentinfo.getItems().get(i).getMetadata().getName(),
                       "localhost:"+ServiceInfo.getItems().get(i).getSpec().getPorts().get(0).getNodePort().toString()
               };
               ServerInfo.add(ServerInfoItem);
           }
       }catch (IndexOutOfBoundsException ex){}
        return ServerInfo;
    }

    private void deleteServer(String serverName){
        AppsV1Api api = new AppsV1Api(client);
        try {
            api.deleteNamespacedDeployment(serverName,WORKING_NAMESPACE).execute();
        } catch (ApiException e) {
            System.out.println("Server Deleted Un-successfully");
            return;
        }
        System.out.println("Server Deleted Successfully");
    }
    private void deletePV(String PVName){
        CoreV1Api api =new CoreV1Api(client);
        try {
            api.deletePersistentVolume(PVName).execute();
        } catch (ApiException e) {
            System.out.println("VolumeDeleted un-Successfully");
            return;
        }
        System.out.println("VolumeDeleted un-Successfully");
    }
    private void deletePVC(String PVCName){
        CoreV1Api api =new CoreV1Api(client);
        try {
            api.deleteNamespacedPersistentVolumeClaim(PVCName,WORKING_NAMESPACE).execute();
        } catch (ApiException e) {
            System.out.println("VolumeClaim Deleted un-Successfully");
        }
        System.out.println("VolumeClaim Deleted un-Successfully");
        return;
    }
    private void deleteService(String ServerName){
        CoreV1Api api = new CoreV1Api(client);
        try {
            api.deleteNamespacedService(ServerName+"-service",WORKING_NAMESPACE).execute();
        } catch (ApiException e) {
            System.out.println("Service Deleted un-successfully");
            return;
        }
        System.out.println("Service Deleted successfully");

    }
    private void deleteRuntime(String RunTimename){
        NodeV1Api api = new NodeV1Api(client);
        try {
            api.deleteRuntimeClass(RunTimename).execute();
        } catch (ApiException e) {
            System.out.println("Runtime Delete Un-successfully");
            return;
        }
        System.out.println("Runtime Delete successfully");

    }
}

