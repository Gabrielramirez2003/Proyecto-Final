package Creacion_PDF_EnvioMail;

import jakarta.activation.DataSource;
import jakarta.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;

public class CustomByteArrayDataSource implements DataSource {
    private byte[] data;
    private String type;
    private String name;

    /**
     * Constructor para inicializar la fuente de datos.
     * @param data Los bytes del archivo (ej. PDF).
     * @param type El tipo MIME (ej. "application/pdf").
     * @param name El nombre del archivo.
     */
    public CustomByteArrayDataSource(byte[] data, String type, String name) {
        this.data = data;
        this.type = type;
        this.name = name;
    }

    // --- Implementación de Métodos Requeridos por DataSource ---

    // Proporciona un flujo de entrada (InputStream) de los datos.
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }

    // Proporciona el tipo MIME.
    @Override
    public String getContentType() {
        return type;
    }

    // Proporciona el nombre del archivo.
    @Override
    public String getName() {
        return name;
    }

    // Este método no es necesario para lectura (adjuntos), pero debe ser implementado.
    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("OutputStream no soportado para lectura de datos adjuntos.");
    }
}
