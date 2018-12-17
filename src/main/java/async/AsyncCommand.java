package async;

public class AsyncCommand {
    
    private static final AsyncCommandFactory FACTORY = new HystrixAsyncCommandFactory();
    
    private AsyncCommand() {
        // No-op
    }
    
    public static AsyncCommandFactory factory() {
        return FACTORY;
    }
}
