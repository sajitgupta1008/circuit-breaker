package async.utils;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.CompletableFuture;

public class RxJavaMapper {
    
    private RxJavaMapper() {
        // No-op
    }
    
    public static <T> CompletableFuture<T> toCompletableFuture(Observable<T> observable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        
        observable
                .single()
                .subscribeOn(Schedulers.io())
                .subscribe(future::complete, future::completeExceptionally);
        
        return future;
    }
}
