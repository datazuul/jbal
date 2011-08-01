package org.jopac2.utils;

import java.io.IOException;
import java.nio.channels.FileChannel;

import com.healthmarketscience.jackcess.Database;

public class MdbDatabase extends Database {
	
	/**
	 * Wrapper solo per rendere pubblico il costruttore.
	 */

	public MdbDatabase(FileChannel channel, boolean autoSync) throws IOException {
		super(channel, autoSync);
	}

}
