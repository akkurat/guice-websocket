package com.asafalima.websocket.server;

import com.asafalima.websocket.di.GuiceConfigurator;
import com.asafalima.websocket.endpoints.EchoEndpoint;
import com.asafalima.websocket.endpoints.GamePoint;
import com.google.inject.Injector;
import jakarta.servlet.Servlet;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.websocket.core.exception.InvalidWebSocketException;

import java.util.Arrays;

public class JettyServer {

    private GuiceConfigurator configurator;
    private Server server;
    private int port;

    public JettyServer(int port, Injector injector) {
        this.port = port;
        this.configurator = new GuiceConfigurator(injector);
    }

    public boolean shutdown() {
        try {
            server.stop();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void start() throws Exception {
        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
//        context.addFilter(AuthenticationFilter.class, "/*", null);
        context.setServer(server);

        // Add static files handler
        context.setBaseResource(Resource.newResource(JettyServer.class.getResource("/webapp")));
        context.addServlet(DefaultServlet.class, "/");
        context.setWelcomeFiles(new String[]{"index.html"});

        var wsEndpoint = createEndpointConfig(EchoEndpoint.class);

//        var restConfig = new ResourceConfig(GamePoint.class);
//        Servlet servletContainer = new ServletContainer(restConfig);
//        var restServlet = new ServletHolder(servletContainer);
//        context.addServlet(restServlet, "/rest");

        ServletContextHandler contextJ = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        contextJ.setServer(server);


        server.setHandler(context);
        server.start();
    }

    private ServerEndpointConfig createEndpointConfig(Class<?> endpointClass) throws DeploymentException {
        ServerEndpoint annotation = endpointClass.getAnnotation(ServerEndpoint.class);
        if (annotation == null) {
            throw new InvalidWebSocketException("Unsupported WebSocket object, missing @" + ServerEndpoint.class + " annotation");
        }

        return ServerEndpointConfig.Builder.create(endpointClass, annotation.value())
                .subprotocols(Arrays.asList(annotation.subprotocols()))
                .decoders(Arrays.asList(annotation.decoders()))
                .encoders(Arrays.asList(annotation.encoders()))
                .configurator(configurator)
                .build();
    }

}