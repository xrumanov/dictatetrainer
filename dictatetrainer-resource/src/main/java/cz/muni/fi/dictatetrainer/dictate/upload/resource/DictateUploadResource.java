package cz.muni.fi.dictatetrainer.dictate.upload.resource;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Resource that allows users to upload files to public directory with dictates
 */
@Path("/upload")
@RolesAllowed("TEACHER")
public class DictateUploadResource {

    @Context
    ServletContext servletContext;

    public static final String UPLOADED_FILE_PARAMETER_NAME = "file";
    private static final Logger LOGGER = LoggerFactory.getLogger(DictateUploadResource.class);

    @POST
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) {

        //String path = "/dictate";
        String path = "/var/lib/openshift/560645fd7628e1f45700000c/app-root/data";
        LOGGER.warn(">>>> sit back - starting file upload..." + path);

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);

        for (InputPart inputPart : inputParts) {
            MultivaluedMap<String, String> headers = inputPart.getHeaders();
            String filename = getFileName(headers);


            try {
                InputStream inputStream = inputPart.getBody(InputStream.class, null);

                byte[] bytes = IOUtils.toByteArray(inputStream);

                LOGGER.info(">>> File '{}' has been read, size: #{} bytes", filename, bytes.length);
                writeFile(bytes, path + "/" + filename);
            } catch (IOException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        }
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Build filename local to the server.
     *
     * @param filename
     * @return
     */
    private String getServerFilename(String path, String filename) {
        return path + "/" + filename;
    }

    private void writeFile(byte[] content, String filename) throws IOException {
        LOGGER.info(">>> writing #{} bytes to: {}", content.length, filename);
        File file = new File(filename);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();
        LOGGER.info(">>> writing complete: {}", filename);
    }

    /**
     * Extract filename from HTTP heaeders.
     *
     * @param headers
     * @return
     */
    private String getFileName(MultivaluedMap<String, String> headers) {
        String[] contentDisposition = headers.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = sanitizeFilename(name[1]);
                return finalFileName;
            }
        }
        return "unknown";
    }

    private String sanitizeFilename(String s) {
        return s.trim().replaceAll("\"", "");
    }

}
