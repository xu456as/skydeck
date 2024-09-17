package io.skydeck.gserver.socketio;


import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SIOConfiguration {
    private SocketIOServer server;
    @Bean
    public SocketIOServer socketIOServer() throws Exception {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost");
        // 端口，任意
        config.setPort(8085);
        config.setMaxFramePayloadLength(1024 * 1024);
        config.setMaxHttpContentLength(1024 * 1024);
        //该处进行身份验证h
        config.setAuthorizationListener(handshakeData -> {
            //http://localhost:8081?username=test&password=test
            //例如果使用上面的链接进行connect，可以使用如下代码获取用户密码信息
            //String username = data.getSingleUrlParam("username");
            //String password = data.getSingleUrlParam("password");
            return AuthorizationResult.SUCCESSFUL_AUTHORIZATION;
        });
        server = new SocketIOServer(config);
        server.start();
        return server;
    }
    @PreDestroy
    public void stopSocketIOServer() {
        this.server.stop();
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }
}
