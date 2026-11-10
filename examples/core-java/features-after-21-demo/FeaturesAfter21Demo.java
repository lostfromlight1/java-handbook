import java.lang.Runtime.Version;

public class FeaturesAfter21Demo {

    public static void main(String[] args) {
        Version runtime = Runtime.version();
        System.out.println("feature=" + runtime.feature());
        System.out.println("interim=" + runtime.interim());
        System.out.println("update=" + runtime.update());
        System.out.println("handbookPrimary=21-LTS");
        System.out.println("awareness=Document 22-24 non-LTS and 25-LTS in Module 19 Ch.02");
        System.out.println("upgradeCheck=java --version and Runtime.version() before adopting new syntax");
    }
}
