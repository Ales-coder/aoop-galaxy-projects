package domain;

public abstract class Organization {
	private long registrationNumber;
	private String name; 
	private String description; 

	public Organization(String name, String description) {
		super();
		this.name = name;
		this.description = description;
		this.registrationNumber = longHashCode();
	}
	
	public Organization(String name) {
		this(name, null);
	}

	@Override
	public int hashCode() {
		return (int) (this.longHashCode() % Integer.MAX_VALUE);
	}
	
	public long  longHashCode() {
		final long prime = 86796792733316639l;
		long result = 1;
		long temp = 3;
		result = prime * result + (long) (temp ^ (temp >>> 32));
		temp = name.hashCode();
		result = prime * result + (long) (temp ^ (temp >>> 32));
		temp = description.hashCode();
		result = prime * result + (long) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Organization other = (Organization) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public long getRegistrationNumber() {
		return registrationNumber;
	}
}
