package domain;


public enum Intelligence { BORDERLINE("At this tier, creatures cannot meaningfully communicate for the most part, they behave programmatically, and function essentially in a non-cognitive manner. Yet they may have procedural memory, like for unconscious motor skills, and â€” if living and subject to evolution â€” they might have adaptive memory as a consequence of species selection. Processing and learning come incredibly slowly, mostly skating by on random mutation. They also lack any sort of notable working memory."),
							NONSAPIENT("At this tier, creatures have very limited communication, and operate primarily based on instinct. They have basic cognition, but largely survive through physiological trauma-adaptations, whether within a lifetime, or across generations. Minimally-susceptible to training, mostly based on pleasure and pain, they have singular thoughts only, if at all. But they have begun to develop the most rudimentary form of thoughts, based on attraction and aversion."), 
							PRESAPIENT("At this tier, creatures largely use pantomiming to communicate, and have a foundational intuition around things like ratios of predators-to-prey within view. They have begun to grasp advantage and disadvantage more meaningfully. They possess basic visualization, such as through backtracking very recent experiences, with functioning short-term memory. They can learn through rote learning, as in memorization through repetition. And in terms of their working memory capacity, they have begun to alert and orient themselves away from immediate survival concerns when possible. "), 
							SAPIENT("At this tier, creatures bear full capacity for simple symbolic communication, though they may still misunderstand or misuse more complex words often. They have basic inductive reasoning, a better grasp of probabilities, though will tend to overgeneralize. They can have more abstract thoughts, as well as more meaningful flashbacks or daydreams. They bear episodic memory, remembering \\\"what\\\", \\\"when\\\", and \\\"where\\\" events, as well as semantic memory, beginning to remember some ideas as facts and principles. They may have an identifiable learning style, though constrained by a volatile train of thought which can become derailed easily."), 
							SUBCOMMON("At this tier, creatures may seem dull and take things literally, but nevertheless have more complex visualization, through forecasting and anticipation. They may misremember often, but have developed a method for compensating for a minimal deficit of theirs, using a strength to cover a weakness. And they have a more solid train of thought too, though they may overstretch that by trying to multitask. "), 
							COMMON("At this tier, creatures become fluent in complex communication, and skilled at deduction via many related details. Capable of complex ideation and symbolism, they will sometimes get flashes of insight depicting novel techniques. They may show mild forgetfulness, but have developed a system of compensating for a moderate deficit. Whatâ€™s more, they can demonstrate inefficient active multitasking through sequentially focusing on several tasks within a short window of time. These creatures may reach topical proficiency through natural intelligence alone."), 
							SMART("At this tier, creatures may present themselves in more individual ways, such as with wit and sarcasm. And they may have ratiocination routines, where they evaluate the consistency of their thinking. With moderate ability to mentally simulate complex phenomena across multiple senses, they sometimes have flashes of insight on novel processes rather than just techniques. Likewise, with strong prospective memory, they can demonstrate higher levels of planning and intentionality. At this point they can compensate for a more major deficit, and are sometimes known for ruminating on more abstract or philosophical matters. They may reach topical mastery in a subject from their raw mental acuity. "), 
							GIFTED("At this tier, creatures begin to present a bit more eccentrically, perhaps with idiosyncratic speech patterns, or the clever use of irony. They can sometimes make solid deductions based on many seemingly unrelated details. Occasionally, the Gifted may have flashes of insight on novel systems, which they can put to good use with a strong working memory. Notably, creatures of this intellectual level may begin to attempt a simple skill transference, filling skill gaps with a reliance on wit."), 
							GENIUS("At this tier, creatures develop arbitrary mnemonics, meaning linguistic or visualization techniques that enhance memorization through methods of imagination, association, or location, which can free up working memory and increase recollection. For instance, the classic phrase â€œRoy G. Bivâ€� to recall the order of a rainbowâ€™s colors (red, orange, green, blue, indigo, violet). This can also include useful acronyms and adages, or mapping unrelated names onto spaces within oneâ€™s navigational visualization. Additionally, they can show a stellar grasp on induction and formal logic, as well as a strong reflex for analogy, simile, and metaphor. And a related aspect, the Geniusâ€™ ability for complex simulation, allows improved forecasting of probabilities. Now, rather than just an occasional flash of insight, these creaturesâ€™ demeanor changes toward recurring inspiration, frequently devising new techniques."),
							PRODIGY("Creatures at this tier begin to reach the threshold of hyperintelligence. They may develop practical mnemonics, where the mnemonic mechanism itself becomes practical rather than arbitrary. For instance, they may imprint terms to memorize onto physical routines that require many tasks but little specific order, like varied cleaning tasks, allowing for planned, multi-faceted productivity both mental and physical at once. They often also exhibit an uncanny ability to deduce accurate conclusions using a very minimal number of related details. Likewise, the Prodigyâ€™s capacity for sublitizing, meaning the number of objects they can immediately group to count or estimate with, increases significantly. Additionally, they exhibit recurring inspiration on novel processes, as well as extreme breadth of stored memory. Overall, creatures at this tier become stunningly agile or methodical learners. They can reliably distinguish between a vast array of alternatives in problem-solving, memorize long lists in short windows of time, and use active sequential multitasking fairly efficiently."), 
							MASTERMIND("At this tier, creatures reach hyperintelligence proper. To start, their auditory memory functions more like lower visual memory, with the ability to recall, playback, and loop sounds. They likely have audiographic memory, with perfect vocal memory, able to instantly replicate sounds theyâ€™ve heard only once. Their minds can do passively more than most can ever reach actively, A Mastermind lives in a state of recurring inspiration, contemplating and devising whole systems. This sometimes reaches ideogenesis, or continuous creativity with no intermediate steps or withdrawal periods."),
							PSYCHOMETER("At this tier, the creatures mindsets take on a more mystical character in the eyes of lower tier beings.");

	private final String description;

	private Intelligence(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static Intelligence parse(String description) {
		if (description.equals("BORDERLINE")) {
			return Intelligence.BORDERLINE;
		}
		if (description.equals("NONSAPIENT")) {
			return Intelligence.NONSAPIENT;
		}
		if (description.equals("PRESAPIENT")) {
			return Intelligence.PRESAPIENT;
		}
		if (description.equals("SAPIENT")) {
			return Intelligence.SAPIENT;
		}
		if (description.equals("SUBCOMMON")) {
			return Intelligence.SUBCOMMON;
		}
		if (description.equals("COMMON")) {
			return Intelligence.COMMON;
		}
		if (description.equals("SMART")) {
			return Intelligence.SMART;
		}
		if (description.equals("GIFTED")) {
			return Intelligence.GIFTED;
		}
		if (description.equals("GENIUS")) {
			return Intelligence.GENIUS;
		}
		if (description.equals("PRODIGY")) {
			return Intelligence.PRODIGY;
		}
		if (description.equals("MASTERMIND")) {
			return Intelligence.MASTERMIND;
		}
		if (description.equals("PSYCHOMETER")) {
			return Intelligence.PSYCHOMETER;
		}
		return null;
	}
}
