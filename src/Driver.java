import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {
    private Process loggerProcess;
    private Process encryptionProcess;
    private BufferedReader encryptionReader;
    private BufferedWriter encryptionWriter;
    private BufferedReader loggerReader;
    private BufferedWriter loggerWriter;
    private List<String> history = new ArrayList<>();
    private Scanner userInput;

    public Driver(String logFileName) throws IOException {
        // Start logger process
        ProcessBuilder loggerBuilder = new ProcessBuilder("java", "Logger", logFileName);
        loggerProcess = loggerBuilder.start();
        loggerWriter = new BufferedWriter(new OutputStreamWriter(loggerProcess.getOutputStream()));
        loggerReader = new BufferedReader(new InputStreamReader(loggerProcess.getInputStream()));

        // Start encryption process
        ProcessBuilder encryptionBuilder = new ProcessBuilder("java", "Encryption");
        encryptionProcess = encryptionBuilder.start();
        encryptionWriter = new BufferedWriter(new OutputStreamWriter(encryptionProcess.getOutputStream()));
        encryptionReader = new BufferedReader(new InputStreamReader(encryptionProcess.getInputStream()));

        // Log start of driver program
        log("START Driver program started");

        userInput = new Scanner(System.in);
    }

    private void log(String message) throws IOException {
        loggerWriter.write(message);
        loggerWriter.newLine();
        loggerWriter.flush();
    }

    private String sendToEncryption(String command) throws IOException {
        encryptionWriter.write(command);
        encryptionWriter.newLine();
        encryptionWriter.flush();
        return encryptionReader.readLine();
    }

    private void close() throws IOException {
        // Log end of driver program
        log("END Driver program ended");

        // Send QUIT to both processes
        sendToEncryption("QUIT");
        loggerWriter.write("QUIT");
        loggerWriter.newLine();
        loggerWriter.flush();

        // Close resources
        encryptionWriter.close();
        encryptionReader.close();
        loggerWriter.close();
        loggerReader.close();
        userInput.close();
    }

    private void showMenu() {
        System.out.println("\n===== Encryption Program =====");
        System.out.println("1. Set Password");
        System.out.println("2. Encrypt");
        System.out.println("3. Decrypt");
        System.out.println("4. Show History");
        System.out.println("5. Quit");
        System.out.print("Enter your choice: ");
    }

    private void showHistory() {
        if (history.isEmpty()) {
            System.out.println("History is empty.");
            return;
        }

        System.out.println("\n===== History =====");
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
    }

    private String getInput(String prompt, boolean fromHistory) throws IOException {
        if (!fromHistory || history.isEmpty()) {
            System.out.print(prompt);
            String input = userInput.nextLine().toUpperCase();

            // Validate input contains only letters
            if (!input.matches("^[A-Z]+$")) {
                System.out.println("Error: Input must contain only letters.");
                log("ERROR Input must contain only letters");
                return null;
            }

            history.add(input);
            return input;
        }

        // History selection
        while (true) {
            showHistory();
            System.out.println("0. Enter a new string");
            System.out.print("Select a string from history (0-" + history.size() + "): ");

            try {
                int choice = Integer.parseInt(userInput.nextLine());

                if (choice == 0) {
                    System.out.print(prompt);
                    String input = userInput.nextLine().toUpperCase();

                    // Validate input contains only letters
                    if (!input.matches("^[A-Z]+$")) {
                        System.out.println("Error: Input must contain only letters.");
                        log("ERROR Input must contain only letters");
                        return null;
                    }

                    history.add(input);
                    return input;
                }

                if (choice < 1 || choice > history.size()) {
                    System.out.println("Invalid selection.");
                    continue;
                }

                return history.get(choice - 1);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void setPassword() throws IOException {
        // Log the password command without revealing the password
        log("COMMAND password");

        String password = getInput("Enter password: ", false);
        if (password == null) return;

        // Send password to encryption program
        String response = sendToEncryption("PASS " + password);

        // Process response
        if (response.startsWith("RESULT")) {
            System.out.println("Password set successfully.");
            log("RESULT Password set successfully");
        } else {
            System.out.println("Error setting password: " + response.substring(6));
            log("ERROR " + response.substring(6));
        }
    }

    private void encrypt() throws IOException {
        log("COMMAND encrypt");

        String text = getInput("Enter text to encrypt: ", true);
        if (text == null) return;

        // Send encrypt command to encryption program
        String response = sendToEncryption("ENCRYPT " + text);

        // Process response
        if (response.startsWith("RESULT")) {
            String encryptedText = response.substring(7);
            System.out.println("Encrypted text: " + encryptedText);
            log("RESULT " + encryptedText);
            history.add(encryptedText);
        } else {
            System.out.println("Encryption error: " + response.substring(6));
            log("ERROR " + response.substring(6));
        }
    }

    private void decrypt() throws IOException {
        log("COMMAND decrypt");

        String text = getInput("Enter text to decrypt: ", true);
        if (text == null) return;

        // Send decrypt command to encryption program
        String response = sendToEncryption("DECRYPT " + text);

        // Process response
        if (response.startsWith("RESULT")) {
            String decryptedText = response.substring(7);
            System.out.println("Decrypted text: " + decryptedText);
            log("RESULT " + decryptedText);
            history.add(decryptedText);
        } else {
            System.out.println("Decryption error: " + response.substring(6));
            log("ERROR " + response.substring(6));
        }
    }

    public void run() throws IOException {
        while (true) {
            showMenu();

            try {
                int choice = Integer.parseInt(userInput.nextLine());

                switch (choice) {
                    case 1:
                        setPassword();
                        break;
                    case 2:
                        encrypt();
                        break;
                    case 3:
                        decrypt();
                        break;
                    case 4:
                        showHistory();
                        break;
                    case 5:
                        close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Driver <log-file-name>");
            System.exit(1);
        }

        try {
            Driver driver = new Driver(args[0]);
            driver.run();
        } catch (IOException e) {
            System.err.println("Error starting processes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}