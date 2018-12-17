package async;

import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;

import java.util.concurrent.Future;
import java.util.function.Supplier;

public class AsyncCommandWithFallback<Result> extends HystrixObservableCommand<Result> {
    
    private final Supplier<Future<Result>> supplier;
    private final Supplier<Future<Result>> fallback;
    
    protected AsyncCommandWithFallback(Setter setter,
                                       Supplier<Future<Result>> supplier,
                                       Supplier<Future<Result>> fallback) {
        super(setter);
        this.supplier = supplier;
        this.fallback = fallback;
    }
    
    @Override
    protected Observable<Result> construct() {
        return Observable.from(supplier.get());
    }
    
    @Override
    protected Observable<Result> resumeWithFallback() {
        return Observable.from(fallback.get());
    }
}
