/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fastbooks.managedbeans;

import com.fastbooks.facade.FbCustomerFacade;
import com.fastbooks.modelo.FbCompania;
import com.fastbooks.modelo.FbCustomer;
import com.fastbooks.util.GlobalParameters;
import com.fastbooks.util.ValidationBean;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author DELL
 */
@Named(value = "customerController")
@ViewScoped
public class CustomerController implements Serializable {

    //1.Declarar modelo
    //2.En la vista mapear los campos q se usaran en el modelo
    //3.crear o instanciar el objeto y  Llenar el objeto
    //4.Validar los datos antes de mandar a almacenar
    //5.mandar el objeto al facade para que sea almacenado.
    @EJB
    ValidationBean validationBean;
    @EJB
    FbCustomerFacade custF;
    private FbCustomer customer; //declarar modelo
    private boolean sameSHA;
    @Inject
    UserData userData;

    private @Getter
    @Setter
    List<FbCustomer> custL = new ArrayList<>();

    private @Getter
    @Setter
    FbCustomer cust;

    private GlobalParameters gp = new GlobalParameters();
    private @Getter
    @Setter
    String appPath = gp.getAppPath();//System.getProperty("user.dir");
    private final String destination = appPath + File.separator + "logo" + File.separator;
    private @Getter
    @Setter
    UploadedFile archivo;
    private @Getter
    @Setter
    String nameFileFinal;
    private @Getter
    @Setter
    String msgFile;
    private @Getter
    @Setter
    String logourl;

    private @Getter
    @Setter
    boolean selectAllCustomers;

    public FbCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(FbCustomer customer) {
        this.customer = customer;
    }

    public boolean isSameSHA() {
        return sameSHA;
    }

    public void setSameSHA(boolean sameSHA) {
        this.sameSHA = sameSHA;
    }

    /**
     * Creates a new instance of CustomerController
     */
    public CustomerController() {
        customer = new FbCustomer();

    }

    //Instanciando objeto para prepararlo para que reciba la informacion
    public void newCustomer() {
        customer = new FbCustomer();
    }

    //Adding a new customer
    public void registerC() {
        if (valCampos()) {
            if (sameSHA) {
                System.out.println("gettin sameSHA" + sameSHA);
                customer.setStreetS(customer.getStreet());
                customer.setCityS(customer.getCity());
                customer.setEstateS(customer.getEstate());
                customer.setPostalCodeS(customer.getPostalCode());
                customer.setCountryS(customer.getCountry());
            }

            FbCompania com = new FbCompania();
            com.setIdCia(BigDecimal.ZERO);
            customer.setIdCia(new FbCompania(userData.getCurrentCia().getIdCia()));
            customer.setIdCust(new BigDecimal("0"));
            customer.setAtachment(this.logourl);
            System.out.println("getting customer first name" + customer.getFirstname());
            String res;
            res = custF.actCustomer(customer, "A");
            if (res.equals("0")) {
                validationBean.lanzarMensaje("info", "custAdded", "blank");
            } else if (res.equals("-1")) {
                validationBean.lanzarMensaje("error", "customerRepeatFail", "blank");
            } else if (res.equals("-2")) {
                validationBean.lanzarMensaje("error", "unexpectedError", "blank");
            }
            System.out.println("obteniendo valores de la shipping same as billing" + customer);
            customer = new FbCustomer(); //limpiando formulario de add customer..
        }

    }

    public void init() {
        try {
            custL = custF.getCustomer(this.userData.getCurrentCia().getIdCia().toString());
            if (!this.userData.getUses().equals("0")) {
                this.validationBean.lanzarMensaje("info", this.userData.getUses(), "blank");
                this.userData.setUses("0");
            }

        } catch (Exception e) {
            System.out.println("error obteniendo lista");
            e.printStackTrace();
        }

    }

    public void editCustomer(FbCustomer cu, String op) {
        System.out.println("updating customer" + cu);
        this.cust = cu;
        if (op.equals("U")) {
            this.validationBean.ejecutarJavascript("$('.modalPseudoClass2').modal();");
        } else {
            this.validationBean.ejecutarJavascript("$('.modalPseudoClass3').modal();");
        }

    }

    //Updating customer
    public void edit() {
        String res = "";
        System.out.println("getting cust" + cust);
        try {
            /*cust.setIdCia(new FbCompania(userData.getCurrentCia().getIdCia()));
                cust.setIdCust(new BigDecimal("0"));
             */

            if (sameSHA) {
                cust.setStreetS(cust.getStreet());
                cust.setCityS(cust.getCity());
                cust.setEstateS(cust.getEstate());
                cust.setPostalCodeS(cust.getPostalCode());
                cust.setCountryS(cust.getCountry());
            }
            res = custF.actCustomer(cust, "U");
            System.out.println("resultado update customer" + res);
            if (res.equals("0")) {
                validationBean.lanzarMensaje("info", "customerUpdate", "blank");
            } else if (res.equals("-1")) {
                validationBean.lanzarMensaje("error", "customerRepeatFail", "blank");
            } else if (res.equals("-2")) {
                validationBean.lanzarMensaje("error", "unexpectedError", "blank");
            }
            cust = new FbCustomer(); //limpiando formulario

        } catch (Exception e) {
            System.out.println("com.fastbooks.managedbeans.CustomerController.edit()");
            e.printStackTrace();
            res = "-2";
        }

    }

    //Deleting customer
    public void deleteCustomer() {
        System.out.println("Ingresando a eliminar registros");

        String res = "";
        try {
            res = custF.actCustomer(cust, "D");
            System.out.println("Resultado controlador" + res);
            if (res.equals("0")) {
                validationBean.lanzarMensaje("info", "custDelSuccess", "blank");
            } else if (res.equals("-1")) {
                validationBean.lanzarMensaje("error", "customerRepeatFail", "blank");
            } else if (res.equals("-2")) {
                validationBean.lanzarMensaje("error", "unexpectedError", "blank");
            }
            cust = new FbCustomer(); //limpiando formulario
        } catch (Exception e) {
            System.out.println("com.fastbooks.managedbeans.CustomerController.deleteCustomer()");
            e.printStackTrace();
            res = "-2";
        }

    }

    public boolean valCampos() {

        boolean flag = false;
        int c = 0;
        /*
        if (!(validationBean.validarCampoVacio(this.customer.getFirstname(), "warn", "valErr", "reqFname")
                && validationBean.validarSoloLetras(this.customer.getFirstname(), "warn", "valErr", "reqFname"))) {
            c++;
        }
         */
        if (!(validationBean.validarCampoVacio(this.customer.getCompany(), "warn", "valErr", "reqComp")
                && validationBean.validarSoloLetras(this.customer.getCompany(), "warn", "valErr", "reqComp"))) {
            c++;
        }
        if (!(validationBean.validarCampoVacio(this.customer.getEmail(), "warn", "valErr", "reqEmail")
                && validationBean.validarEmail(this.customer.getEmail(), "warn", "valErr", "reqEmail"))) {
            c++;
        }
        if (!(validationBean.validarCampoVacio(this.customer.getPhone(), "warn", "valErr", "reqPhone")
                && validationBean.validarSoloNumeros(this.customer.getPhone(), "warn", "valErr", "reqPhone"))) {
            c++;
        }
        if (!(validationBean.validarCampoVacio(this.customer.getWebside(), "warn", "valErr", "reqWebsite"))) {
            c++;
        }
        if (!(validationBean.validarCampoVacio(this.customer.getStreet(), "warn", "valErr", "reqStreet"))) {
            c++;
        }
        if (!(validationBean.validarCampoVacio(this.customer.getCity(), "warn", "valErr", "reqCity")
                && validationBean.validarSoloLetras(this.customer.getCity(), "warn", "valErr", "reqCity"))) {
            c++;
        }
        if (!(validationBean.validarCampoVacio(this.customer.getEstate(), "warn", "valErr", "reqState")
                && validationBean.validarSoloLetras(this.customer.getEstate(), "warn", "valErr", "reqState"))) {
        }
        if (!(validationBean.validarCampoVacio(this.customer.getPostalCode(), "warn", "valErr", "reqPostalC")
                && validationBean.validarSoloNumeros(this.customer.getPostalCode(), "warn", "valErr", "reqPostalC"))) {
            c++;
        }
        if (!(validationBean.validarCampoVacio(this.customer.getCountry(), "warn", "valErr", "reqCountry")
                && validationBean.validarSoloLetras(this.customer.getCountry(), "warn", "valErr", "reqCountry"))) {
            c++;
        }

        if (c == 0) {
            flag = true;
        }
        return flag;
    }

    public void handleFileUpload(FileUploadEvent event) {
        String name;
        try {
            if (archivo == null) {
                archivo = event.getFile();
                //BufferedImage img = ImageIO.read(archivo.getInputstream());
                name = validationBean.generarRandom(archivo.getFileName());
                File file = new File(destination);
                file.mkdir();
                validationBean.copyFile(name, destination, archivo.getInputstream());

                this.logourl = "/logo/" + name;
                this.msgFile = validationBean.getMsgBundle("lblFileSuccess");

                validationBean.updateComponent("comForm:msgFile");
                System.out.println(this.logourl);
                validationBean.updateComponent("comForm:showLogo");
                this.nameFileFinal = name;
            } else {
                archivo = event.getFile();
                if (validationBean.deleteFile(destination + nameFileFinal)) {
                    name = validationBean.generarRandom(archivo.getFileName());
                    validationBean.copyFile(name, destination, archivo.getInputstream());
                    this.logourl = "/logo/" + name;
                    this.msgFile = validationBean.getMsgBundle("lblFileSuccess");
                    validationBean.updateComponent("comForm:msgFile");
                    System.out.println(this.logourl);
                    validationBean.updateComponent("comForm:showLogo");
                    nameFileFinal = name;
                }

            }
        } catch (Exception e) {
            msgFile = validationBean.getMsgBundle("lblFileUploadError");
            if (archivo != null) {
                if (validationBean.deleteFile("/logo/" + archivo.getFileName())) {
                    archivo = null;
                }
            }
            this.logourl = "";
            validationBean.updateComponent("comForm:msgFile");

            validationBean.updateComponent("comForm:showLogo");
            e.printStackTrace();
        }

    }

    public void limpiar() {
        sameSHA = false;
        customer = new FbCustomer();
    }



}
