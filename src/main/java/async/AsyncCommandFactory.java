package async;

import com.netflix.hystrix.HystrixObservableCommand;

import java.util.concurrent.Future;
import java.util.function.Supplier;

public interface AsyncCommandFactory {
    
    <Result> HystrixObservableCommand<Result> createWithName(String name,
                                                             Supplier<Future<Result>> supplier);
    
    <Result> HystrixObservableCommand<Result> createWithName(String name,
                                                             Supplier<Future<Result>> supplier,
                                                             int timeoutInMilliSeconds);
    
    <Result> HystrixObservableCommand<Result> createWithName(String name,
                                                             Supplier<Future<Result>> supplier,
                                                             Supplier<Future<Result>> fallback);
    
    <Result> HystrixObservableCommand<Result> createWithName(String name,
                                                             Supplier<Future<Result>> supplier,
                                                             Supplier<Future<Result>> fallback,
                                                             int timeoutInMilliSeconds);
    
    <Result> HystrixObservableCommand<Result> createWithName(String name,
                                                             Supplier<Future<Result>> supplier,
                                                             Supplier<Future<Result>> fallback,
                                                             HystrixConfiguration hystrixConfig);
}
