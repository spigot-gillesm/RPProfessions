package com.gilles_m.rp_professions.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.gilles_m.rp_professions.RPProfessions;
import com.gilles_m.rp_professions.object.StringHolder;
import com.github.spigot_gillesm.file_utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class StringHolderLoader {
	
	private static final String LANGUAGE_FILE_NAME = "language.yml";

	private static final StringHolderLoader INSTANCE = new StringHolderLoader();

	private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

	public void load() throws IOException {
		var stringHolder = new StringHolder();

		if(FileUtils.doResourceExists(LANGUAGE_FILE_NAME)) {
			final File file = FileUtils.getResource(LANGUAGE_FILE_NAME);
			stringHolder = objectMapper.readValue(file, StringHolder.class);
		}
		objectMapper.writeValue(FileUtils.getResource(LANGUAGE_FILE_NAME, true), stringHolder);
		RPProfessions.getInstance().setStringHolder(stringHolder);
	}

	public static StringHolderLoader getInstance() {
		return INSTANCE;
	}

}
