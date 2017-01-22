package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class ClauseInstances {

	@JsonDeserialize(contentAs = ClauseInstanceJpaImpl.class)
	@JsonSerialize(contentAs = ClauseInstanceJpaImpl.class)
	private ClauseInstanceJpaImpl[] clauses;

	public ClauseInstances() {
		// Default constructor for Spring
	}
	
	public ClauseInstances(final ClauseInstanceJpaImpl[] clauses) {
		this.clauses = clauses;
	}	

	public ClauseInstances(final Collection<ClauseInstanceJpaImpl> clauses) {
		setClauses(clauses);
	}

	public final ClauseInstanceJpaImpl[] getClauses() {
		return this.clauses;
	}

	@JsonIgnore
	public final Collection<ClauseInstanceJpaImpl> getClausesList() {
		if (isNotNullOrEmpty(clauses)) {
			return Arrays.asList(clauses);	
		}
		return null;
	}

	public final void setClauses(final ClauseInstanceJpaImpl[] clauses) {
		this.clauses = clauses;
	}

	@JsonIgnore
	public final void setClauses(final Collection<ClauseInstanceJpaImpl> clauses) {
		if (isNotNullOrEmpty(clauses)) {
			this.clauses = clauses.toArray(new ClauseInstanceJpaImpl[clauses.size()]);	
		}		
	}
}