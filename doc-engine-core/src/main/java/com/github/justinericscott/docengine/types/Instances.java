/**TODO: License
 */
package com.github.justinericscott.docengine.types;

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

	@JsonDeserialize(contentAs = InstanceJpaImpl.class)
	@JsonSerialize(contentAs = InstanceJpaImpl.class)
	private InstanceJpaImpl[] instances = null;

	public Instances() {
		// Default constructor for Spring
	}

	public Instances(final InstanceJpaImpl[] instances) {
		this.instances = instances;
	}

	public Instances(final Collection<InstanceJpaImpl> instances) {
		setInstances(instances);
	}

	public final InstanceJpaImpl[] getInstances() {
		return instances;
	}

	@JsonIgnore
	public final Collection<InstanceJpaImpl> getInstancesList() {
		if (isNotNullOrEmpty(instances)) {
			return Arrays.asList(instances);
		}
		return null;
	}

	public final void setInstances(final InstanceJpaImpl[] instances) {
		this.instances = instances;
	}

	@JsonIgnore
	public final void setInstances(final Collection<InstanceJpaImpl> instances) {
		if (isNotNullOrEmpty(instances)) {
			this.instances = instances.toArray(new InstanceJpaImpl[instances.size()]);
		}
	}
}
