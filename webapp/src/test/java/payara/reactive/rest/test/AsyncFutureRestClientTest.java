package payara.reactive.rest.test;

import org.junit.Test;
import payara.reactive.rest.JaxrsResponseCallback;

import javax.ws.rs.client.ClientBuilder;

/**
 * Created by mertcaliskan
 */
public class AsyncFutureRestClientTest {

    @Test
    public void callAsync() {
        Thread mainThread = Thread.currentThread();
        JaxrsResponseCallback.get(ClientBuilder.newClient()
                .target("http://localhost:8080/JavaEEReactive/rest")
                .path("async")
                .request()
                .async())
                .thenAccept(
                        response -> {
                            System.out.println("Response code " + response.getStatus()
                                    + ", with content: " + response.readEntity(String.class));
                        }
                )
                .exceptionally(throwable -> {
                    System.out.println("Failed");
                    throwable.printStackTrace();
                    return null;
                }).thenRun(() -> {
                    System.out.println("Interrupting...");
                    mainThread.interrupt();
                });

        try {
            while (true) {
                Thread.sleep(1000);
                System.out.println("I'm alive");
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted, stopping.");
        }
    }
}
