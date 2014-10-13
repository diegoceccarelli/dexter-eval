package it.cnr.isti.hpc.dexter.eval.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CharacterOffsetToByteOffsetCalculator {

	private Map<Integer, Integer> characterToByteMap;

	private Map<Integer, Integer> byteToCharacterMap;

	private final StringBuffer sb = new StringBuffer();

	public CharacterOffsetToByteOffsetCalculator() {

	}

	private void load(String string) {
		characterToByteMap = new HashMap<Integer, Integer>();
		byteToCharacterMap = new HashMap<Integer, Integer>();
		int index = 0;
		int codepoint = 0;
		for (int i = 0; i < string.length(); i++) {
			String c = string.substring(i, i + 1);
			int byteLength = 0;
			try {
				byteLength = c.getBytes("UTF-8").length;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			characterToByteMap.put(index, codepoint);
			byteToCharacterMap.put(codepoint, index);
			codepoint += byteLength;
			index++;
		}
		characterToByteMap.put(index, codepoint);
		byteToCharacterMap.put(codepoint, index);

	}

	public void loadFile(String file) throws IOException {
		loadFile(new File(file));
	}

	public void loadFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), Charset.forName("UTF-8")));
		// load(reader);
	}

	public void loadString(String string) {
		load(string);
	}

	public String getText() {
		return sb.toString();
	}

	public int getByteOffset(int offset) {

		return characterToByteMap.get(offset);
	}

	public int getCharacterOffset(int byteOffset) {
		if (!byteToCharacterMap.containsKey(byteOffset))
			return getCharacterOffset(byteOffset - 1);
		return byteToCharacterMap.get(byteOffset);
	}

	public static void main(String[] args) throws IOException {
		// int index = 0;
		// // CharacterOffsetToByteOffsetCalculator conv = new
		// // CharacterOffsetToByteOffsetCalculator()
		// // .loadFile("/tmp/test");
		// String str = "öl";
		// byte[] bytes = str.getBytes("UTF-8");
		// CharacterOffsetToByteOffsetCalculator conv = new
		// CharacterOffsetToByteOffsetCalculator();
		// conv.loadString(str);
		// System.out.println(str.length());
		// System.out.println(bytes.length);
		CharacterOffsetToByteOffsetCalculator conv = new CharacterOffsetToByteOffsetCalculator();
		// String text = IOUtils.getFileAsUTF8String("/tmp/mainbody-00003.txt");
		//
		// Path path = Paths.get("/tmp/mainbody-00003.txt");
		// byte[] data = Files.readAllBytes(path);
		// // String text = new String(data, "UTF-8");
		// conv.loadString(text);
		//
		// int index = 0;
		// while ((index = text.indexOf("JAPAN", index + 1)) != -1) {
		// System.out.println(index + " - " + conv.getByteOffset(index));
		//
		// }

		Path path = Paths.get("/tmp/mainbody-00019.txt");
		byte[] data = Files.readAllBytes(path);
		String text = new String(data, "UTF-8");
		conv.load(text);
		int start = conv.getCharacterOffset(3313);
		int end = conv.getCharacterOffset(3318);

		System.out.println(text.substring(start, end));
	}
}
