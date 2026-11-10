import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleI18nDemo {

    public static void main(String[] args) {
        Locale us = Locale.US;
        Locale de = Locale.GERMANY;

        NumberFormat usMoney = NumberFormat.getCurrencyInstance(us);
        NumberFormat deNumber = NumberFormat.getNumberInstance(de);
        System.out.println("usPrice=" + usMoney.format(1999.50));
        System.out.println("deNumber=" + deNumber.format(1999.50));
        System.out.println("deCurrency=" + Currency.getInstance(de).getCurrencyCode());

        Currency usd = Currency.getInstance(us);
        System.out.println("usdCode=" + usd.getCurrencyCode());

        ResourceBundle bundle = ResourceBundle.getBundle(
                "messages", Locale.forLanguageTag("en"));
        System.out.println("greeting=" + bundle.getString("greeting"));
        System.out.println("farewell=" + bundle.getString("farewell"));
    }
}
