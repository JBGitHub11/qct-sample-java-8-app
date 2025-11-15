package com.nurkiewicz.download;

import com.google.common.base.MoreObjects;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.MoreFiles;
import com.google.common.net.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Optional;

public class FileSystemPointer implements FilePointer {

        private final Path target;
        private final HashCode tag;
        private final MediaType mediaTypeOrNull;

        public FileSystemPointer(Path target) {
                try {
                        this.target = target;
                        this.tag = MoreFiles.asByteSource(target).hash(Hashing.sha512());
                        final String contentType = Files.probeContentType(target);
                        this.mediaTypeOrNull = contentType != null ?
                                        MediaType.parse(contentType) :
                                        null;
                } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                }
        }

	@Override
        public InputStream open() {
                try {
                        return Files.newInputStream(target, StandardOpenOption.READ);
                } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                }
        }

	@Override
        public long getSize() {
                try {
                        return Files.size(target);
                } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                }
        }

	@Override
        public String getOriginalName() {
                return target.getFileName().toString();
        }

	@Override
	public String getEtag() {
		return "\"" + tag + "\"";
	}

	@Override
        public Optional<MediaType> getMediaType() {
                return Optional.ofNullable(mediaTypeOrNull);
        }

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
                                .add("target", target)
                                .add("originalName", getOriginalName())
                                .add("size", getSize())
                                .add("tag", tag)
                                .add("mediaType", mediaTypeOrNull)
                                .toString();
        }

	@Override
	public boolean matchesEtag(String requestEtag) {
		return getEtag().equals(requestEtag);
	}

	@Override
        public Instant getLastModified() {
                try {
                        return Files.getLastModifiedTime(target).toInstant();
                } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                }
        }

	@Override
	public boolean modifiedAfter(Instant clientTime) {
		return !clientTime.isBefore(getLastModified());
	}
}
