package async.utils;

import org.junit.Test;
import rx.Observable;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletionException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RxJavaMapperTest {
    
    @Test
    public void shouldConvertSuccessfulObservableToFuture() {
        final String expectedResult = "random";
        
        Observable<String> observable = Observable.just(expectedResult);
        
        String actualResult = RxJavaMapper
                .toCompletableFuture(observable)
                .join();
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void failToConvertObservableContainingMultipleElementsToFuture() {
        Observable<String> observable = Observable.from(Arrays.asList("john", "wick"));
        try {
            RxJavaMapper
                    .toCompletableFuture(observable)
                    .join();
            
            fail("An exception should have been thrown in the try block.");
        } catch (CompletionException ex) {
            assertThat(ex.getCause(), instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void failToConvertFailedObservableToFuture() {
        Observable<String> failed = Observable.error(new NoSuchElementException());
        
        try {
            RxJavaMapper
                    .toCompletableFuture(failed)
                    .join();
            
            fail("An exception should have been thrown in the try block.");
        } catch (CompletionException ex) {
            assertThat(ex.getCause(), instanceOf(NoSuchElementException.class));
        }
    }
}
