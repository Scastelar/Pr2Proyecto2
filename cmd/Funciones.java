package cmd;

import java.io.*;

public class Funciones {

    public File dirActual;

    public Funciones(String startPath) {
        dirActual = new File(startPath);
    }

    public String getCurrentPath() {
        try {
            return dirActual.getCanonicalPath();
        } catch (Exception e) {
            System.out.println("Un error ha ocurrido intentando obtener el canonicalPath");
            return dirActual.getAbsolutePath();
        }
    }

    public String mkdir(String dir) {
        String mensaje = "";
        File folder = new File(dir);

        if (folder.exists()) {
            mensaje = "Error: Esta carpeta ya existe";
            return mensaje;
        } else {
            mensaje = "Carpeta creada existosamente";
            folder.mkdir();
            return mensaje;
        }
    }

    public String mfile(String dir) {
        String mensaje = "";
        File archivo = new File(dir);

        if (archivo.exists()) {
            mensaje = "Error: Este archivo ya existe";
            return mensaje;
        } else {
            mensaje = "Archivo creado existosamente";

            try {
                archivo.createNewFile();
            } catch (IOException e) {
                mensaje = "Error: no se pudo crear";
            }
            return mensaje;

        }
    }

    public void VaciarCarpeta(File vaciar) {
        if (vaciar.isDirectory()) {
            for (File vacio : vaciar.listFiles()) {
                vacio.delete();
            }
        }
    }

    public String Eliminar(File elemento) {
        if (elemento.isDirectory()) {
            for (File carpeta : elemento.listFiles()) {
                if (carpeta.isDirectory()) {
                    VaciarCarpeta(carpeta);
                    carpeta.delete();
                } else {
                    carpeta.delete();
                }
            }
            elemento.delete();
            return "Carpeta eliminada";
        }

        if (elemento.isFile()) {
            elemento.delete();
            return "Archivo eliminado";
        }

        return "Error";
    }

    public String cd(String dir) {
        if (dir.charAt(0) != '/') {
            File newDir = new File(dirActual.getAbsolutePath() + "/" + dir);
            if (!newDir.isDirectory()) {
                return "Error. La direccion tiene que ser una carpeta.";
            }
            dirActual = newDir;
            System.out.println("NUEVO DIR ES: " + newDir.getAbsolutePath());
            return "";
        }
        dirActual = new File(dir);
        return "";
    }

    public static String listarFiles(String dir) {
        String mensaje;
        String lista = "";
        File listado = new File(dir);

        if (listado.isDirectory()) {

            for (File archivo : listado.listFiles()) {
                lista += "\n ->" + archivo.getName();
            }

            return lista;
        } else {
            return "Error: debe seleccionar un directorio";
        }
    }

    public String escribir(String men, String dir) {
        File wr = new File(dir);
        String salida = "";

        if (wr.exists()) {
            if (wr.isFile()) {
                try {
                    FileWriter fr = new FileWriter(wr);
                    fr.write(men);
                    fr.flush();
                } catch (IOException e) {
                    salida = "Error: no se pudo crear";
                }
                salida = "Escritura completada";
                return salida;
            } else {
                salida = "Error: debe seleccionar un archivo";
                return salida;
            }
        } else {
            salida = "Error: Archivo inexistente";
            return salida;
        }
    }

    public static String leer(String dir) {
        File elemento = new File(dir);
        String mensaje = "";

        if (elemento.exists()) {
            if (elemento.isFile()) {
                try {
                    FileReader fr = new FileReader(elemento);
                    String contenido = "";
                    for (int i = fr.read(); i != -1; i = fr.read()) {
                        contenido += (char) i;
                    }
                    return contenido;
                } catch (IOException e) {
                    mensaje = "Error: no se pudo leer";
                    return mensaje;
                }
            } else {
                mensaje = "Error: debe seleccionar un archivo";
                return mensaje;
            }
        } else {
            mensaje = "Error: Archivo inexistente";
            return mensaje;
        }
    }
}
