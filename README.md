# Simple-Boggle
An Android Boggle Game developed in Kotlin, showcasing development concepts like ViewModels to manage fragment communication and simple UI Polish.

Boggle is a simple word game that involves forming as many words as you can from a grid of letters. This is a modified version with the following rules:
- Your first letter can start anywhere. 
- Subsequent letter connections must "touch" the previous letter. This can occur in any direction, including diagonally. 
- You may only use a letter in the current word once (no doubling back or reconnecting). 
- All words must utilize a minimum of two vowels. 
- You cannot generate the same word more than once, even if it’s from different letters. 
- Words must be at least 4 chars long. 
