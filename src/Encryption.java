import java.util.Scanner;

public class Encryption {
    private String passkey = null;

    public String processCommand(String input) {
        // Split input into command and argument
        String[] parts = input.split(" ", 2);
        String command = parts[0].toUpperCase();
        String argument = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "PASS":
                return setPasskey(argument);
            case "ENCRYPT":
                return encrypt(argument);
            case "DECRYPT":
                return decrypt(argument);
            case "QUIT":
                System.exit(0);
                return null;
            default:
                return "ERROR Unknown command";
        }
    }

    private String setPasskey(String key) {
        // Validate key
        if (key == null || key.trim().isEmpty()) {
            return "ERROR Passkey cannot be empty";
        }

        // Check if passkey contains only uppercase letters
        for (char c : key.toCharArray()) {
            if (!Character.isUpperCase(c)) {
                return "ERROR Passkey must contain only uppercase letters";
            }
        }

        this.passkey = key;
        return "RESULT";
    }

    private String encrypt(String text) {
        // Check if passkey is set
        if (passkey == null) {
            return "ERROR No passkey set";
        }

        // Validate input
        for (char c : text.toCharArray()) {
            if (!Character.isUpperCase(c)) {
                return "ERROR Input must be uppercase letters";
            }
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char plainChar = text.charAt(i);
            char keyChar = passkey.charAt(i % passkey.length());

            // Convert to 0-25 range
            int plainValue = plainChar - 'A';
            int keyValue = keyChar - 'A';

            // Apply Vigenère cipher formula (addition modulo 26)
            int encryptedValue = (plainValue + keyValue) % 26;

            // Convert back to ASCII
            char encryptedChar = (char) (encryptedValue + 'A');
            result.append(encryptedChar);
        }

        return "RESULT " + result.toString();
    }

    private String decrypt(String text) {
        // Check if passkey is set
        if (passkey == null) {
            return "ERROR No passkey set";
        }

        // Validate input
        for (char c : text.toCharArray()) {
            if (!Character.isUpperCase(c)) {
                return "ERROR Input must be uppercase letters";
            }
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char encryptedChar = text.charAt(i);
            char keyChar = passkey.charAt(i % passkey.length());

            // Convert to 0-25 range
            int encryptedValue = encryptedChar - 'A';
            int keyValue = keyChar - 'A';

            // Apply Vigenère cipher formula (subtraction modulo 26)
            int plainValue = (encryptedValue - keyValue + 26) % 26;

            // Convert back to ASCII
            char plainChar = (char) (plainValue + 'A');
            result.append(plainChar);
        }

        return "RESULT " + result.toString();
    }

    public static void main(String[] args) {
        Encryption encryption = new Encryption();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String response = encryption.processCommand(line);

            if (response != null) {
                System.out.println(response);
            }
        }

        scanner.close();
    }
}
