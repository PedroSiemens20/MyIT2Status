package Class;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExcelUtils {
    private static final SimpleDateFormat SDF = new SimpleDateFormat(MasterData.DATE_PATTERN_DISPLAY);

    public static String canonical(Object v) {
        if (v == null) return "";
        if (v instanceof String) return ((String) v).trim();
        if (v instanceof Number) {
            double d = ((Number) v).doubleValue();
            if (Math.rint(d) == d) return String.format(Locale.ROOT, "%.0f", d);
            return java.math.BigDecimal.valueOf(d).stripTrailingZeros().toPlainString();
        }
        if (v instanceof Date) return SDF.format((Date) v);
        return v.toString();
    }

    public static Date extractDate(Object value) {
        if (value == null) return null;
        if (value instanceof Date) return (Date) value;

        try {
            String str = value.toString().trim();
            // AJUSTE: Se o utilizador usou pontos (11.12.2025) ou traços, mudamos para barra
            // apenas para o Java conseguir ler a data e filtrar corretamente.
            str = str.replace(".", "/").replace("-", "/");

            // Tenta ler formatos dd/MM/yyyy ou dd/MM/yy
            SimpleDateFormat tempSdf = new SimpleDateFormat("dd/MM/yyyy");
            if (str.length() <= 8) tempSdf = new SimpleDateFormat("dd/MM/yy");

            return tempSdf.parse(str);
        } catch (Exception e) {
            return null; // Se não for data de jeito nenhum, ignora o filtro mas não quebra
        }
    }
}