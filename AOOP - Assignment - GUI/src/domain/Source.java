package domain;

public class Source {
	private long registrationNumber;
	private String name; 
	private String description; 
	private Organization owner;
	
	
	public Source(String name, String description, Organization owner) {
		super();
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.registrationNumber = longHashCode();
	}
	
	public Source(String name, Organization owner) {
		this(name, null, owner);
	}
	
	public Source(String name, String description) {
		this(name, description, null);
	}
	
	public Source(String name) {
		this(name, null, null);
	}
	
	@Override
	public int hashCode() {
		return (int) (this.longHashCode() % Integer.MAX_VALUE);
	}
	
	public long  longHashCode() {
		final long prime = 60410923346665403l;
		long result = 1;
		long temp = 3;
		result = prime * result + (long) (temp ^ (temp >>> 32));
		temp = name.hashCode();
		result = prime * result + (long) (temp ^ (temp >>> 32));
		temp = description.hashCode();
		result = prime * result + (long) (temp ^ (temp >>> 32));
		temp = owner.longHashCode();
		result = prime * result + (long) (temp ^ (temp >>> 32));
		return result;
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

	public Organization getOwner() {
		return owner;
	}

	public void setOwner(Organization owner) {
		this.owner = owner;
	}

	public long getRegistrationNumber() {
		return registrationNumber;
	}
}
