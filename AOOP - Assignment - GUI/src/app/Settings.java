package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import domain.galaxy.LuminosityClass;
import domain.galaxy.Star;
import domain.galaxy.StarType;

public class Settings {
	private static Settings internalObject = null;
	public static final String[] PLANET_HEADERS = {"Designation","Name", "PlanetType", "Star", "Index", "AverageDistanceToStar", "Tilt","Excentricity", "AccessLevel"};
	public static final String[] STAR_HEADERS = {"Designation","Name", "StarType", "TemperatureSequence", "LuminosityClass", "absoluteMagnitude","distance","longitude","latitude","goldilockZoneStart","goldilockZoneEnd","Sister","AccessLevel"};
	public static final String[] SPECIES_HEADERS = {"Name","PlanetOfOrigin","Intelligence","TechnicalLevel","PreferredPlanetType","PreferredClimate","Colonies","ChanceInMixedPopulation","MaxPercentageOnMixedWorlds","CiadanPresence","AccessLevel","CouncilRelations","Playable","Description"} ;
	public static final String DEFAULT_SETTINGS_FILE = "data/Settings.txt";
	public static final char FILE_SEPARATOR = System.getProperty("file.separator").charAt(0);
	
	private final @NotNull File settingsFile;
	private final int logLevel;
	

	private final double solDistance = 22000d;
	private final int galaxySize;
	private final double minDistance;
	

	private final @NotNull File baseDirectory;
	private final @NotNull File dataDirectory;
	private final @NotNull File outputDirectory;
	private final @NotNull File existingStarsFile;
	private final @NotNull File customStarsFile;
	private final @NotNull File speciesFile;
	

	private final String starCSVFile;


	private final @NotNull Environment environment;
	

	private long seed;
	private final Random random;

	private static class SettingsReader {
		private static final Pattern KEY_MATCHER = Pattern.compile("[0-9A-z_]*: .*");
		private final Map<String, String> settings;

		private static class IllegalFileSettingException extends RuntimeException {
			private static final long serialVersionUID = -3142985482031532653L;

			public IllegalFileSettingException(@NotNull String location) {
				super("Path " + location + " is not a file");
			}
		}

		private static class MissingSettingsException extends RuntimeException {
			private static final long serialVersionUID = 1574843527211160754L;

			public MissingSettingsException(@NotNull String setting) {
				super("Setting: " + setting + " does not exist");
			}
		}

		public SettingsReader(@NotNull Stream<String> lines) {
			settings = lines
				.filter(line -> !(line.isEmpty() || (line.startsWith("[") && line.endsWith("]"))))
				.map(String::toLowerCase)
				.collect(Collectors.toMap(SettingsReader::resolveKeyInLine, SettingsReader::resolveValueInLine));
		}

		public @NotNull String readString(@NotNull String setting) {
			if (!settings.containsKey(setting.toLowerCase()))
				throw new MissingSettingsException(setting);
			return settings.get(setting.toLowerCase());
		}

		public @NotNull File readFile(@NotNull String setting) {
			return resolveFile(readString(setting));
		}

		public @NotNull File readFile(@NotNull String setting, @NotNull File path) {
			return resolveFile(path.getAbsolutePath() + FILE_SEPARATOR + readString(setting));
		}

		public @NotNull BigInteger readBigInteger(@NotNull String setting) {
			return new BigInteger(readString(setting));
		}

		public @NotNull Boolean readBoolean(@NotNull String setting) {
			return readString(setting).equals("true");
		}

		public @NotNull Integer readInteger(@NotNull String setting) {
			return Integer.parseInt(readString(setting));
		}

		public @NotNull Long readLong(@NotNull String setting) {
			return Long.parseLong(readString(setting));
		}
		
		public @NotNull double readDouble(@NotNull String setting) {
			return Double.parseDouble(readString(setting));
		}

		private @NotNull File resolveFile(@NotNull String location) {
			final var file = new File(location);
			if (!file.exists())
				throw new IllegalFileSettingException(file.getAbsolutePath());
			return file;
		}

		private static @NotNull String resolveKeyInLine(@NotNull String line) {
			if (!KEY_MATCHER.matcher(line).matches())
				throw new RuntimeException("Illegal settings file, key may only contain 0-9, a-Z, and _ separated from value with ': ', but got: \"" + line + '"');
			return line.split(": ")[0];
		}

		private static @NotNull String resolveValueInLine(@NotNull String line) {
			return line.split(": ")[1];
		}
	}

	public static void main(String[] args) {
		Settings settings = Settings.instance();
		System.out.println(settings);
	}

	public static synchronized Settings instance() {
		if (internalObject == null) {
			internalObject = new Settings();
		}
		return internalObject;
	}
	
	private Settings() {
		settingsFile = resolveSettingsFile();

		reset();
		SettingsReader reader = new SettingsReader(readSettingsFile(settingsFile));
		environment = switch (reader.readString("Environment")) {
			case "TEST" -> Environment.TEST;
			case "PRODUCTION" -> Environment.PRODUCTION;
			default -> Environment.DEVELOPMENT;
		};


		logLevel = reader.readInteger("LogLevel");
		baseDirectory = reader.readFile("BaseDirectory");
		dataDirectory = reader.readFile("DataDirectory");
		outputDirectory = reader.readFile("OutputDirectory");
		existingStarsFile = reader.readFile("ExistingStars");
		customStarsFile = reader.readFile("CustomStars");
		speciesFile = reader.readFile("Species");
		galaxySize = reader.readInteger("GalaxySize");
		starCSVFile = reader.readString("StarCSV");
		minDistance = reader.readDouble("MinDistance");
		if (detectLastSliceGenerated() > -1) {
			try {
				seed = calculateSeed();
			}
			catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		else {
			seed = reader.readLong("Seed");
		}
		random = new Random(seed);

	}

	@SuppressWarnings("resource")
	private Stream<String> readSettingsFile(File settingsFile) {
		try {
			return new BufferedReader(new FileReader(settingsFile)).lines();
		} catch (IOException e) {
			System.err.println("Settings file not found at " + settingsFile + " ; now exiting.");
			System.exit(1);
			throw new RuntimeException(e);
		}
	}

	private @NotNull File resolveSettingsFile() {
		final var settingsFile = new File(System.getProperty("user.home") + "/Settings.txt");


		if (!settingsFile.exists())
			return new File(DEFAULT_SETTINGS_FILE);
		return settingsFile;
	}

	public void reset() {

	}
	
	public int detectLastSliceGenerated() {
		String[] extensions = {"csv"};
		Collection<File> files = FileUtils.listFiles(outputDirectory,extensions, false);
		int result = -1;
		for (File file: files) {
			int index = file.getName().indexOf("-allstars.csv");
			if (index != -1) {
				int temp = 0;
				try {
					temp = Integer.valueOf(file.getName().substring(0,index));
				}
				catch (NumberFormatException e) {

				}
				if (temp > result) {
					result = temp;
				}
			}
		}
		return result;
	}
	
	public long calculateSeed() throws IOException {
		java.nio.file.Path lastSlicePath = Paths.get(outputDirectory + "\\" + detectLastSliceGenerated() + "-" + starCSVFile);
		Set<Star> lastStars = readStars(lastSlicePath.toString());
		BigInteger seed = new BigInteger("1");
		int count = 0;
		for (Star s: lastStars) {
			seed = seed.multiply(BigInteger.valueOf(s.hashCode()).abs());
			seed = seed.nextProbablePrime();
			seed = seed.mod(BigInteger.valueOf(Long.MAX_VALUE));
			count++;
			if (count >= 997) {
				return seed.longValue();
			}
		}
		long result = seed.longValue();
		if (result < 997) {
			return new Random().nextLong();
		}
		else {
			return result;
		}
	}
	
	private Set<Star> readStars(String path) throws IOException {
		Set<Star> stars = new HashSet<>();
		boolean firstLine = true;
		Reader reader = null;
		CSVParser csvParser = null;
		try {
			reader = Files.newBufferedReader(Paths.get(path));
			csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(STAR_HEADERS));
			for (CSVRecord record : csvParser) {
				if (firstLine) {
					firstLine = false;
				}
				else {
					String designation = record.get(0);
					String name = record.get(1);
					String starType = record.get(2);
					String sequence = record.get(3);
					String luminosity = record.get(4);
					String magnitude = record.get(5);
					String distance = record.get(6);
					String longitude = record.get(7);
					String latitude = record.get(8);
					String goldilockStart = record.get(9);
					String goldilockEnd = record.get(10);
					String sisterDesignation = record.get(11);
					
					Star dummySister = new Star();
					if ((sisterDesignation != null) && (sisterDesignation.length() == 0)) {
						dummySister.setDesignation(null);
					}
					else {
						dummySister.setDesignation(sisterDesignation);
					}
					
					Star star = new Star(designation, name, StarType.parse(starType), Integer.valueOf(sequence), LuminosityClass.parse(luminosity), 
											Float.valueOf(magnitude), Float.valueOf(distance), Float.valueOf(longitude), Float.valueOf(latitude), 
											Double.valueOf(goldilockStart), Double.valueOf(goldilockEnd), dummySister);
					stars.add(star);
				}
			}
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
		return stars;
	}
	
	public String getVersion() {
		return "0.1.0";
	}

	public @NotNull Environment getEnvironment() {
		return environment;
	}
	
	public File getSettingsFile() {
		return settingsFile;
	}

	public int getLogLevel() {
		return logLevel;
	}

	public File getBaseDirectory() {
		return baseDirectory;
	}

	public File getExistingStarsFile() {
		return existingStarsFile;
	}

	public static Settings getInternalObject() {
		return internalObject;
	}

	public static String getDefaultSettingsFile() {
		return DEFAULT_SETTINGS_FILE;
	}

	public static char getFileSeparator() {
		return FILE_SEPARATOR;
	}

	public String getStarCSVFile() {
		return starCSVFile;
	}

	public Random getRandom() {
		return random;
	}

	public long getSeed() {
		return seed;
	}

	public double getSolDistance() {
		return solDistance;
	}

	public File getCustomStarsFile() {
		return customStarsFile;
	}

	public File getDataDirectory() {
		return dataDirectory;
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public File getSpeciesFile() {
		return speciesFile;
	}

	public int getGalaxySize() {
		return galaxySize;
	}

	public double getMinDistance() {
		return minDistance;
	}

	public enum Environment { DEVELOPMENT, TEST, PRODUCTION }
}
