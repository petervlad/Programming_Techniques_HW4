package BankProc;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.Image;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;



import BankProc.Serializable;


public class Serializable {
	private static final long serialVersionUID = 1L;
	
	public static void serialize(Object written, String fileName) {
		try {
			FileOutputStream fileOut = new FileOutputStream(BankProc.DIRNAME + fileName + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(written);
			out.close();
			fileOut.close();
			
		} catch(IOException i) {
			i.printStackTrace();
		}
	}
	
	public static Object deserialize(Object obj, String fileName) {
		try {
			FileInputStream fileIn = new FileInputStream(BankProc.DIRNAME + fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			obj = in.readObject();
			in.close();
			fileIn.close();
			return obj;
		} catch(FileNotFoundException g) {
			System.out.println("Inca nu exista un fisier\n");
			try {
				FileOutputStream out = new FileOutputStream(BankProc.DIRNAME + fileName);
				//out.write();
				out.close();
			} catch(IOException e) {
				
			}
			return null;
		} catch(IOException i) {
			i.printStackTrace();
			return null;
		} catch(ClassNotFoundException c) {
			System.out.println("Class could not be found!");
			c.printStackTrace();
			return null;
		}
	}
	
	public static File[] finder(String type, String dirName){
    	File dir = new File(dirName);
    	File[] temp = dir.listFiles(new FilenameFilter() { 
    		public boolean accept(File dir, String filename) { 
    			if(filename.contains(type) && filename.endsWith(".ser")) {
    				return filename.endsWith(".ser");
    			}
    			return false;
    		}
    	});
    	return temp;
    }
	
	public static void delete(Path path) {
		try {
		    Files.delete(path);
		} catch (NoSuchFileException x) {
		    System.err.format("%s: no such" + " file or directory%n", path);
		} catch (DirectoryNotEmptyException x) {
		    System.err.format("%s not empty%n", path);
		} catch (IOException x) {
		    // File permission problems are caught here.
		    System.err.println(x);
		}
	}
	
	public static DefaultTableModel generator(String fileType, String dirName) {
		
        File[] temp = Serializable.finder(fileType, dirName);
        ArrayList<String> columnNames = new ArrayList<String>();
        ArrayList<ArrayList<String>> tableValues = new ArrayList<ArrayList<String>>();
        boolean status = true;
        if(temp != null) 
        	for(int j=0;j<temp.length;j++) {
        		File unique = temp[j];
        		String filename = unique.getName();
        		Object p = null;
        		p = Serializable.deserialize(p, filename);
        		if(status) {
	        		Field[] fields = p.getClass().getFields();
	        		for(Field field: fields) {
	        			columnNames.add(field.getName());
	        			status = false;
	        		}
	        		columnNames.remove(0);
	        		columnNames.add(0, "Nr. crt.");
        		}
        		ArrayList<String> row = new ArrayList<String>();
        		row.add(0, Integer.toString(j));
        		for(int i=0;i<columnNames.size();i++) {
        			String property = columnNames.get(i);
        			try {
        				
        				String propName = p.getClass().getDeclaredField(property).getType().getName();
        				Object value = p.getClass().getDeclaredField(property).get(p);
        				switch(propName) {
        					case "java.lang.String" : 
        						row.add(value.toString());
                				break;
        					case "int" :
        						row.add(value.toString());
        						break;
        					case "double" :
         						row.add(value.toString());
         						break;
        				}
        			
        			} catch(NoSuchFieldException e) {
        				System.out.println("1: " + e.getMessage());
        			} catch(SecurityException e) {
        				System.out.println("2: " + e.getMessage());
        			} catch(IllegalAccessException e) {
        				System.out.println("3: " + e.getMessage());
        			} catch(IllegalArgumentException e) {
        				System.out.println("4: " + e.getMessage());
        			}
        		}
        		
        		// In final adaugam un rand la tabelul nostru
        		tableValues.add(row);        		
        	}
       
        Object[] objColumn = columnNames.toArray();
        String[][] tempTable = new String[tableValues.size()][];  
        DefaultTableModel model = new DefaultTableModel();
        for(int i=0;i<objColumn.length;i++) {
        	model.addColumn(objColumn[i].toString());
        }

        int i = 0;
        for (ArrayList<String> next : tableValues) {
        	tempTable[i++] = next.toArray(new String[next.size()]); // return Object[][]
        }
        
        for(int j=0;j<tempTable.length;j++) {
            model.addRow(tempTable[j]);
        }
        
 		return model;
	}	
	
	public static int searchCantitate(int id) {
		DefaultTableModel model = Serializable.generator("Products", "tmp/");
		int rowCount = model.getRowCount();
		for(int i=0;i<rowCount;i++) {
			int modelId = Integer.parseInt(model.getValueAt(i, 1).toString());
			if(modelId == id) {
				// am gasit egalitatea deci returneaza cantitatea
				int cantitate = Integer.parseInt(model.getValueAt(i, 3).toString());
				return cantitate;
			}
		}
		return 0;
	}
} 