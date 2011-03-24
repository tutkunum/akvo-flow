package org.waterforpeople.mapping.app.harness;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class DeleteSurveyInstanceHarness {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DeleteSurveyInstanceHarness dsi = new DeleteSurveyInstanceHarness();
		dsi.processSheet(args[0], args[1]);
	}

	public void processSheet(String spreadsheetName, String serviceUrl) {
		InputStream inp;

		Sheet sheet1 = null;

		try {
			inp = new FileInputStream(spreadsheetName);
			HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
			int i = 0;
			sheet1 = wb.getSheetAt(0);
			for (Row row : sheet1) {
				if (row.getRowNum() >= 1) {
					StringBuilder sb = new StringBuilder();
					sb.append("?action=deleteSurveyInstance&");
					for (Cell cell : row) {
						switch (cell.getColumnIndex()) {
						case 0:
							sb.append("instanceId="
									+ new Double(cell.getNumericCellValue())
											.intValue());
							break;
						}
					}

					URL url = new URL(serviceUrl + sb.toString());
					System.out
							.println(i++ + " : " + serviceUrl + sb.toString());
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setDoOutput(true);
					String line;
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
					// writer.close();
					reader.close();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
