/**
 * @author rgundelach
 * @createdOn 1/29/2025 at 1:11 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers.Servers;
 */
package Rgundelach.Capstone.PageControllers.Servers;

import Rgundelach.Capstone.Docker.Kubernetes;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ServersController {

    Kubernetes kubernetesAPI = new Kubernetes();

    @GetMapping("/Home/Server")
    public String getServers(Model model){
    List<String[]> info = kubernetesAPI.GetServerInformation();
    model.addAttribute("ServerInformation",new ServerInformation());
    model.addAttribute("Servers",info);
    return "Server";
    }
    @PostMapping("/Home/Server")
    public String PostServers(@ModelAttribute ServerInformation information){
        kubernetesAPI.CreateServer(information);
        return "HomeServerPage";
    }
}
