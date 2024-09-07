package proyecto2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserManager {

    private Admin admin;
    private User currentUser;

    public UserManager() {
        admin = new Admin();
        admin.createUserDirectories();  // Crear directorio del admin
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean login(String username, String password) {
        if (username.equals(admin.getUsername())) {
            if (admin.authenticate(password)) {
                currentUser = admin;  // Establecer el usuario actual como admin
                return true;
            }
        } else {
            File userDir = new File("Z/Users/" + username);
            if (userDir.exists() && userDir.isDirectory()) {
                File passwordFile = new File(userDir, "password.txt");
                if (passwordFile.exists()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(passwordFile))) {
                        String storedPassword = br.readLine();
                        if (password.equals(storedPassword)) {
                            currentUser = new User(username, password);  // Crear una instancia del usuario actual
                            return true;
                        }
                    } catch (FileNotFoundException e) {
                        System.err.println("File not found: " + e.getMessage());
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Password file not found for user: " + username);
                }
            } else {
                System.err.println("User directory not found or not a directory: " + username);
            }
        }
        return false;
    }

    public void createUser(String username, String password) {
        User newUser = new User(username, password);
        newUser.createUserDirectories();
        try (FileWriter writer = new FileWriter("Z/Users/" + username + "/password.txt")) {
            writer.write(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Usuario creado: " + username);
    }

    public boolean userExists(String username) {
        File userDir = new File("Z/Users/" + username);
        return userDir.exists();
    }

}
