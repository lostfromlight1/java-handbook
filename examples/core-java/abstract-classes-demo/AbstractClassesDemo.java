/** Module 07 Ch.02 — abstract classes and template method style. */
public class AbstractClassesDemo {
    public static void main(String[] args) {
        Report pdf = new PdfReport("Q1");
        Report csv = new CsvReport("Q1");
        System.out.println(pdf.render());
        System.out.println(csv.render());
    }
}

abstract class Report {
    private final String title;

    Report(String title) {
        this.title = title;
    }

    final String render() {
        return header() + "\n" + body() + "\n" + footer();
    }

    protected String header() {
        return "REPORT:" + title;
    }

    protected abstract String body();

    protected String footer() {
        return "END";
    }
}

class PdfReport extends Report {
    PdfReport(String title) {
        super(title);
    }

    @Override
    protected String body() {
        return "pdf-body";
    }
}

class CsvReport extends Report {
    CsvReport(String title) {
        super(title);
    }

    @Override
    protected String body() {
        return "csv-body";
    }
}
