/** Module 06 Ch.03 — immutable types and builder pattern. */
public class ImmutabilityBuilderDemo {
    public static void main(String[] args) {
        Money m = new Money(1250, "USD");
        System.out.println(m);

        HttpRequest req = HttpRequest.builder()
                .method("GET")
                .url("https://example.com/api")
                .header("Accept", "application/json")
                .build();
        System.out.println(req);
    }
}

final class Money {
    private final int cents;
    private final String currency;

    Money(int cents, String currency) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("currency required");
        }
        this.cents = cents;
        this.currency = currency;
    }

    int cents() {
        return cents;
    }

    String currency() {
        return currency;
    }

    Money plus(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("currency mismatch");
        }
        return new Money(cents + other.cents, currency);
    }

    @Override
    public String toString() {
        return cents + " " + currency;
    }
}

final class HttpRequest {
    private final String method;
    private final String url;
    private final String accept;

    private HttpRequest(Builder b) {
        this.method = b.method;
        this.url = b.url;
        this.accept = b.accept;
    }

    static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return method + " " + url + " Accept=" + accept;
    }

    static final class Builder {
        private String method = "GET";
        private String url;
        private String accept = "*/*";

        Builder method(String method) {
            this.method = method;
            return this;
        }

        Builder url(String url) {
            this.url = url;
            return this;
        }

        Builder header(String name, String value) {
            if ("Accept".equalsIgnoreCase(name)) {
                this.accept = value;
            }
            return this;
        }

        HttpRequest build() {
            if (url == null || url.isBlank()) {
                throw new IllegalStateException("url required");
            }
            return new HttpRequest(this);
        }
    }
}
