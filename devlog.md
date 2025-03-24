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