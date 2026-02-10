package Class;

import Class.MasterData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.util.*;

public class ExcelReader {
    public List<Map<String, Object>> readAnySheet(String filePath, String sheetName) {
        List<Map<String, Object>> rows = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return rows;

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return rows;

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) headers.add(cell.getStringCellValue().trim());

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;
                Map<String, Object> rowData = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    rowData.put(headers.get(j), getCellValue(row.getCell(j), evaluator));
                }
                rows.add(rowData);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return rows;
    }

    private Object getCellValue(Cell cell, FormulaEvaluator eval) {
        if (cell == null) return null;
        CellType type = (cell.getCellType() == CellType.FORMULA && eval != null) ? eval.evaluateFormulaCell(cell) : cell.getCellType();
        if (type == CellType.NUMERIC) return DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue() : cell.getNumericCellValue();
        if (type == CellType.BOOLEAN) return cell.getBooleanCellValue();
        return cell.toString().trim();
    }

    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }
}