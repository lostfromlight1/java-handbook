final class CsvExporter implements Exporter {
    @Override
    public String format(String data) {
        return "csv:" + data;
    }
}
