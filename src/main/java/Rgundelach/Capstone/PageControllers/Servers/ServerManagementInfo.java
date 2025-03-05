/**
 * @author rgundelach
 * @createdOn 2/25/2025 at 12:28 PM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers.Servers;
 */
package Rgundelach.Capstone.PageControllers.Servers;

import java.util.ArrayList;
import java.util.List;

public class ServerManagementInfo {
    String[] names;
    String[] resources;

    public ServerManagementInfo(String[] names, String[] resources) {
        this.names = names;
        this.resources = resources;
    }

    public String[] getNames() {
        return names;
    }

    public String[] getResources() {
        return resources;
    }
}
