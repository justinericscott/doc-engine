package com.github.justinericscott.docengine.types;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Clauses {

	@JsonDeserialize(contentAs = ClauseJpaImpl.class)
	@JsonSerialize(contentAs = ClauseJpaImpl.class)
	private ClauseJpaImpl[] clauses;

	public Clauses() {
		// TODO Auto-generated constructor stub
	}

	public Clauses(final ClauseJpaImpl[] clauses) {
		this.clauses = clauses;
	}

	public Clauses(final Collection<ClauseJpaImpl> clauses) {
		setClauses(clauses);
	}

	public ClauseJpaImpl[] getClauses() {
		return clauses;
	}

	@JsonIgnore
	public final Collection<ClauseJpaImpl> getClausesList() {
		if (isNotNullOrEmpty(clauses)) {
			return Arrays.asList(clauses);
		}
		return null;
	}

	public void setClauses(final ClauseJpaImpl[] clauses) {
		this.clauses = clauses;
	}

	@JsonIgnore
	public void setClauses(final Collection<ClauseJpaImpl> clauses) {
		if (isNotNullOrEmpty(clauses)) {
//			this.clauses = clauses.toArray(new ClauseJpaImpl[clauses.size()]);
			this.clauses = clauses.toArray(new ClauseJpaImpl[clauses.size()]);
		}
	}
}
