import java.util.Scanner;

public class Encryption {
    private String passkey = null;
    
    public String setPasskey(String key) {
        if (key == null || key.trim().isEmpty()) {
            return "ERROR Passkey cannot be empty";
        }
        
        // Check if passkey contains only letters
        for (char c : key.toCharArray()) {
            if (!Character.isLetter(c)) {
                return "ERROR Passkey must contain only letters";
            }
        }
        
        this.passkey = key.toUpperCase();
        return "RESULT";
    }
    
    public String encrypt(String text) {
        if (passkey == null) {
            return "ERROR Password not set";
        }
        
        // Check if text contains only letters
        for (char c : text.toCharArray()) {
            if (!Character.isLetter(c)) {
                return "ERROR Input must contain only letters";
            }
        }
        
        String upperText = text.toUpperCase();
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < upperText.length(); i++) {
            char plainChar = upperText.charAt(i);
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
    
    public String decrypt(String text) {
        if (passkey == null) {
            return "ERROR Password not set";
        }
        
        // Check if text contains only letters
        for (char c : text.toCharArray()) {
            if (!Character.isLetter(c)) {
                return "ERROR Input must contain only letters";
            }
        }
        
        String upperText = text.toUpperCase();
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < upperText.length(); i++) {
            char encryptedChar = upperText.charAt(i);
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
        
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] parts = line.split(" ", 2);
            String command = parts[0];
            String argument = parts.length > 1 ? parts[1] : "";
            
            String response;
            switch (command.toUpperCase()) {
                case "PASS":
                    response = encryption.setPasskey(argument);
                    break;
                case "ENCRYPT":
                    response = encryption.encrypt(argument);
                    break;
                case "DECRYPT":
                    response = encryption.decrypt(argument);
                    break;
                case "QUIT":
                    scanner.close();
                    return;
                default:
                    response = "ERROR Unknown command";
                    break;
            }
            
            System.out.println(response);
        }
        
        scanner.close();
    }
}
