/**
 * @author rgundelach
 * @createdOn 1/29/2025 at 1:11 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers.Servers;
 */
package Rgundelach.Capstone.PageControllers.Servers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServersController {


    @GetMapping("/Home/Servers")
    public String getServers(){

    return null;
    }
}
