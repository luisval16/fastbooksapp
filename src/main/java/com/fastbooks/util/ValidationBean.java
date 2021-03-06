/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fastbooks.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author dell
 */
@Stateless
public class ValidationBean {

    public boolean validarCampoVacio(String c, String tipoMsg, String tituloMsg, String descMsg) {
        boolean flag;
        if (c != null) {
            if ("".equals(c)) {
                flag = false;
                lanzarMensaje(tipoMsg, tituloMsg, descMsg);
            } else {
                flag = true;
            }
        } else {
            lanzarMensaje(tipoMsg, tituloMsg, descMsg);
            flag = false;
        }
        return flag;
    }
    
    public String formatDouble(BigDecimal num){
    Double number = Double.parseDouble(num.toString());
    return String.format("%.2f", number);
    }

    public boolean validarSoloLetras(String c, String tipoMsg, String tituloMsg, String descMsg) {
        Pattern patron = Pattern.compile("[^A-Za-z-ZñÑáéíóúÁÉÍÓÚ ]");
        Matcher validar = patron.matcher(c);
        boolean flag;
        if (validar.find()) {
            flag = false;
            lanzarMensaje(tipoMsg, tituloMsg, descMsg);
        } else {
            flag = true;
        }
        return flag;
    }

    public boolean validarURL(String c, String tipoMsg, String tituloMsg, String descMsg) {
        Pattern patron = Pattern.compile("\\\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        Matcher validar = patron.matcher(c);
        boolean flag;
        if (validar.find()) {
            flag = false;
            lanzarMensaje(tipoMsg, tituloMsg, descMsg);
        } else {
            flag = true;
        }
        return flag;
    }

    public boolean validarLongitudCampo(String c, int min, int max, String tipoMsg, String tituloMsg, String descMsg) {
        boolean flag;
        if (!(c != null && c.length() >= min && c.length() <= max)) {
            flag = false;
            lanzarMensaje(tipoMsg, tituloMsg, descMsg);
        } else {
            flag = true;
        }
        return flag;
    }

    public boolean validarEmail(String c, String tipoMsg, String tituloMsg, String descMsg) {
        Pattern patron = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher validar = patron.matcher(c);
        boolean flag;
        if (!validar.find()) {
            flag = false;
            lanzarMensaje(tipoMsg, tituloMsg, descMsg);
        } else {
            flag = true;
        }
        return flag;
    }

    public boolean validarSeleccion(String c, String tipoMsg, String tituloMsg, String descMsg) {
        boolean flag = true;
        if (c == null || c.equals("")) {
            flag = false;
            lanzarMensaje(tipoMsg, tituloMsg, descMsg);
        }
        return flag;
    }

    public boolean validarSoloNumeros(String c, String tipoMsg, String tituloMsg, String descMsg) {
        Pattern patron = Pattern.compile("[^0-9]");
        Matcher validar = patron.matcher(c);
        boolean flag;
        if (validar.find()) {
            flag = false;
            lanzarMensaje(tipoMsg, tituloMsg, descMsg);
        } else {
            flag = true;
        }
        return flag;
    }

    public boolean validarSoloNumerosConPunto(String c, String tipoMsg, String tituloMsg, String descMsg) {
        Pattern patron = Pattern.compile("^\\d+(?:\\.\\d{1,2})?$");
        Matcher validar = patron.matcher(c);
        boolean flag;
        if (validar.find()) {
            flag = true;

        } else {
            flag = false;
            lanzarMensaje(tipoMsg, tituloMsg, descMsg);
        }
        return flag;
    }

    public void ejecutarJavascript(String codigo) {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute(codigo);
    }

    public void lanzarMensaje(String tipo, String titulo, String msg) {
        FacesMessage.Severity typeMessage;
        switch (tipo.toLowerCase()) {
            case "info":
                typeMessage = FacesMessage.SEVERITY_INFO;
                break;
            case "warn":
                typeMessage = FacesMessage.SEVERITY_WARN;
                break;
            case "fatal":
                typeMessage = FacesMessage.SEVERITY_FATAL;
                break;
            case "error":
                typeMessage = FacesMessage.SEVERITY_ERROR;
                break;
            default:
                typeMessage = FacesMessage.SEVERITY_INFO;
                break;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(typeMessage, getMsgBundle(titulo), getMsgBundle(msg)));
    }
    
     public void lanzarMensajeSinBundle(String tipo, String titulo, String msg) {
        FacesMessage.Severity typeMessage;
        switch (tipo.toLowerCase()) {
            case "info":
                typeMessage = FacesMessage.SEVERITY_INFO;
                break;
            case "warn":
                typeMessage = FacesMessage.SEVERITY_WARN;
                break;
            case "fatal":
                typeMessage = FacesMessage.SEVERITY_FATAL;
                break;
            case "error":
                typeMessage = FacesMessage.SEVERITY_ERROR;
                break;
            default:
                typeMessage = FacesMessage.SEVERITY_INFO;
                break;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(typeMessage, titulo, msg));
    }

    public String getMsgBundle(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("/com/fastbooks/messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String value = bundle.getString(key);
        return value;
    }

     public String encriptar(String texto, String keymod) {

        String secretKey = keymod; //llave para encriptar datos
        String base64EncryptedString = "";

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = texto.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            base64EncryptedString = new String(base64Bytes);

        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }

    public String Desencriptar(String textoEncriptado) throws Exception {

        String secretKey = "fastbooks20"; //llave para encriptar datos
        String base64EncryptedString = "";

        try {
            byte[] message = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");

            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = decipher.doFinal(message);

            base64EncryptedString = new String(plainText, "UTF-8");

        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }
    public String obtenerFechaActual() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
        return dateFormat.format(date);
    }

    public String formatearFechaGuion(String fecha) {
        String tmp;
        String[] cad = fecha.split("-");
        tmp = cad[2] + "/" + cad[1] + "/" + cad[0];
        return tmp;
    }

    public String cambiarFormatoFecha(String fecha) {
        String tmp;
        String[] cad = (String[]) (fecha.contains("-") ? fecha.split("-") : (fecha.contains("/") ? fecha.split("/") : ""));
        tmp = cad[2] + "-" + cad[1] + "-" + cad[0];
        return tmp;
    }

    public boolean validarFormatoFecha(String c, String tipoMsg, String tituloMsg, String descMsg) {
        Pattern patron = Pattern.compile("/^([0][1-9]|[12][0-9]|3[01])(\\/|-)([0][1-9]|[1][0-2])\\2(\\d{4})$/");
        Matcher validar = patron.matcher(c);
        //
        //String c, String tipoMsg, String tituloMsg, String descMsg
        boolean flag;
        if (validar.find()) {
            flag = true;

        } else {
            flag = false;
            lanzarMensaje(tipoMsg, tituloMsg, descMsg);
        }
        return flag;
    }

    public boolean validarFecha(String fecha, String tipo, String titulo, String msg) {

        boolean flag = true;
        if (fecha != null && fecha.length() == 10) {
            String[] ftemp = (String[]) (fecha.contains("-") ? fecha.split("-") : (fecha.contains("/") ? fecha.split("/") : ""));
            Integer dias = new Integer(ftemp[0]);
            Integer mes = new Integer(ftemp[1]);
            Integer anio = new Integer(ftemp[2]);
            if (anio >= 1900 && anio <= 2100) {
                if (mes >= 1 && mes <= 12) {
                    if (mes == 2) {
                        if (!(dias >= 1 && dias <= 28)) {
                            flag = false;
                        }
                    } else if (!(dias >= 1 && dias <= 31)) {
                        flag = false;
                    }
                } else {
                    flag = false;
                }
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }
        if (!flag) {
            lanzarMensaje(tipo, titulo, msg);
        }
        return flag;
    }

    public String formatearFecha(String fecha) {
        try {
            System.err.println("Fecha al formatear fecha:    " + fecha);
            //String dateStr = "Mon Jun 18 00:00:00 IST 2012";
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.US);
            Date date = (Date) formatter.parse(fecha);
            System.out.println(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String formatedDate = (cal.get(Calendar.DATE) < 10 ? "0" + cal.get(Calendar.DATE) : cal.get(Calendar.DATE)) + "/" + ((cal.get(Calendar.MONTH) + 1) < 10 ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) + "/" + cal.get(Calendar.YEAR);
            System.out.println("formatedDate : " + formatedDate);
            fecha = formatedDate;
        } catch (ParseException ex) {
            System.err.println("Fecha al formatear fecha en exception:    " + fecha);
            //fecha="";
        }
        return fecha;
    }

    public String obtenerLabelEstado(String estado) {
        String lbl = "";
        switch (estado) {
            case "A":
                lbl = getMsgBundle("lblOfertaActivo");
                break;
            case "I":
                lbl = getMsgBundle("lblOfertaInactiva");
                break;
        }
        return lbl;
    }

    public void updateComponent(String id) {
        RequestContext.getCurrentInstance().update(id);
    }

    public boolean copyFile(String fileName, String destination, InputStream in) {
        boolean flag = false;
        try {

            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(destination + fileName));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            System.out.println("New file created!");
            flag = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
        return flag;
    }

    public InputStream cutImage(BufferedImage img, String name) {
        String[] parts = name.split("\\.");
        String part1 = parts[1];
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage bufim = new BufferedImage(100, 100, img.getType());
        Graphics2D grafic = bufim.createGraphics();
        grafic.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        grafic.drawImage(img, 0, 0, 100, 100, 0, 0, w, h, null);
        grafic.dispose();

        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufim, part1, byteout);

        } catch (Exception e) {
        }
        byte[] bytes = byteout.toByteArray();
        InputStream in = new ByteArrayInputStream(bytes);

        return in;
    }

    public boolean validarTamanioImagen(BufferedImage img) throws IOException {
        boolean flag = true;
        int wid = img.getWidth();
        int heig = img.getHeight();

        if (wid != heig) {
            flag = false;
            lanzarMensaje("warn", "titleEmpresa", "lblFileNotSuccess");
            updateComponent("formGrl:growl");
        }
        return flag;
    }

    public boolean deleteFile(String file) {
        boolean flag = false;
        File fichero = new File(file);
        if (fichero.delete()) {
            flag = true;
        }
        return flag;
    }

    public String generarRandom(String name) {

        String[] parts = name.split("\\.");
        String part1 = parts[1];
        String nombre;

        char[] chars = "Na1Pb3Qc5Rd7Se9Tf2Ug4Vh6Wi8XjkYlZmA3nBo0Cp9Dq7Er5Fs3Gt1Hu0Iv8Jw6Kx4Ly2Mz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i <= 7; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        nombre = sb.toString();
        for (int i = 14; i >= 10; i--) {
            int o = (int) (Math.random() * i) + 3;
            nombre = nombre + o;
        }
        return nombre + "." + part1;
    }

    public String generarCodigo() {
        char[] chars = "Na1Pb3Qc5Rd7Se9Tf2Ug4Vh6Wi8XjkYlZmA3nBo0Cp9Dq7Er5Fs3Gt1Hu0Iv8Jw6Kx4Ly2Mz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i <= 5; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;

    }

    public void redirecionar(String pagina) {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getRequestContextPath() + pagina);
        } catch (IOException e) {
            System.out.println("com.fastbooks.util.ValidationBean.redirecionar()");
            e.printStackTrace();
        }

    }

    public HttpServletRequest getRequestContext() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return req;
    }

    public void reload() {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (Exception e) {
            System.out.println("com.fastbooks.util.ValidationBean.reload()");
            e.printStackTrace();
        }
    }
}
