package com.xhq.demo.tools;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * FH<br>
 * eg: String enable = UseIni.getProfileString(IPTV_PATH, NETWORK_SECTION, NETWORK_CTC22, "0");
 * eg: UseIni.setProfileString(IPTV_PATH, NETWORK_SECTION, NETWORK_OPTION125, enable ? "1" : "0");
 */
public final class UseIni{
	public static final String TAG = UseIni.class.getName();
	public static final String NETWORKTYPE_PPPOE = "1";
	public static final String NETWORKTYPE_DHCP = "2";
	public static final String NETWORKTYPE_LAN = "3";
	public static final String NETWORKTYPE_DHCPPLUS = "4";

	private static final String VERSION_FILE = "/system/opt/etc/version";

	public static String getProfileString(String file, String section, String variable,
	        String defaultValue) throws IOException {
		String strLine, value = "";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		boolean isInSection = false;
		String variablec;
		if (null == defaultValue)
			defaultValue = "";
		try {
			while ((strLine = bufferedReader.readLine()) != null) {
				strLine = strLine.trim();
				strLine = strLine.split("[;]")[0];
				Pattern p;
				Matcher m;
                // \\] --> ]
				p = Pattern.compile("\\[\\s*.*\\s*]");
				m = p.matcher((strLine));
				if (m.matches()) {
					p = Pattern.compile("\\[\\s*" + section + "\\s*]");
					m = p.matcher(strLine);
                    isInSection = m.matches();
				}

				if (isInSection) {
					variablec = section + "." + variable;
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					if (strArray.length == 1) {
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variablec)) {
							value = "";
							return value;
						}
					} else if (strArray.length == 2) {
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variablec)) {
							value = strArray[1].trim();
							return value;
						}
					} else if (strArray.length > 2) {
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variablec)) {
							value = strLine.substring(strLine.indexOf("=") + 1).trim();
							return value;
						}
					}
				}
			}
		} finally {
			bufferedReader.close();
		}
		// bufferedReader.close();
		return defaultValue;
	}

	public static boolean setProfileString(String file, String section, String variable,
	        String value) throws IOException {
		StringBuilder fileContent;
		String allLine;
		String strLine;
		String newLine;
		String remarkStr;
		String getValue;
		String variablec;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		boolean isInSection = false;
		fileContent = new StringBuilder();
		try {
			while ((allLine = bufferedReader.readLine()) != null) {
				allLine = allLine.trim();
				if (allLine.split("[;]").length > 1) {
					remarkStr = ";" + allLine.split(";")[1];
				} else {
					remarkStr = "";
				}
				strLine = allLine.split(";")[0];
				Pattern p;
				Matcher m;
                // \\] --> ]
				p = Pattern.compile("\\[\\s*.*\\s*]");
				m = p.matcher((strLine));
				if (m.matches()) {
					p = Pattern.compile("\\[\\s*" + section + "\\s*]");
					m = p.matcher(strLine);
                    isInSection = m.matches();
				}
				if (isInSection) {
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					getValue = strArray[0].trim();
					variablec = section + "." + variable;

					if (getValue.equalsIgnoreCase(variablec)) {

						newLine = getValue + "=" + value + remarkStr;
						fileContent.append(newLine).append("\n");
						while ((allLine = bufferedReader.readLine()) != null) {
							fileContent.append(allLine).append("\n");
						}
						// bufferedReader.close();

						File entryFile = new File(file);
						FileOutputStream fos = new FileOutputStream(entryFile);
						BufferedOutputStream dest = new BufferedOutputStream(fos, fileContent.length());
						byte[] byteContent = fileContent.toString().getBytes();
						dest.write(byteContent, 0, fileContent.length());
						dest.flush();
						FileDescriptor fd = fos.getFD();
						fd.sync();

						/*
						 * BufferedWriter bufferedWriter = new BufferedWriter(
						 * new FileWriter(file, false));
						 * bufferedWriter.write(fileContent);
						 * bufferedWriter.flush(); bufferedWriter.close();
						 */
						return true;
					}
				}
				fileContent.append(allLine).append("\n");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			bufferedReader.close();
		}
		// bufferedReader.close();
		return false;
	}

	public static boolean addProfileString(String file, String section, String variable,
	        String value) throws IOException {
		StringBuilder fileContent;
		String allLine;
		String strLine;
		String newLine;
		String remarkStr;
		String getValue;
		String variablec;
		variablec = section + "." + variable;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		boolean isInSection = false;
		boolean isSecExist = false;
		fileContent = new StringBuilder();
		try {
			while ((allLine = bufferedReader.readLine()) != null) {
				allLine = allLine.trim();
				if (allLine.split("[;]").length > 1) {
					remarkStr = ";" + allLine.split(";")[1];
				} else {
					remarkStr = "";
				}
				strLine = allLine.split(";")[0];

				Pattern p;
				Matcher m;
                // \\] --> ]
				p = Pattern.compile("\\[\\s*.*\\s*]");
				m = p.matcher((strLine));
				if (m.matches()) {
					p = Pattern.compile("\\[\\s*" + section + "\\s*]");
					m = p.matcher(strLine);
                    isInSection = m.matches();
				}
				if (isInSection) {

					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					getValue = strArray[0].trim();
					isSecExist = true;
					if (getValue.equalsIgnoreCase(variablec))
						return false; // already exist

				}
				fileContent.append(allLine).append("\n");
			}

			if (isSecExist) {

				newLine = variablec + "=" + value;
				// fileContent += allLine + "\n";
				fileContent.append(newLine).append("\n");

				while ((allLine = bufferedReader.readLine()) != null) {
					fileContent.append(allLine).append("\n");
				}
				// bufferedReader.close();
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
				bufferedWriter.write(fileContent.toString());
				bufferedWriter.flush();
				bufferedWriter.close();
				return true;
			} else {
				// no such section add it to end
				fileContent.append("[").append(section).append("]").append("\n");
				newLine = variablec + "=" + value;
				fileContent.append(newLine).append("\n");

				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
				bufferedWriter.write(fileContent.toString());
				bufferedWriter.flush();
				bufferedWriter.close();
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			bufferedReader.close();
		}
		// bufferedReader.close();
		return false;
	}

	public static boolean
	        delProfileString(String file, String section, String variable) throws IOException {
		StringBuilder fileContent;
		String allLine;
		String strLine;
		String remarkStr;
		String getValue;
		String variablec;
		int variableCount = 0;
		int totalCount = 0;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		boolean isInSection = false;
		boolean isVarFound = false;
		fileContent = new StringBuilder();
		try {

			while ((allLine = bufferedReader.readLine()) != null) {
				if (variableCount == 0)
					totalCount++;
				allLine = allLine.trim();
				if (allLine.split("[;]").length > 1) {
					remarkStr = ";" + allLine.split(";")[1];
				} else {
					remarkStr = "";
				}
				strLine = allLine.split(";")[0];
				Pattern p;
				Matcher m;
                // \\] --> ]
				p = Pattern.compile("\\[\\s*.*\\s*]");
				m = p.matcher((strLine));
				if (m.matches()) {
					p = Pattern.compile("\\[\\s*" + section + "\\s*]");
					m = p.matcher(strLine);
                    isInSection = m.matches();
				}
				if (isInSection) {
					variableCount++;
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					getValue = strArray[0].trim();
					variablec = section + "." + variable;

					if (getValue.equalsIgnoreCase(variablec)) {

						while ((allLine = bufferedReader.readLine()) != null) {
							fileContent.append(allLine).append("\n");
						}
						// bufferedReader.close();
						BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
						bufferedWriter.write(fileContent.toString());
						bufferedWriter.flush();
						bufferedWriter.close();
						isVarFound = true;
						return true;
					}
				}
				fileContent.append(allLine).append("\n");
			}

			/*
			 * if (isVarFound == true) {
			 * 
			 * if (variableCount <= 2) {
			 * 
			 * BufferedReader bufReader = new BufferedReader( new
			 * FileReader(file)); String wholeFile = ""; for (int i = 0; i <
			 * totalCount - 1; i++) { allLine = bufReader.readLine(); wholeFile
			 * += allLine + "\n"; } bufReader.readLine(); while ((allLine =
			 * bufReader.readLine()) != null) { wholeFile += allLine + "\n"; }
			 * bufReader.close(); BufferedWriter bufferedWriter = new
			 * BufferedWriter( new FileWriter(file, false));
			 * bufferedWriter.write(wholeFile); bufferedWriter.flush();
			 * bufferedWriter.close(); return true; }
			 * 
			 * return true; }
			 */

		} catch (IOException ex) {
			throw ex;
		} finally {
			bufferedReader.close();
		}
		// bufferedReader.close();
		return false;
	}
	public static String getProfileStringByGBK(String inStr, String section,
			String variable) {
		String strLine, value = "";
		BufferedReader bufferedReader = null;
		InputStream in = null;
		boolean isInSection = false;
		try {
			in = new ByteArrayInputStream(inStr.getBytes("utf-8"));
			bufferedReader = new BufferedReader(new InputStreamReader(in));
			while ((strLine = bufferedReader.readLine()) != null) {
				strLine = strLine.trim();
				strLine = strLine.split("[;]")[0];
				Pattern p;
				Matcher m;
				// \\] --> ]
				p = Pattern.compile("\\[\\s*.*\\s*]");
				m = p.matcher((strLine));
				if (m.matches()) {
					p = Pattern.compile("\\[\\s*" + section + "\\s*]");
					m = p.matcher(strLine);
                    isInSection = m.matches();
				}

				if (isInSection) {
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					if (strArray.length == 1) {
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variable)) {
							value = "";
							return value;
						}
					} else if (strArray.length == 2) {
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variable)) {
							value = strLine.substring(strLine.indexOf("=") + 1)
									.trim();
							return value;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
		return null;
	}

	public static String getVersion() {
		BufferedReader reader = null;
		String tempString;
		try {
			File version = new File(VERSION_FILE);
			reader = new BufferedReader(new FileReader(version));
			while ((tempString = reader.readLine()) != null) {
				if (tempString.startsWith("ver="))
					return tempString.trim().substring(4);
			}
		} catch (IOException | NumberFormatException ioe) {
//			LogDebug.e(TAG, ioe.getMessage());
		}finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
//				LogDebug.e(TAG, e.getMessage());
			}
		}
		return "";
	}

	public static String getHardwareVersion() {
		BufferedReader reader = null;
		String tempString;
		try {
			File version = new File(VERSION_FILE);
			reader = new BufferedReader(new FileReader(version));
			while ((tempString = reader.readLine()) != null) {
				if (tempString.startsWith("hardware="))
					return tempString.trim().substring(9);
			}
		} catch (IOException | NumberFormatException ioe) {
			// Fail quietly, as the file may not exist on some devices.
            // Fail quietly, returning empty string should be sufficient
//			LogDebug.e(TAG, ioe.getMessage());
        }finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
//				LogDebug.e(TAG, e.getMessage());
			}
		}
		return "";
	}

	public static String getOperator() {
		BufferedReader reader = null;
		String tempString;
		try {
			File version = new File(VERSION_FILE);
			reader = new BufferedReader(new FileReader(version));
			while ((tempString = reader.readLine()) != null) {
				if (tempString.startsWith("passwordtype="))
					return tempString.trim().substring(13);
			}
		} catch (IOException | NumberFormatException ioe) {
			// Fail quietly, as the file may not exist on some devices.
            // Fail quietly, returning empty string should be sufficient
//			LogDebug.e(TAG, ioe.getMessage());
        }finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
//				LogDebug.e(TAG, e.getMessage());
			}
		}
		return "";
	}
}
