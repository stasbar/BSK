package com.milbar.logic.login;

import com.milbar.gui.abstracts.factories.LoggerFactory;
import com.milbar.logic.exceptions.*;
import com.milbar.logic.login.loaders.SerializedDataReader;
import com.milbar.logic.login.wrappers.SessionToken;
import com.milbar.logic.login.wrappers.UserCredentials;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsersManager {
    
    private final static Logger logger = LoggerFactory.getLogger(UsersManager.class);
    
    private SerializedDataReader<String, UserCredentials> usersCollection;
    private CredentialsManager credentialsManager = new CredentialsManager();
    private byte[] loginToken;
    
    UsersManager(Path pathToUsersData) {
        usersCollection = new SerializedDataReader<>(pathToUsersData);
        try {
            usersCollection.readFromFile();
        } catch (ReadingSerializedFileException e) {
            Map<String, UserCredentials> usersList = new HashMap<>();
            usersCollection.updateCollection(usersList);
            logger.log(Level.WARNING, "Failed to load users data from file. Creating a new, empty collection.");
        }
    }
    
    public boolean registerUser(String username, String password) throws UserAlreadyExists {
        if (usersCollection.keyExists(username))
            throw new UserAlreadyExists("User with name: " + username + " already exists.");
        
        byte[] salt = credentialsManager.getSalt();
        byte[] hashedPassword;
        try {
            hashedPassword = credentialsManager.getHash(password, salt);
        } catch (ImplementationError e) {
            e.printStackTrace();
            return false;
        }
        UserCredentials newUser = new UserCredentials(username, hashedPassword, salt);
        usersCollection.updateCollection(username, newUser);
        return true;
    }
    
    public boolean removeUser(String username, String password) throws UserDoesNotExist {
        UserCredentials usersCredentials = usersCollection.getValue(username);
        if (usersCredentials == null)
            throw new UserDoesNotExist("Failed to remove user, because there is not user with name: " + username);
        
        byte[] salt = usersCredentials.getSalt();
        byte[] hashedPassword = usersCredentials.getHashedPassword();
        if (credentialsManager.validatePassword(password, salt, hashedPassword)) {
            usersCollection.removeItem(username);
            return true;
        }
        else
            return false;
    }

    public SessionToken loginUser(String username, String password) throws UserDoesNotExist, UsersPasswordNotValid {
        UserCredentials userCredentials = usersCollection.getValue(username);
        if (usersCollection == null)
            throw new UserDoesNotExist("User with name: " + username + " does not exists.");
        
        byte[] salt = userCredentials.getSalt();
        byte[] hashedPassword = userCredentials.getHashedPassword();
        if (credentialsManager.validatePassword(password, salt, hashedPassword))
            return credentialsManager.getRandomSessionToken(username);
        else
            throw new UsersPasswordNotValid("Login operation for " + username + " failed. Given password is wrong.");
    }
}