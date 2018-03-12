# Visitor-Pattern

This project implements a visitor pattern to validate State and Singleton pattern.

For singleton patter it check that a given class, with a given name has a:
1. Private constructor
2. Public static method that returns an instance of the class type.
3. A private static instance variable, of the type of the class
4. Calls the constructor only once, from the public static method.

For State pattern it checks that, 
1. The CONTEXT has matching method names for each of methods in ABSTRACTSTATE.
2. Each of the CONTEXT methods calls the correct ABSTRACTSTATE method. Return an integer that matches the number of methods correctly matched.
