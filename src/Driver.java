import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {
    private Process loggerProcess;
    private Process encryptionProcess;
    private PrintWriter loggerInput;
    private PrintWriter encryptionInput;
    private Scanner encryptionOutput;
    private List<String> history = new ArrayList<>();
    
    public Driver(String logFileName) throws IOException {
        // Start logger process
        loggerProcess = Runtime.getRuntime().exec("java Logger " + logFileName);
        loggerInput = new PrintWriter(loggerProcess.getOutputStream(), true);
        
        // Start encryption process
        encryptionProcess = Runtime.getRuntime().exec("java Encryption");
        encryptionInput = new PrintWriter(encryptionProcess.getOutputStream(), true);
        encryptionOutput = new Scanner(encryptionProcess.getInputStream());
        
        // Log start of driver program
        loggerInput.println("START Driver program started");
    }
    
    public void close() {
        // Log end of driver program
        loggerInput.println("END Driver program ended");
        
        // Send QUIT command to both processes
        loggerInput.println("QUIT");
        encryptionInput.println("QUIT");
        
        // Close resources
        loggerInput.close();
        encryptionInput.close();
        encryptionOutput.close();
    }
    
    public void showMenu() {
        System.out.println("\n===== Encryption Program =====");
        System.out.println("1. Set Password");
        System.out.println("2. Encrypt");
        System.out.println("3. Decrypt");
        System.out.println("4. Show History");
        System.out.println("5. Quit");
        System.out.print("Enter your choice: ");
    }
    
    public void showHistory() {
        if (history.isEmpty()) {
            System.out.println("History is empty.");
            return;
        }
        
        System.out.println("\n===== History =====");
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
    }
    
    public String getStringFromUserOrHistory(Scanner scanner, String prompt) {
        if (history.isEmpty()) {
            System.out.print(prompt);
            return scanner.nextLine();
        }
        
        System.out.println("1. Enter a new string");
        System.out.println("2. Use a string from history");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());
        
        if (choice == 1) {
            System.out.print(prompt);
            return scanner.nextLine();
        } else if (choice == 2) {
            showHistory();
            System.out.print("Enter the number of the string to use (0 to cancel): ");
            int historyIndex = Integer.parseInt(scanner.nextLine());
            
            if (historyIndex <= 0 || historyIndex > history.size()) {
                return null;
            }
            
            return history.get(historyIndex - 1);
        }
        
        return null;
    }
    
    public void setPassword(Scanner scanner) {
        String password = getStringFromUserOrHistory(scanner, "Enter password: ");
        if (password == null) {
            return;
        }
        
        // Check if password contains only letters
        boolean containsNonLetter = false;
        for (char c : password.toCharArray()) {
            if (!Character.isLetter(c)) {
                containsNonLetter = true;
                break;
            }
        }
        
        if (containsNonLetter) {
            System.out.println("Error: Password must contain only letters.");
            return;
        }
        
        // Send command to encryption program
        encryptionInput.println("PASS " + password);
        
        // Get response from encryption program
        String response = encryptionOutput.nextLine();
        
        // Log command (without revealing the password)
        loggerInput.println("COMMAND password");
        
        // Check response
        String[] responseParts = response.split(" ", 2);
        String responseType = responseParts[0];
        String responseMessage = responseParts.length > 1 ? responseParts[1] : "";
        
        if (responseType.equals("ERROR")) {
            System.out.println("Error: " + responseMessage);
            loggerInput.println("ERROR " + responseMessage);
        } else {
            System.out.println("Password set successfully.");
            loggerInput.println("RESULT Password set successfully");
        }
    }
    
    public void encrypt(Scanner scanner) {
        String text = getStringFromUserOrHistory(scanner, "Enter text to encrypt: ");
        if (text == null) {
            return;
        }
        
        // Check if text contains only letters
        boolean containsNonLetter = false;
        for (char c : text.toCharArray()) {
            if (!Character.isLetter(c)) {
                containsNonLetter = true;
                break;
            }
        }
        
        if (containsNonLetter) {
            System.out.println("Error: Text must contain only letters.");
            loggerInput.println("ERROR Text must contain only letters");
            return;
        }
        
        // Add to history if it's a new entry
        if (!history.contains(text)) {
            history.add(text);
        }
        
        // Send command to encryption program
        encryptionInput.println("ENCRYPT " + text);
        
        // Get response from encryption program
        String response = encryptionOutput.nextLine();
        
        // Log command
        loggerInput.println("COMMAND encrypt " + text);
        
        // Check response
        String[] responseParts = response.split(" ", 2);
        String responseType = responseParts[0];
        String responseMessage = responseParts.length > 1 ? responseParts[1] : "";
        
        if (responseType.equals("ERROR")) {
            System.out.println("Error: " + responseMessage);
            loggerInput.println("ERROR " + responseMessage);
        } else {
            System.out.println("Encrypted result: " + responseMessage);
            loggerInput.println("RESULT " + responseMessage);
            
            // Add result to history
            if (!history.contains(responseMessage)) {
                history.add(responseMessage);
            }
        }
    }
    
    public void decrypt(Scanner scanner) {
        String text = getStringFromUserOrHistory(scanner, "Enter text to decrypt: ");
        if (text == null) {
            return;
        }
        
        // Check if text contains only letters
        boolean containsNonLetter = false;
        for (char c : text.toCharArray()) {
            if (!Character.isLetter(c)) {
                containsNonLetter = true;
                break;
            }
        }
        
        if (containsNonLetter) {
            System.out.println("Error: Text must contain only letters.");
            loggerInput.println("ERROR Text must contain only letters");
            return;
        }
        
        // Add to history if it's a new entry
        if (!history.contains(text)) {
            history.add(text);
        }
        
        // Send command to encryption program
        encryptionInput.println("DECRYPT " + text);
        
        // Get response from encryption program
        String response = encryptionOutput.nextLine();
        
        // Log command
        loggerInput.println("COMMAND decrypt " + text);
        
        // Check response
        String[] responseParts = response.split(" ", 2);
        String responseType = responseParts[0];
        String responseMessage = responseParts.length > 1 ? responseParts[1] : "";
        
        if (responseType.equals("ERROR")) {
            System.out.println("Error: " + responseMessage);
            loggerInput.println("ERROR " + responseMessage);
        } else {
            System.out.println("Decrypted result: " + responseMessage);
            loggerInput.println("RESULT " + responseMessage);
            
            // Add result to history
            if (!history.contains(responseMessage)) {
                history.add(responseMessage);
            }
        }
    }
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Driver <log-file-name>");
            System.exit(1);
        }
        
        String logFileName = args[0];
        
        try {
            Driver driver = new Driver(logFileName);
            Scanner scanner = new Scanner(System.in);
            
            while (true) {
                driver.showMenu();
                int choice;
                
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Please enter a number.");
                    continue;
                }
                
                switch (choice) {
                    case 1:
                        driver.setPassword(scanner);
                        break;
                    case 2:
                        driver.encrypt(scanner);
                        break;
                    case 3:
                        driver.decrypt(scanner);
                        break;
                    case 4:
                        driver.showHistory();
                        break;
                    case 5:
                        driver.close();
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting processes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
