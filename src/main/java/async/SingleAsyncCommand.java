package async;

import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;

import java.util.concurrent.Future;
import java.util.function.Supplier;

public class SingleAsyncCommand<Result> extends HystrixObservableCommand<Result> {
    
    private final Supplier<Future<Result>> supplier;
    
    protected SingleAsyncCommand(Setter setter,
                                 Supplier<Future<Result>> supplier) {
        super(setter);
        this.supplier = supplier;
    }
    
    @Override
    protected Observable<Result> construct() {
        return Observable.from(supplier.get());
    }
}
