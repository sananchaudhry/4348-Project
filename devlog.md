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