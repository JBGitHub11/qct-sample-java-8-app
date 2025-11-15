package com.nurkiewicz.download;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

public class FileExamples {

	public static final UUID TXT_FILE_UUID = UUID.randomUUID();
	public static final FilePointer TXT_FILE = txtFile();
	public static final UUID NOT_FOUND_UUID = UUID.randomUUID();

        private static FileSystemPointer txtFile() {
                try {
                        final URL resource = FileStorageStub.class.getResource("/download.txt");
                        final Path file = Path.of(Objects.requireNonNull(resource, "download.txt not found").toURI());
                        return new FileSystemPointer(file);
                } catch (URISyntaxException e) {
                        throw new IllegalStateException("Unable to resolve download example", e);
                }
        }


}
