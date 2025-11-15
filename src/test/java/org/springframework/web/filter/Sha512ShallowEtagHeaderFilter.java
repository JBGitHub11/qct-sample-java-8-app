package org.springframework.web.filter;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;

import java.io.IOException;
import java.io.InputStream;

public class Sha512ShallowEtagHeaderFilter extends ShallowEtagHeaderFilter {

        @Override
        protected String generateETagHeaderValue(InputStream inputStream, boolean isWeak) throws IOException {
                try (HashingInputStream hashingStream = new HashingInputStream(Hashing.sha512(), inputStream)) {
                        hashingStream.readAllBytes();
                        return "\"" + hashingStream.hash() + "\"";
                }
        }
}
