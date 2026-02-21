package domain.types;

public enum PlanetType { D("Moon", "An uninhabitable planetoid, moon, or small planet with little to no atmosphere", Habitability.TERRAFORMING),
						H("", "Generally uninhabitable for Humans due to gravity or climate, might be suitable for other species", Habitability.MARGINALLY),
						J("", "Gas Giant with no nuclear fusion", Habitability.NO),
						K("", "Adaptable for Humans by use of artificial biospheres", Habitability.BIOSPHERE),
						L("", "Marginally habitable, with vegetation but usually no animal life", Habitability.MARGINALLY),
						M("Gaia", "Earth-like, with atmospheres containing oxygen. Largely habitable for humanoid life forms.", Habitability.YES),
						N("", "Moon or small planet with atmosphere but very low temperatures", Habitability.BIOSPHERE),
						R("", "Moon orbiting within heavy radiation of a gas giant", Habitability.NO),
						T("", "Gas giant with some nuclear fusion at its core", Habitability.NO),
						Y("Demon", "A world with a toxic atmosphere and surface temperatures exceeding 500 Kelvin. Prone to thermic and/ or radiation discharges.", Habitability.NO);
	private String nickname;
	private String description;
	private Habitability habitability;

	private PlanetType(String nickname, String description, Habitability habitability) {
		this.nickname = nickname;
		this.description = description;
		this.habitability = habitability;
	}

	public String getNickname() {
		return nickname;
	}

	public String getDescription() {
		return description;
	}

	public Habitability getHabitability() {
		return habitability;
	}

	public enum Habitability { NO, BIOSPHERE, TERRAFORMING, MARGINALLY, YES
		
	}
}
