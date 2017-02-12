package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class ClauseInstances {

	@JsonDeserialize(contentAs = ClauseInstance.class)
	@JsonSerialize(contentAs = ClauseInstance.class)
	private ClauseInstance[] clauses;

	public ClauseInstances() {
		// Default constructor for Spring
	}
	
	public ClauseInstances(final ClauseInstance[] clauses) {
		this.clauses = clauses;
	}	

	public ClauseInstances(final Collection<ClauseInstance> clauses) {
		setClauses(clauses);
	}

	public final ClauseInstance[] getClauses() {
		return this.clauses;
	}

	@JsonIgnore
	public final Collection<ClauseInstance> getClausesList() {
		if (isNotNullOrEmpty(clauses)) {
			return Arrays.asList(clauses);	
		}
		return null;
	}

	public final void setClauses(final ClauseInstance[] clauses) {
		this.clauses = clauses;
	}

	@JsonIgnore
	public final void setClauses(final Collection<ClauseInstance> clauses) {
		if (isNotNullOrEmpty(clauses)) {
			this.clauses = clauses.toArray(new ClauseInstance[clauses.size()]);	
		}		
	}
}