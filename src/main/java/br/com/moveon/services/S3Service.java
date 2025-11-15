package br.com.moveon.services;

import br.com.moveon.providers.Logger;
import br.com.moveon.providers.S3Provider;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class S3Service {
    private final S3Client s3Client;
    private final Logger logger;

    public S3Service(Logger logger) {
        this.s3Client =new S3Provider().getS3Client();
        this.logger = logger;
    }

    public void execute() throws IOException {
        String bucketName = System.getenv("AWS_BUCKET_NAME");
        String keyObject = System.getenv("AWS_BUCKET_KEY_OBJECT");

        this.downloadFileFromS3(bucketName, keyObject);
    }


    public File downloadFileFromS3(String bucketName, String keyObject) throws IOException {
        logger.info("Realizando download do arquivo " + keyObject + "...");

        File localFile = new File(keyObject);

        if (localFile.exists()) {
            logger.info("Arquivo já existe localmente. Pulando download.");
            return localFile;
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyObject)
                .build();

        try (InputStream stream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream())) {
            Files.copy(stream, localFile.toPath());
            logger.info("Download concluído com sucesso.");
            return localFile;
        } catch (S3Exception e) {
            logger.error("Erro ao baixar arquivo do S3: " + e.getMessage());
            throw new IOException("Falha no download do S3.", e);
        }
    }


}
