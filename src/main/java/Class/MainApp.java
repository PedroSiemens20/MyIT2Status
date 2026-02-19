package Class;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainApp {
    private LogGui gui;

    public static void main(String[] args) {


        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                MainApp app = new MainApp();
                app.gui = new LogGui();
                app.gui.setVisible(true);
                new Thread(app::executeProcess).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void executeProcess() {
        try {
            gui.addLog("System started. Waiting for file selection...");

            Path sourcePath = pickFile("Select Incident Excel (Source)");
            if (sourcePath == null) { exitApp("Selection cancelled.", false); return; }

            Path snapPath = pickFile("Select Current Snapshot (Snapshot)");
            if (snapPath == null) { exitApp("Selection cancelled.", false); return; }

            gui.setProcessing(true);
            ExcelReader reader = new ExcelReader();
            IncidentMapper mapper = new IncidentMapper();

            // 1. Load Snapshot
            gui.addLog("Reading existing Snapshot data...");
            Map<String, Incident> snapData = loadSnapshotData(snapPath.toString(), reader, mapper);

            // 2. Read Source
            gui.addLog("Reading Source data...");
            List<Map<String, Object>> sourceRows = reader.readAnySheet(sourcePath.toString(), MasterData.DEFAULT_INCIDENT_SHEET);

            List<Incident> migrated = new ArrayList<>();
            List<Incident> newEntries = new ArrayList<>();

            gui.addLog("Comparing records...");
            for (Map<String, Object> row : sourceRows) {
                Incident currentInc = mapper.mapFromSource(row);
                if (currentInc.number.isEmpty()) continue;

                if (snapData.containsKey(currentInc.number)) {
                    if (!isIdentical(currentInc, snapData.get(currentInc.number))) {
                        migrated.add(currentInc);
                    }
                } else {
                    newEntries.add(currentInc);
                }
            }

            // 3. Output logic
            Files.createDirectories(Paths.get(MasterData.OUTPUT_FOLDER));
            String outPath = Paths.get(MasterData.OUTPUT_FOLDER, MasterData.DRAFT_FILENAME).toString();

            gui.addLog("Generating file...");
            boolean success = new ExcelWriter().writeMigratedSnapshot(outPath, migrated, newEntries, gui);

            if (success) {
                gui.setProcessing(false);
                gui.addLog("SUCCESS! Updated: " + migrated.size() + " | New: " + newEntries.size());
                openExcel(outPath);
                exitApp("Process finished. Closing in 5 seconds...", false);
            } else {
                exitApp("Failed to write file. Process aborted.", true);
            }

        } catch (Exception e) {
            gui.setProcessing(false);
            gui.addLog("CRITICAL ERROR: " + e.getMessage());
            exitApp("Exiting due to error...", true);
        }
    }

    private boolean isIdentical(Incident n, Incident s) {

        return Objects.equals(n.state, s.state) &&
                Objects.equals(ExcelUtils.canonical(n.priority), ExcelUtils.canonical(s.priority));
                //Objects.equals(ExcelUtils.canonical(n.applicationSpecificInfo), ExcelUtils.canonical(s.applicationSpecificInfo));



    }

    private Map<String, Incident> loadSnapshotData(String path, ExcelReader reader, IncidentMapper mapper) {
        Map<String, Incident> data = new HashMap<>();
        List<Map<String, Object>> rows = reader.readAnySheet(path, MasterData.STATUS_SHEET);
        for (Map<String, Object> r : rows) {
            Incident inc = mapper.mapFromStatus(r);
            if (!inc.number.isEmpty()) data.put(inc.number, inc);
        }
        return data;
    }

    private void openExcel(String path) {
        try {
            File file = new File(path);
            if (Desktop.isDesktopSupported()) {
                gui.addLog("Opening Excel automatically...");
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            gui.addLog("Could not open file automatically.");
        }
    }

    private void exitApp(String message, boolean isError) {
        gui.addLog(message);
        if (isError) gui.setProcessing(false);

        // Timer to close the app after 5 seconds
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(isError ? 1 : 0);
            }
        }, 5000);
    }

    private Path pickFile(String title) {
        FileDialog d = new FileDialog(gui, title, FileDialog.LOAD);
        d.setFile("*.xlsx;*.xls");
        d.setVisible(true);
        if (d.getFile() == null) return null;
        return Paths.get(d.getDirectory(), d.getFile());
    }
}