package Class;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Comparator; // Adicione este import

public class ExcelWriter {

    public boolean writeMigratedSnapshot(String path, List<Incident> migrated, List<Incident> newEntries, LogGui gui) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            createSheet(wb, "Migrated Data", migrated);
            createSheet(wb, "New Entries", newEntries);

            try (FileOutputStream fos = new FileOutputStream(path)) {
                wb.write(fos);
                return true;
            } catch (FileNotFoundException e) {
                gui.addLog("ERROR: The file is open in another program!");
                gui.addLog("Please close '" + path + "' and try again.");
                return false;
            }
        } catch (Exception e) {
            gui.addLog("Excel Error: " + e.getMessage());
            return false;
        }
    }

    private void createSheet(XSSFWorkbook wb, String name, List<Incident> data) {
        // 1. Ordenar os dados antes de escrever (Do mais antigo para o mais novo)
        data.sort(Comparator.comparing(inc -> {
            Date d = ExcelUtils.extractDate(inc.opened);
            return d != null ? d : new Date(0); // Se a data for nula, joga para o topo (muito antigo)
        }));

        XSSFSheet sheet = wb.createSheet(name);
        writeHeader(wb, sheet);

        CellStyle dateStyle = wb.createCellStyle();
        dateStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat(MasterData.DATE_PATTERN_DISPLAY));

        int rowIdx = 1;
        for (Incident inc : data) {

            if (!ignoreNumbers().contains(inc.number)) {
                Row row = sheet.createRow(rowIdx++);

                // O resto do seu código de escrita continua igual...
                writeCell(row, 1, inc.number, null);
                writeCell(row, 8, inc.openedBy, null);
                writeCell(row, 9, inc.shortDescription, null);
                writeCell(row, 11, inc.applicationSpecificInfo, null);
                writeCell(row, 12, inc.opened, dateStyle);
                writeCell(row, 13, inc.priority, null);
                writeCell(row, 14, inc.state, null);
                writeCell(row, 17, inc.description, null);

                if (inc.applicationSpecificInfo.equals(MasterData.NEW_ARE_SRE))
                    writeCell(row, 18, "x", null);

                if (inc.applicationSpecificInfo.equals(MasterData.NEW_ARE_BUZ))
                    writeCell(row, 19, "x", null);
            }


        }
    }

    public List<String> ignoreNumbers() {
        return List.of(
                "INC44557114",
                "INC44542910",
                "INC44514794",
                "INC44497998",
                "INC44475191",
                "INC44301197",
                "INC44247133",
                "INC44233598",
                "INC43708822",
                "INC44567943",
                "INC44599000",
                "INC44607997",
                "INC44537940",
                "INC44556908"
        );
    }


    private void writeHeader(XSSFWorkbook wb, Sheet sheet) {
        Row row = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);

        for (int i = 0; i < MasterData.STATUS_HEADERS.length; i++) {
            Cell c = row.createCell(i);
            c.setCellValue(MasterData.STATUS_HEADERS[i]);
            c.setCellStyle(style);
        }
    }

    private void writeCell(Row row, int col, Object val, CellStyle s) {
        if (val == null || val.toString().isEmpty()) return;
        Cell c = row.createCell(col);
        if (val instanceof Date) {
            c.setCellValue((Date) val);
            if (s != null) c.setCellStyle(s);
        } else {
            c.setCellValue(val.toString());
        }
    }
}