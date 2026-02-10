package Class;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

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
        XSSFSheet sheet = wb.createSheet(name);
        writeHeader(wb, sheet);

        CellStyle dateStyle = wb.createCellStyle();
        dateStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat(MasterData.DATE_PATTERN_DISPLAY));

        int rowIdx = 1;
        for (Incident inc : data) {
            Row row = sheet.createRow(rowIdx++);
            // Mapping based on MasterData.STATUS_HEADERS order
            writeCell(row, 1, inc.number, null);              // Incident (ID)
            writeCell(row, 8, inc.openedBy, null);            // Reported By
            writeCell(row, 9, inc.shortDescription, null);    // Description
            writeCell(row, 11, inc.applicationSpecificInfo, null); // ARE
            writeCell(row, 12, inc.opened, dateStyle);        // Created
            writeCell(row, 13, inc.priority, null);           // Priority
            writeCell(row, 14, inc.state, null);              // Status
            writeCell(row, 16, inc.description, null);        // Comments
        }
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