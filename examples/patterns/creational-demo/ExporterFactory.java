enum ExportKind { CSV, JSON }

final class ExporterFactory {
    static Exporter create(ExportKind kind) {
        return switch (kind) {
            case CSV -> new CsvExporter();
            case JSON -> new JsonExporter();
        };
    }
}

final class JsonExporter implements Exporter {
    @Override
    public String format(String data) {
        return "json:" + data;
    }
}
