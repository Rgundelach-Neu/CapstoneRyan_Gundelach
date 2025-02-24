/**
 * @author rgundelach
 * @createdOn 2/20/2025 at 4:02 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers.Servers;
 */
package Rgundelach.Capstone.PageControllers.Servers;

import Rgundelach.Capstone.Docker.Kubernetes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DeleteController {
    Kubernetes kubernetesAPI = new Kubernetes();


}
