package scenic3.tester.controller.matcher;

// @javax.annotation.Generated
public class Dummy1PageMatcher extends scenic3.UrlMatcherImpl {

    private static final Dummy1PageMatcher INSTANCE = new Dummy1PageMatcher();
    /**
     * get a instance of this class.
     */
    public static Dummy1PageMatcher get() {
        return INSTANCE;
    }

    // Constractor.
    private Dummy1PageMatcher() {
        super("/");
        super.add(new scenic3.UrlPattern("/", ""), "net.sue445.jubeatplusplus.controller.$Index");
    }


}
