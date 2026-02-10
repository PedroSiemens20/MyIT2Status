package Class;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogGui extends JFrame {
    private JTextArea logArea;
    private JProgressBar progressBar;

    public LogGui() {
        setTitle("MyIT Status Tool - Execution Logs");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(20, 20, 20)); // Matrix Dark Background
        logArea.setForeground(new Color(50, 255, 150)); // Matrix Green Text
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        logArea.setMargin(new Insets(10, 10, 10, 10));

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(0, 15));
        progressBar.setBackground(new Color(30, 30, 30));
        progressBar.setForeground(new Color(50, 255, 150));
        progressBar.setBorderPainted(false);

        setLayout(new BorderLayout());
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);

        addLog("System initialized.");
    }

    public void addLog(String message) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + time + "] " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public void setProcessing(boolean active) {
        SwingUtilities.invokeLater(() -> progressBar.setVisible(active));
    }
}