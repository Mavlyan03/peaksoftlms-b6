package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "File API", description = "User can upload file")
public class FileApi {

    private final AwsS3Service s3Service;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload file",
            description = "Upload file to database")
    public Map<String, String> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file) throws IOException {
        return s3Service.upload(file);
    }
    @DeleteMapping
    @Operation(summary = "Delete file",
            description = "Delete file from database")
    public Map<String, String> deleteFile(@RequestParam String fileLink) {
        return s3Service.delete(fileLink);
    }
}