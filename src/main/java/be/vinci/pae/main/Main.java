package be.vinci.pae.main;

import be.vinci.pae.exception.mapper.WebExceptionMapper;
import be.vinci.pae.presentation.filters.CorsFilter;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;
import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * This is the main class of the application. It sets up and starts the Grizzly HTTP server,
 * exposing the JAX-RS resources defined in this application.
 */
public class  Main {

  /**
   * The BASE_URI constant represents the URI on which the Grizzly HTTP server will listen.
   */
  public static final String BASE_URI;


  // Load the configuration file and set the base URI
  static {
    String log4jConfigFile = "log4j2.xml";
    System.setProperty("log4j.configurationFile", log4jConfigFile);
    Config.load("dev.properties");
    BASE_URI = Config.getProperty("BaseUri");
  }

  /**
   * This method starts the Grizzly HTTP server, exposing the JAX-RS resources defined in this
   * application.
   *
   * @return the Grizzly HTTP server
   */
  public static HttpServer startServer() {
    // The package that contains the JAX-RS resources
    String resources = "be.vinci.pae.presentation";

    // Create a resource config that scans for JAX-RS resources and providers in the package
    final ResourceConfig rc = new ResourceConfig().packages(resources).register(CorsFilter.class)
        .register(ApplicationBinder.class).register(WebExceptionMapper.class);

    // Create and start a new instance of the Grizzly HTTP server
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  /**
   * The main method of the application. It starts the server and waits for the user to hit enter to
   * stop it.
   *
   * @param args the command-line arguments
   * @throws IOException if an I/O error occurs
   */
  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    System.out.println(String.format("Jersey app started with WADL available at "
        + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
    System.in.read();
    server.shutdownNow();
  }
}