package logic;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.headers.HttpCookie;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public class Main extends AllDirectives {

    private Map<UUID, User> storage = new HashMap<UUID, User>();

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("routes");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        Main app = new Main();

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();

        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());
    }

    private Route createRoute() {
        return concat(
                path("push", () -> (
                                optionalCookie("userName", userName -> {
                                            if (userName.isPresent()) {
                                                User user = storage.getOrDefault(userName, new User());
                                                System.out.println("present>>>");
                                                return complete("User state is balance: " + user.balance + "and bet: " + user.bet);
                                            } else {
                                                UUID uuid = UUID.randomUUID();
                                                storage.put(uuid, new User());
                                                System.out.println("<<<created");
                                                return setCookie(HttpCookie.create("userName", uuid.toString()),
                                                        () -> get(() -> complete("New user was created. " +
                                                                "Initial state. Balance - 100 and bet - 10."))
                                                );
                                            }
                                        }
                                )
                        )
                ),
                path("delete", () -> deleteCookie("userName", () -> complete("User was removed"))));
    }
}

class User {
    int balance;
    int bet;
    double threshold;

    User() {
        this.balance = 100;
        this.bet = 10;
        this.threshold = 0.5;
    }
}
