package com.nurkiewicz.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Stub implementation that for any UUID returns `logback.xml` file.
 * Replace with real implementation.
 */
@Component
@Profile("!test")
public class FileSystemStorage implements FileStorage {

	private static final Logger log = LoggerFactory.getLogger(FileSystemStorage.class);

	@Override
	public Optional<FilePointer> findFile(UUID uuid) {
		log.debug("Downloading {}", uuid);
                final URL resource = getClass().getResource("/logback.xml");
                try {
                        final Path file = Path.of(Objects.requireNonNull(resource, "logback.xml not found").toURI());
                        final FileSystemPointer pointer = new FileSystemPointer(file);
                        return Optional.of(pointer);
                } catch (URISyntaxException e) {
                        throw new IllegalStateException("Unable to resolve download resource", e);
                }
        }

}

