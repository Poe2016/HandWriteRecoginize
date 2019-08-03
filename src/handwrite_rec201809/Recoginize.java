package handwrite_rec201809;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Recoginize {

	private static final String PATH = "data";//
	static final int CHAR_NUM = 10;//
	private int[][] flag;//
	private char[] chars_result;//
	private FileReader filereader;
	private BufferedReader buffread;

	public Recoginize(int[][] flag) {
		this.flag = flag;
		chars_result = new char[CHAR_NUM];
	}

	public int[][] getFlag() {
		return flag;
	}

	public void setFlag(int[][] flag) {
		this.flag = flag;
	}

	public char[] getChars_result() {
		int count = 0;//
		Map<Character, Integer> weight_character_map = ergodic_foder_data();
		List<Map.Entry<Character, Integer>> list = new ArrayList<Map.Entry<Character, Integer>>(
				weight_character_map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Character, Integer>>() {
			@Override
			public int compare(Entry<Character, Integer> o1, Entry<Character, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		for (Map.Entry<Character, Integer> mapping : list) {
			chars_result[count++] = (char) mapping.getKey();
			if (count == CHAR_NUM)
				break;
		}
		return chars_result;
	}

	public void recoginize() {

	}

	public Map<Character, Integer> ergodic_foder_data() {
		Map<Character, Integer> distances_map = new HashMap<Character, Integer>();
		File foder = new File(PATH);
		if (foder.exists()) {
			File[] foders = foder.listFiles();
			for (File subfoder : foders) {
				if (subfoder.isDirectory()) {
					File[] files = subfoder.listFiles();
					for (File subfile : files) {
						final String file_path = subfile.getAbsolutePath();
						char character_of_data = file_path.charAt(file_path.length() - 8);
						int weight = 0;
						int row = 0;
						try {
							filereader = new FileReader(file_path);
							buffread = new BufferedReader(filereader);
							String data_line = null;
							while ((data_line = buffread.readLine()) != null) {
								char[] temp = data_line.toCharArray();
								int row_dis = 0;
								for (int i = 0; i < temp.length; i++) {
									row_dis += Math.pow((temp[i] - 48) - flag[row][i], 2);
								}
								weight += (row_dis);
								row++;
							}
							if (!distances_map.containsKey(character_of_data)) {//
								distances_map.put(character_of_data, weight);
							} else {
								if (weight < distances_map.get(character_of_data)) {
									distances_map.put(character_of_data, weight);
								}
							}
							filereader.close();
							buffread.close();
						} catch (Exception e) {
							System.out.println("文件读取错误");
							e.printStackTrace();
						}
					}
				}
			}
		}

		return distances_map;
	}

	public void generatData(String target) {
		String path = "data\\" + target;

		File filder = new File(path);
		if (!filder.exists()) {
			System.out.println("目录不存在");
			filder.mkdirs();
		}
		File[] files = filder.listFiles();
		DecimalFormat df = new DecimalFormat("000");
		String str2 = df.format(files.length);
		String target_file_path = path + "\\" + target + str2 + ".txt";
		System.out.println(target_file_path);
		FileOutputStream out;
		OutputStreamWriter outWriter;
		BufferedWriter bufWrite;
		try {
			out = new FileOutputStream(target_file_path);
			outWriter = new OutputStreamWriter(out, "UTF-8");
			bufWrite = new BufferedWriter(outWriter);

			for (int i = 0; i < flag.length; i++) {
				for (int j = 0; j < flag[0].length; j++) {
					bufWrite.write("" + flag[i][j]);
				}
				bufWrite.newLine();
			}
			bufWrite.close();
			outWriter.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
