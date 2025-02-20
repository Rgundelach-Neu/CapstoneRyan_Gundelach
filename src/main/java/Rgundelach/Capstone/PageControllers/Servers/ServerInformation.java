/**
 * @author rgundelach
 * @createdOn 2/11/2025 at 11:54 AM
 * @projectName Capstone
 * @packageName Rgundelach.Capstone.PageControllers.Servers;
 */
package Rgundelach.Capstone.PageControllers.Servers;

public class ServerInformation {

    //String PVCName, String PVName,String PodName,String RuntimeName,int PortNumber
    String PodName;
    int PortNumber;


    public String getPodName() {
        return PodName;
    }


    public void setPodName(String podName) {
        podName = podName.toLowerCase();
        PodName = podName;

    }


    public int getPortNumber() {
        return PortNumber;
    }

    public void setPortNumber(int portNumber) {
        PortNumber = portNumber;
    }
}
