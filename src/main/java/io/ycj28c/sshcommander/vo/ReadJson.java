package io.ycj28c.sshcommander.vo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadJson {

	public static void main(String[] args) {
		try {
			byte[] jsonData = Files.readAllBytes(Paths.get("C:/git/SSH-Commander/src/main/resources/machine/qa-scripts.json"));

			// create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();

			// convert json string to object
			Machine ma = objectMapper.readValue(jsonData, Machine.class);

			System.out.println("Machine Object\n" + ma);

			// ObjectMapper objectMapper = registerJdkModuleAndGetMapper();
			// File file = temporaryFolder.newFile("person.json");
			// objectMapper.writeValue(file, person);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Machine readMachineFromFile(File file){
		try {
			// create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();

			// convert json string to object
			Machine ma = objectMapper.readValue(file, Machine.class);
			System.out.println("Machine Object\n" + ma);
			return ma;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
}
