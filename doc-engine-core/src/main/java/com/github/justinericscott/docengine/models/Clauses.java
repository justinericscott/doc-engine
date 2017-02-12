package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Clauses {

	@JsonDeserialize(contentAs = Clause.class)
	@JsonSerialize(contentAs = Clause.class)
	private Clause[] clauses;

	public Clauses() {
		// TODO Auto-generated constructor stub
	}

	public Clauses(final Clause[] clauses) {
		this.clauses = clauses;
	}

	public Clauses(final Collection<Clause> clauses) {
		setClauses(clauses);
	}

	public Clause[] getClauses() {
		return clauses;
	}

	@JsonIgnore
	public final Collection<Clause> getClausesList() {
		if (isNotNullOrEmpty(clauses)) {
			return Arrays.asList(clauses);
		}
		return null;
	}

	public void setClauses(final Clause[] clauses) {
		this.clauses = clauses;
	}

	@JsonIgnore
	public void setClauses(final Collection<Clause> clauses) {
		if (isNotNullOrEmpty(clauses)) {
//			this.clauses = clauses.toArray(new ClauseJpaImpl[clauses.size()]);
			this.clauses = clauses.toArray(new Clause[clauses.size()]);
		}
	}
}
