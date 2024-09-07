
package proyecto2;

import java.io.File;

public class User {

    protected String username;
    protected String password;
    private File userDir;
    File Docs;
    File Music;
    File Imgs;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public String getUsername() {
        return username;
    }

    public void createUserDirectories() {
    this.userDir = new File("Z/Users/" , username);
    try {
        if (!userDir.exists()) {
            userDir.mkdirs();
            Docs = new File(userDir, "Mis Documentos");
            Music = new File(userDir, "Musica");
            Imgs = new File(userDir, "Mis Imagenes");
            Docs.mkdir();
            Music.mkdir();
            Imgs.mkdir();
        }
    } catch (SecurityException e) {
        System.err.println("Error al crear directorios: " + e.getMessage());
        e.printStackTrace();
    }
}

    
    public File getUserDir(){
        return userDir;
    }
    
    public File[] getFolders() {
        return new File[]{Docs, Music, Imgs};
    }
}
