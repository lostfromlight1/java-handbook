public final class CreationalDemo {
    public static void main(String[] args) {
        Exporter csv = ExporterFactory.create(ExportKind.CSV);
        System.out.println("factory=" + csv.format("rows"));

        Report report = Report.builder().title("T").rowCount(2).build();
        System.out.println("builder=" + report);
        System.out.println("creational=ok");
    }
}
