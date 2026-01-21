package tb2.protocol;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Referência para um objeto remoto
 * Contém informações de localização do objeto remoto
 */
public class RemoteObjectRef implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private InetAddress serverHost;
    private int serverPort;
    private String objectName;
    
    public RemoteObjectRef() {}
    
    public RemoteObjectRef(InetAddress serverHost, int serverPort, String objectName) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.objectName = objectName;
    }
    
    public InetAddress getServerHost() { return serverHost; }
    public void setServerHost(InetAddress serverHost) { this.serverHost = serverHost; }
    
    public int getServerPort() { return serverPort; }
    public void setServerPort(int serverPort) { this.serverPort = serverPort; }
    
    public String getObjectName() { return objectName; }
    public void setObjectName(String objectName) { this.objectName = objectName; }
    
    @Override
    public String toString() {
        return "RemoteObjectRef{host=" + serverHost.getHostAddress() + 
               ", port=" + serverPort + 
               ", object='" + objectName + "'}";
    }
}
