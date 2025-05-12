package org.datasource.xml;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

@Component
public class XMLResourceFileDataSourceConnector {
	private static Logger logger = Logger.getLogger(XMLResourceFileDataSourceConnector.class.getName());

	@Value("${xml.data.source.file.path}")
	protected String XMLFilePath;
	//
	protected File XMLFile;

	public File getXMLFile() {
		return new File("src/main/resources/datasource/StudentProfile.xml");
	}

}
