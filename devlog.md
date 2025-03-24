March 23 7:00 PM

Initial thoughts for each program

Logger: 
The purpose is to maintain a comprehensive log of all system activities with time stamp formatting. Overall this seems like a simple program. 
One function to log the entered message to the log file and another to process the messages and handle any errors.  

Encrypter: 
This program implements a vigenere cypher for encryption. Key requirements are as follows: 
Case-insensitive operation |
Only work with alphabetic characters |
Maintain current passkey between operations |
Some things to consider are having error messages be more specific than usual and how to handle obsure edge cases. 

Driver: 
The driver is the User interface of the entire system. Main development point making the different commands 
(Password,
Encrypt,
Decrypt,
Show History,
Quit) 
work with the other programs. 

March 24, 3:00 PM
First full implementation done. Will start testing with cases. Here are some thoughts I had for each program. 

Logger.java: 
Was indeed pretty simple, just the two functions I had outlined yesterday that needed to be implemented. 

Encryption.java
Learning to apply the vigenere cypher function and using ascii codes for alphabetical letters was a little tedious but 
I belive I managed to make it work. Will have to test along with Driver to make sure there isnt any weird bugs.

Driver.java:
Not too difficult just tedious making sure everything aligned with the other programs and functions. Figuring out that I can 
Just use java.lang.character was also helpful with the containsNonLetter function. 

March 24, 3:15 PM

Time to start testing my implementation. I already have some test cases laid out along with the one mentioned in the project description
Hopefully this will be the last hard session before submitting. 

Test case provided in project description: 
Input:   ENCRYPT HELLO
Output: ERROR Password not set
• Input: PASSKEY HELLO
Output: RESULT
• Input: ENCRYPT HELLO
Output: RESULT OIWWC

For encryption testing, test with passkey and without passkey and make sure output is correct. There should be valid error messages
for non-alphabetical characters

For log testing, correct timestamp should be there and all characters including special ones should be logged. Should quit.

March 24, 5:40 PM

Initial round of testing is done, and I made a bunch of changes to Encryption.java. Here is a summary of the changes:
1. Added a centralized processCommand() method that handles all command routing and processing
2. Simplified error handling to strictly output "RESULT" or "ERROR" responses
3. Removed multiple public methods in favor of a single method that processes all commands
4. Simplified main() method to directly use processCommand()

March 24 6:45 PM
Further testing is complete and changes to driver program have been made:
Input validation:
Only allows alphabetic characters
Converts input to uppercase
Prevents non-letter inputs

Inter-process communication:
Uses ProcessBuilder for creating processes
Uses BufferedReader/Writer for communication

Logging:
Logs all commands and results
Does not log passwords

History management:
Allows selecting from history
Option to enter new strings
Stores both input and output strings
