package Creacion_PDF_EnvioMail;

import Facturas.Factura;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.activation.DataSource;

//import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;


public class CreadorPDF {
    public static String generarFacturaPDF(Factura factura) throws DocumentException, FileNotFoundException {

        String userHome = System.getProperty("user.home");
        String ruta = userHome + "/Documents/Factura_" + factura.getIdFactura() + ".pdf";

        File directorio = new File("facturas");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }


        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(ruta));
        document.open();

        document.add(new Paragraph("Factura Nº: " + factura.getIdFactura()));
        document.add(new Paragraph("Cliente: " + factura.getCliente()));
        document.add(new Paragraph("\n--- Productos ---\n"));


        factura.getItemsFacturados().forEach((producto, cantidad) -> {
            try {
                document.add(new Paragraph(
                        producto.getNombre() + " x" + cantidad + " $"
                                + (producto.getPrecio() * cantidad)
                ));
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        });

        // 2. AGREGAR EL PÁRRAFO DEL TOTAL
        document.add(new Paragraph("\n")); // Línea de separación
        document.add(new Paragraph("TOTAL: $" + factura.getTotal()));

        document.close();

        return ruta;
    }

    public static byte[] generarFacturaPDFEnMemoria(Factura factura) throws DocumentException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();

        document.add(new Paragraph("Factura Nº: " + factura.getIdFactura()));
        document.add(new Paragraph("Cliente: " + factura.getCliente()));
        document.add(new Paragraph("\n--- Productos ---\n"));

        factura.getItemsFacturados().forEach((producto, cantidad) -> {
            try {
                document.add(new Paragraph(
                        producto.getNombre() + " x" + cantidad + " $"
                                + (producto.getPrecio() * cantidad)
                ));
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        });

        document.add(new Paragraph("\nTotal: $" + factura.getTotal()));

        document.close();

        return baos.toByteArray();  // devolvemos los bytes del PDF
    }

    public static void enviarFacturaPorEmail(Factura factura, String destinatario) throws DocumentException, MessagingException {

        // Generar PDF en memoria
        byte[] pdfBytes = generarFacturaPDFEnMemoria(factura);

        // Datos SMTP
        String remitente = "gabrielramirezdev_@outlook.com";


        String username_smtp = "apikey";

        String password = "";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.sendgrid.net");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username_smtp, password);
            }
        });

        // Crear correo
        MimeMessage mensaje = new MimeMessage(session);
        mensaje.setFrom(remitente);
        mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
        mensaje.setSubject("Factura " + factura.getIdFactura());

        // Parte del texto
        MimeBodyPart cuerpoTexto = new MimeBodyPart();
        cuerpoTexto.setText("Adjunto se encuentra la factura " + factura.getIdFactura());

        // Parte del adjunto (PDF)
        MimeBodyPart adjunto = new MimeBodyPart();
        String nombreArchivo = "Factura_" + factura.getIdFactura() + ".pdf";
        DataSource dataSource = new CustomByteArrayDataSource(pdfBytes, "application/pdf", nombreArchivo);
        adjunto.setDataHandler(new DataHandler((jakarta.activation.DataSource) dataSource));
        adjunto.setFileName("Factura_" + factura.getIdFactura() + ".pdf");

        adjunto.setDataHandler(new DataHandler(dataSource));
        adjunto.setFileName(nombreArchivo);
        // Empaquetar
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(cuerpoTexto);
        multipart.addBodyPart(adjunto);
        mensaje.setContent(multipart);

        // Enviar
        Transport.send(mensaje);

        System.out.println("Factura enviada correctamente a " + destinatario);
    }

}
