package in.dev.shvms.authservice.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CustomerContextHolder {

    static Logger logger = LoggerFactory.getLogger(CustomerContextFilter.class);
    private static final InheritableThreadLocal<String> currentCustomerId = new InheritableThreadLocal<>();
    // In case of ThreadLocal child threads will get currentCustomerId as NULL

    public static void setCurrentCustomerId(String customerId) {
        currentCustomerId.set(customerId);
        logger.debug("Setting customer-context: {}", customerId);
    }
    public static String getCurrentCustomerId() {
        /*
         * This (ThreadLocal) will work for Synchronous App, Since we are using reactive/non-blocking spring framework,
         * this will not work, threads will keep on changing, there is no single thread for 1 request concept,
         * so will get inconsistent and even wrong values of currentCustomerId.
         * Also, just for demonstration, we are checking if null and returning DEFAULT which is not correct ideally.
         *
         * TODO : What (is the best way) & (are all ways) to dynamically switch DB in case of spring reactive without compromising non-blocking feature.
         */
//        return currentCustomerId.get();
        return Objects.nonNull(currentCustomerId.get()) ? currentCustomerId.get(): "DEFAULT";
    }
    public static void clearCurrentCustomerId() {
        currentCustomerId.remove();
        logger.debug("Clearing customer-context: {}", getCurrentCustomerId());
    }
}
