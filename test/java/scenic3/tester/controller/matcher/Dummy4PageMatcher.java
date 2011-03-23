package scenic3.tester.controller.matcher;

// @javax.annotation.Generated
public class Dummy4PageMatcher extends scenic3.UrlMatcherImpl {

    private static final Dummy4PageMatcher INSTANCE = new Dummy4PageMatcher();
    /**
     * get a instance of this class.
     */
    public static Dummy4PageMatcher get() {
        return INSTANCE;
    }

    // Constractor.
    private Dummy4PageMatcher() {
        super("/");
        super.add(new scenic3.UrlPattern("/", ""), "net.sue445.jubeatplusplus.controller.$Index");
    }


}
