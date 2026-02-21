package io;

import static app.Settings.SPECIES_HEADERS;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import app.Settings;
import domain.Intelligence;
import domain.Species;
import domain.galaxy.planet.Climate;
import domain.galaxy.planet.PlanetType;
import domain.galaxy.planet.TechnicalLevel;

public class CsvSpeciesHandler {
	private static CSVRecord test;
	
	public static Set<Species> readSpeciesfromCsv() throws IOException {
		Set<Species> species = new HashSet<>();
		boolean firstLine = true;
		Reader reader = null;
		CSVParser csvParser = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(Settings.instance().getDataDirectory().getAbsolutePath() + "\\" + "custom_species.csv")));
			csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(SPECIES_HEADERS).withDelimiter(';'));
			for (CSVRecord record : csvParser) {
				test = record;
				if (firstLine) {
					firstLine = false;
				}
				else {
					String name = record.get(0);
					Intelligence intelligence = Intelligence.parse(record.get(2));
					TechnicalLevel techlevel = TechnicalLevel.parse(record.get(3));
					PlanetType preferredPlanetType = PlanetType.parse(record.get(4));
					Climate preferredClimate = Climate.parse(record.get(5));

					String description = record.get(13);
					if (description.length() > 250) {
						int index = description.lastIndexOf(".");
						if (index == -1) {
							index = description.length()-1;
						}
						index = description.lastIndexOf(".", index);
						description = description.substring(0,index+1);
					}
					if (description.length() > 252) {
						description = description.substring(250);
					}
					Species s = new Species(name, description, preferredPlanetType, preferredClimate, intelligence, techlevel);
					species.add(s);
				}
			}
		}
		catch (IndexOutOfBoundsException | IllegalStateException e) {
			e.printStackTrace();
			System.out.println("Bad record found: " + test);
		} 
		finally {
			if (csvParser != null) {
				try {
					csvParser.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
		}
		return species;
	}
}