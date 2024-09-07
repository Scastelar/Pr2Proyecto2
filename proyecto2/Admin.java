package proyecto2;

public class Admin extends User {

    public Admin() {
        super("admin", "123");  // Contraseña predeterminada para admin
    }

    public void createUser(String username, String password) {
        User newUser = new User(username, password);
        newUser.createUserDirectories();
        System.out.println("Usuario creado: " + username);
    }
    
    
}
