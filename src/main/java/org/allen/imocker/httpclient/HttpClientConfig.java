package org.allen.imocker.httpclient;

/**
 * http client configuration
 */
public class HttpClientConfig {

    // maximum total connection value
    private int maxConnTotal = 200;
    // maximum connection per route value
    private int maxConnPerRoute = 20;
    // socket timeout value for non-blocking I/O operations
    private int socketTimeout = 4000;
    // Determines the default value of the {@link java.net.SocketOptions#SO_KEEPALIVE} parameter for newly created sockets
    private boolean socketKeepAlive = true;
    // Determines the default value of the {@link java.net.SocketOptions#TCP_NODELAY} parameter for newly created sockets
    private boolean tcpNoDelay = true;
    // Determines the default value of the {@link java.net.SocketOptions#SO_LINGER} parameter for newly created sockets
    private int socketLinger = -1;
    //
    private int connTimeToLive = -1;

    public int getMaxConnTotal() {
        return maxConnTotal;
    }

    public void setMaxConnTotal(int maxConnTotal) {
        this.maxConnTotal = maxConnTotal;
    }

    public int getMaxConnPerRoute() {
        return maxConnPerRoute;
    }

    public void setMaxConnPerRoute(int maxConnPerRoute) {
        this.maxConnPerRoute = maxConnPerRoute;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public boolean isSocketKeepAlive() {
        return socketKeepAlive;
    }

    public void setSocketKeepAlive(boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public int getSocketLinger() {
        return socketLinger;
    }

    public void setSocketLinger(int socketLinger) {
        this.socketLinger = socketLinger;
    }

    public int getConnTimeToLive() {
        return connTimeToLive;
    }

    public void setConnTimeToLive(int connTimeToLive) {
        this.connTimeToLive = connTimeToLive;
    }
}
