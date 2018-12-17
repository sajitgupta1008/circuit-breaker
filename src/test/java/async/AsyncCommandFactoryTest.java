package async;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AsyncCommandFactoryTest {
    
    @Test
    public void testSingleAsyncCommandWithName() {
        final String key = "command_key";
        
        val command = AsyncCommand.factory().createWithName(key, null);
        
        assertEquals(key, command.getCommandKey().name());
        assertEquals(key, command.getCommandGroup().name());
    }
    
    @Test
    public void testAsyncCommandWithFallbackForName() {
        final String key = "command_key";
        
        val command = AsyncCommand.factory().createWithName(key, null, null);
        
        assertEquals(key, command.getCommandKey().name());
        assertEquals(key, command.getCommandGroup().name());
    }
    
    @Test
    public void testSingleAsyncCommandWithDefaultHystrixConfiguration() {
        final String key = "command_key_with_default_config";
        
        HystrixConfiguration hystrixConfiguration = HystrixConfiguration.builder().build();
        
        val command = AsyncCommand.factory().createWithName(key, null, null, hystrixConfiguration);
        
        assertEquals(key, command.getCommandKey().name());
        assertEquals(key, command.getCommandGroup().name());
        
        Integer expectedRollingStatisticalWindowInMilliseconds = 10000;
        assertEquals(expectedRollingStatisticalWindowInMilliseconds, command.getProperties()
                .metricsRollingStatisticalWindowInMilliseconds().get());
        
        Integer expectedCircuitBreakerRequestVolumeThreshold = 20;
        assertEquals(expectedCircuitBreakerRequestVolumeThreshold, command.getProperties().circuitBreakerRequestVolumeThreshold().get());
        
        Integer expectedCircuitBreakerErrorThresholdPercentage = 50;
        assertEquals(expectedCircuitBreakerErrorThresholdPercentage, command.getProperties().circuitBreakerErrorThresholdPercentage().get());
        
        Integer expectedExecutionTimeoutInMilliseconds = 10000;
        assertEquals(expectedExecutionTimeoutInMilliseconds, command.getProperties().executionTimeoutInMilliseconds().get());
    }
    
    @Test
    public void testSingleAsyncCommandWithUserSpecificHystrixConfiguration() {
        final String key = "command_key_with_user_specific_config";
        
        HystrixConfiguration hystrixConfiguration1 = HystrixConfiguration.builder()
                .timeoutInMilliseconds(1000)
                .rollingStatsTimeInMilliseconds(1000)
                .circuitBreakerRequestVolumeThreshold(1000)
                .circuitBreakerErrorThresholdPercentage(1000)
                .build();
        
        val command = AsyncCommand.factory().createWithName(key, null, null, hystrixConfiguration1);
        
        assertEquals(key, command.getCommandKey().name());
        assertEquals(key, command.getCommandGroup().name());
        
        Integer expectedRollingStatisticalWindowInMilliseconds = 1000;
        assertEquals(expectedRollingStatisticalWindowInMilliseconds, command.getProperties()
                .metricsRollingStatisticalWindowInMilliseconds().get());
        
        Integer expectedCircuitBreakerRequestVolumeThreshold = 1000;
        assertEquals(expectedCircuitBreakerRequestVolumeThreshold, command.getProperties().circuitBreakerRequestVolumeThreshold().get());
        
        Integer expectedCircuitBreakerErrorThresholdPercentage = 1000;
        assertEquals(expectedCircuitBreakerErrorThresholdPercentage, command.getProperties().circuitBreakerErrorThresholdPercentage().get());
        
        Integer expectedExecutionTimeoutInMilliseconds = 1000;
        assertEquals(expectedExecutionTimeoutInMilliseconds, command.getProperties().executionTimeoutInMilliseconds().get());
    }
}
