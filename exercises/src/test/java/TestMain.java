import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class TestMain {
    @Test
    public void test() {
        Mono<String> mono1 = asyncTask("mono1", Duration.ofMillis(1000));
        Mono<String> mono2 = asyncTask("mono2", Duration.ofMillis(2000));
        Mono<String> mono3 = asyncTask("mono3", Duration.ofMillis(3000));
        Mono<String> result = mono3.or(mono2).or(mono1);
        System.out.println(result.block()); // "mono1"
    }

    public static Mono<String> asyncTask(String name, Duration duration) {
        CompletableFuture<String> task = CompletableFuture.supplyAsync(() -> {
            Thread.currentThread().setName(name);
            try {
                Thread.sleep(duration.toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Thread.currentThread().getName();
        });
        return Mono.fromFuture(task);
    }
}
