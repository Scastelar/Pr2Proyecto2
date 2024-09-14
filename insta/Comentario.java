/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package insta;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Comentario implements Serializable {
    private String username;
    private String texto;
    private Date fecha;

    public Comentario(String username, String texto) {
        this.username = username;
        this.texto = texto;
        this.fecha = Calendar.getInstance().getTime();
    }

    @Override
    public String toString() {
        return username + " escribió:\n" + "\"" + texto + "\"" + " el " + fecha;
    }
}