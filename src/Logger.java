import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Logger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private String logFileName;
    
    public Logger(String logFileName) {
        this.logFileName = logFileName;
    }
    
    public void log(String message) {
        try (FileWriter writer = new FileWriter(new File(logFileName), true)) {
            String[] parts = message.split(" ", 2);
            String action = parts[0];
            String content = parts.length > 1 ? parts[1] : "";
            
            LocalDateTime now = LocalDateTime.now();
            String timestamp = now.format(formatter);
            
            String logEntry = timestamp + " [" + action + "] " + content + "\n";
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Logger <log-file-name>");
            System.exit(1);
        }
        
        String logFileName = args[0];
        Logger logger = new Logger(logFileName);
        
        Scanner scanner = new Scanner(System.in);
        String line;
        
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            
            if (line.equals("QUIT")) {
                break;
            }
            
            logger.log(line);
        }
        
        scanner.close();
    }
}
