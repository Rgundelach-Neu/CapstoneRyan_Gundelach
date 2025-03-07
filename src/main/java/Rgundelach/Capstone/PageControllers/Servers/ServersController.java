/**
 * @author rgundelach
 * @createdOn 1/29/2025 at 1:11 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers.Servers;
 */
package Rgundelach.Capstone.PageControllers.Servers;

import Rgundelach.Capstone.Docker.Kubernetes;
import Rgundelach.Capstone.Models.Global.GlobalObjects;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ServersController {

    Kubernetes kubernetesAPI = new Kubernetes();
    List<Integer> portNumbers = new ArrayList<>(){{add(30056);add(30057);add(30058);add(30059);add(30060);add(31696);}};

    @GetMapping("/Home/Server")
    public String getServers(Model model){
        if (GlobalObjects.isAllowed()) {
            portNumbers = kubernetesAPI.CorrectPortNumber(portNumbers);
            List<ServerManagementInfo> info = new ArrayList<>();
            List<String[]> serverInfo = kubernetesAPI.GetServerInformation();
            List<String[]> resourceInfo = kubernetesAPI.GetResourceManagement();
            for (int i = 0; i < serverInfo.size(); i++) {
                info.add(new ServerManagementInfo(serverInfo.get(i), resourceInfo.get(i)));
            }
            model.addAttribute("ServerInformation", new ServerInformation());
            model.addAttribute("Servers", info);
            model.addAttribute("StringObject", new StringObject());
            model.addAttribute("AvailablePortNumbers", portNumbers);
            kubernetesAPI.GetResourceManagement();
            return "Server";
        }else {
            return "redirect:/loginUser";
        }
    }
    @PostMapping("/Home/Server")
    public String PostServers(Model model,@ModelAttribute ServerInformation information){
        if(!GlobalObjects.isAllowed()){
            return "redirect:/loginUser";
        }
        if(information.PodName.length() >15){
            portNumbers = kubernetesAPI.CorrectPortNumber(portNumbers);
            List<ServerManagementInfo> info = new ArrayList<>();
            List<String[]> serverInfo = kubernetesAPI.GetServerInformation();
            List<String[]> resourceInfo = kubernetesAPI.GetResourceManagement();
            for (int i = 0; i < serverInfo.size() ; i++) {
                info.add(new ServerManagementInfo(serverInfo.get(i), resourceInfo.get(i)));
            }

            model.addAttribute("ServerInformation",new ServerInformation());
            model.addAttribute("Servers",info);
            model.addAttribute("StringObject",new StringObject());
            model.addAttribute("AvailablePortNumbers",portNumbers);
            model.addAttribute("Error", Collections.singleton("ServerNames must be less than 15 Characters!"));
            return "Server";
        }
        information.setPodName(information.PodName.toLowerCase());
        portNumbers.remove(portNumbers.indexOf(kubernetesAPI.CreateServer(information)));
        return "redirect:/Home/Server";
    }
    @GetMapping("/Home/Server/Delete/{id}")
    public String PostServers(@PathVariable("id") String info){
        if(!GlobalObjects.isAllowed()){
            return "redirect:/loginUser";
        }
        portNumbers.add(kubernetesAPI.DeleteServer(info.toLowerCase()));
        return "redirect:/Home/Server";
    }


}
