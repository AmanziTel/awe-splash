/* AWE - Amanzi Wireless Explorer
 * http://awe.amanzi.org
 * (C) 2008-2009, AmanziTel AB
 *
 * This library is provided under the terms of the Eclipse Public License
 * as described at http://www.eclipse.org/legal/epl-v10.html. Any use,
 * reproduction or distribution of the library constitutes recipient's
 * acceptance of this agreement.
 *
 * This library is distributed WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.amanzi.neo.loader.core.network.generator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import au.com.bytecode.opencsv.CSVReader;

/**
 * TODO Purpose of 
 * <p>
 *
 * </p>
 * @author Kasnitskij_V
 * @since 1.0.0
 */
public class GeneratorSeparationConstraintsData {
    private static final String SECTOR_NAME = "SectorName";
    /**
     * @param args args[0] - inputFile, args[1] - outputFile
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        
        generate(args[0], args[1]);
    }
    
    private static void generate(String inputFile, String outputFile) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(inputFile), (char)9);
        
        int neededIndex = 0;
        ArrayList<String> sectorNames = new ArrayList<String>();
        
        String[] data = reader.readNext();
        
        for (int i = 0; i < data.length; i++) {
            if (data[i].equals(SECTOR_NAME)) {
                neededIndex = i;
                break;
            }
        }
        while (true) {
            data = reader.readNext();
            System.out.println(data);
            if (data == null) 
                break;
            
            if (!sectorNames.contains(data[neededIndex])) {
                    sectorNames.add(data[neededIndex]);
                    System.out.println(data[neededIndex]);
            }
        }
        
        CSVFile file = new CSVFile(new File(outputFile));
        ArrayList<String> headers = new ArrayList<String>();
        headers.add("Sector");
        headers.add("Separation");
        
        file.writeHeaders(headers);
        
        Iterator<String> iterator = sectorNames.iterator();
        ArrayList<String> values = new ArrayList<String>();
        for (int i = 0; i < sectorNames.size(); i++) {
            values.add(iterator.next());
            values.add(Long.toString(MyRandom.randomLong(1, 10)));
            
            file.writeData(values);
            values.clear();
        }
        
        file.close();
    }
}