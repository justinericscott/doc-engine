/**TODO: License
 */
package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Justin Scott TODO: Description
 */
public class Instances {

	@JsonDeserialize(contentAs = Instance.class)
	@JsonSerialize(contentAs = Instance.class)
	private Instance[] instances = null;

	public Instances() {
		// Default constructor for Spring
	}

	public Instances(final Instance[] instances) {
		this.instances = instances;
	}

	public Instances(final Collection<Instance> instances) {
		setInstances(instances);
	}

	public final Instance[] getInstances() {
		return instances;
	}

	@JsonIgnore
	public final Collection<Instance> getInstancesList() {
		if (isNotNullOrEmpty(instances)) {
			return Arrays.asList(instances);
		}
		return null;
	}

	public final void setInstances(final Instance[] instances) {
		this.instances = instances;
	}

	@JsonIgnore
	public final void setInstances(final Collection<Instance> instances) {
		if (isNotNullOrEmpty(instances)) {
			this.instances = instances.toArray(new Instance[instances.size()]);
		}
	}
}
