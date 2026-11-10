final class Report {
    private final String title;
    private final int rowCount;

    private Report(String title, int rowCount) {
        this.title = title;
        this.rowCount = rowCount;
    }

    static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "Report{title=" + title + ",rows=" + rowCount + "}";
    }

    static final class Builder {
        private String title = "Untitled";
        private int rowCount;

        Builder title(String title) {
            this.title = title;
            return this;
        }

        Builder rowCount(int rowCount) {
            this.rowCount = rowCount;
            return this;
        }

        Report build() {
            return new Report(title, rowCount);
        }
    }
}
