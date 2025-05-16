package bruce.rra_vms.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class CurrencyFormatter {
    private static final NumberFormat formatter;

    static {
        formatter = NumberFormat.getCurrencyInstance();
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
    }

    public static String format(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }

        return formatter.format(amount);
    }
}
