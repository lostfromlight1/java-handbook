/** Module 14 Ch.01 — Throwable hierarchy: Error vs Exception, checked vs unchecked. */
public class ThrowableDemo {
    public static void main(String[] args) {
        try {
            mightFail(true);
        } catch (IllegalArgumentException ex) {
            System.out.println("caughtUnchecked=" + ex.getMessage());
        }

        try {
            readConfig();
        } catch (ConfigException ex) {
            System.out.println("caughtChecked=" + ex.getMessage());
        }

        System.out.println("runtimeIsUnchecked=" + (new RuntimeException() instanceof Exception));
        System.out.println("errorIsThrowable=" + (new OutOfMemoryError() instanceof Throwable));
    }

    static void mightFail(boolean bad) {
        if (bad) {
            throw new IllegalArgumentException("bad-arg");
        }
    }

    static void readConfig() throws ConfigException {
        throw new ConfigException("missing-file");
    }
}

class ConfigException extends Exception {
    ConfigException(String message) {
        super(message);
    }
}
