package async;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

import java.util.concurrent.Future;
import java.util.function.Supplier;

class HystrixAsyncCommandFactory implements AsyncCommandFactory {
    
    private static final int DEFAULT_TIMEOUT_IN_MILLISECONDS = 1000;
    
    @Override
    public <Result> HystrixObservableCommand<Result> createWithName(String name,
                                                                    Supplier<Future<Result>> supplier) {
        return createWithName(name, supplier, DEFAULT_TIMEOUT_IN_MILLISECONDS);
    }
    
    @Override
    public <Result> HystrixObservableCommand<Result> createWithName(String name,
                                                                    Supplier<Future<Result>> supplier,
                                                                    int timeoutInMilliSeconds) {
        return new SingleAsyncCommand<>(
                setterForGroup(HystrixCommandGroupKey.Factory.asKey(name), timeoutInMilliSeconds)
                        .andCommandKey(HystrixCommandKey.Factory.asKey(name)),
                supplier);
    }
    
    @Override
    public <Result> HystrixObservableCommand<Result> createWithName(String name,
                                                                    Supplier<Future<Result>> supplier,
                                                                    Supplier<Future<Result>> fallback) {
        return createWithName(name, supplier, fallback, DEFAULT_TIMEOUT_IN_MILLISECONDS);
    }
    
    @Override
    public <Result> HystrixObservableCommand<Result> createWithName(String name,
                                                                    Supplier<Future<Result>> supplier,
                                                                    Supplier<Future<Result>> fallback,
                                                                    int timeoutInMilliSeconds) {
        return new AsyncCommandWithFallback<>(
                setterForGroup(HystrixCommandGroupKey.Factory.asKey(name), timeoutInMilliSeconds)
                        .andCommandKey(HystrixCommandKey.Factory.asKey(name)),
                supplier,
                fallback);
    }
    
    @Override
    public <Result> HystrixObservableCommand<Result> createWithName(String name,
                                                                    Supplier<Future<Result>> supplier,
                                                                    Supplier<Future<Result>> fallback,
                                                                    HystrixConfiguration hystrixConfig) {
        return new AsyncCommandWithFallback<>(
                setterForGroupWithHystrixProperties(HystrixCommandGroupKey.Factory.asKey(name), hystrixConfig)
                        .andCommandKey(HystrixCommandKey.Factory.asKey(name)),
                supplier,
                fallback);
    }
    
    private HystrixObservableCommand.Setter setterForGroup(HystrixCommandGroupKey group,
                                                           int timeoutInMilliseconds) {
        return HystrixObservableCommand.Setter.withGroupKey(group)
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(timeoutInMilliseconds));
    }
    
    private HystrixObservableCommand.Setter setterForGroupWithHystrixProperties(HystrixCommandGroupKey group,
                                                                                HystrixConfiguration hystrixConfig) {
        return HystrixObservableCommand.Setter.withGroupKey(group)
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(hystrixConfig.timeoutInMilliseconds)
                        .withMetricsRollingStatisticalWindowInMilliseconds(hystrixConfig.rollingStatsTimeInMilliseconds)
                        .withCircuitBreakerRequestVolumeThreshold(hystrixConfig
                                .circuitBreakerRequestVolumeThreshold)
                        .withCircuitBreakerErrorThresholdPercentage(hystrixConfig
                                .circuitBreakerErrorThresholdPercentage));
    }
}
