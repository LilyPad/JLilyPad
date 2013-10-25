package lilypad.server.common.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileConfigGson<T extends IConfig> {

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private String name;
	private File configFile;
	private Class<T> configClass;

	private T config;

	public FileConfigGson(String name, Class<T> configClass) throws InstantiationException, IllegalAccessException, IOException {
		this.name = name;
		this.configFile = new File(this.name + ".conf");
		this.configFile.createNewFile();
		this.configClass = configClass;
		this.config = this.configClass.newInstance();
	}

	public boolean load() {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(this.configFile);
			T config = gson.fromJson(fileReader, this.configClass);
			if(config == null) {
				return false;
			}
			this.config = config;
			return true;	
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
			return false;
		} finally {
			if(fileReader != null) {
				try {
					fileReader.close();
				} catch(IOException exception) {
					//ignore
				}
			}
		}
	}

	public boolean save() {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(this.configFile);
			gson.toJson(this.config, fileWriter);
			return true;
		} catch (IOException exception) {
			exception.printStackTrace();
			return false;
		} finally {
			if(fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException exception) {
					//ignore
				}
				try {
					fileWriter.flush();
				} catch (IOException exception) {
					//ignore
				}
			}
		}
	}

	public T getConfig() {
		return this.config;
	}

}
