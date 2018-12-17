package async;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class HystrixConfiguration {
    
    @Builder.Default
    Integer timeoutInMilliseconds = 10000;
    
    @Builder.Default
    Integer circuitBreakerRequestVolumeThreshold = 20;
    
    @Builder.Default
    Integer rollingStatsTimeInMilliseconds = 10000;
    
    @Builder.Default
    Integer circuitBreakerErrorThresholdPercentage = 50;
    
}
