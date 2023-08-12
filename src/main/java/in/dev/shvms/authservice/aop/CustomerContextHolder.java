package in.dev.shvms.authservice.aop;

public class CustomerContextHolder {
    private static final ThreadLocal<String> currrentCustomerId = new ThreadLocal<>();

    public static void setCurrrentCustomerId(String customerId) {
        currrentCustomerId.set(customerId);
    }
    public static String getCurrentCustomerId() {
        return currrentCustomerId.get();
    }
    public static void clearCurrentCustomerId() {
        currrentCustomerId.remove();
    }
}
