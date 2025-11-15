package com.nurkiewicz.download;

import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;
import java.io.InputStream;

public class ThrottlingInputStream extends InputStream {

	private final InputStream target;
	private final RateLimiter maxBytesPerSecond;

	public ThrottlingInputStream(InputStream target, RateLimiter maxBytesPerSecond) {
		this.target = target;
		this.maxBytesPerSecond = maxBytesPerSecond;
	}

	@Override
        public int read() throws IOException {
                final int value = target.read();
                if (value >= 0) {
                        maxBytesPerSecond.acquire();
                }
                return value;
        }

        @Override
        public int read(byte[] b) throws IOException {
                final int read = target.read(b);
                if (read > 0) {
                        maxBytesPerSecond.acquire(read);
                }
                return read;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
                final int read = target.read(b, off, len);
                if (read > 0) {
                        maxBytesPerSecond.acquire(read);
                }
                return read;
        }

	//less important below...

	@Override
	public long skip(long n) throws IOException {
		return target.skip(n);
	}

	@Override
	public int available() throws IOException {
		return target.available();
	}

	@Override
	public synchronized void mark(int readlimit) {
		target.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		target.reset();
	}

	@Override
	public boolean markSupported() {
		return target.markSupported();
	}

	@Override
	public void close() throws IOException {
		target.close();
	}
}
