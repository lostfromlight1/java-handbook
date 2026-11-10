/** Module 11 Ch.03 — StringBuilder and text blocks (JEP 378). */
public class StringBuilderTextBlockDemo {
    public static void main(String[] args) {
        StringBuilder greet = new StringBuilder();
        greet.append("Hello ").append("Java!");
        System.out.println(greet);

        String json = """
            {
              "name": "Ada",
              "lang": "Java"
            }
            """;
        long lines = json.lines().count();
        System.out.println("jsonLines=" + lines);
        System.out.print(json);
    }
}
