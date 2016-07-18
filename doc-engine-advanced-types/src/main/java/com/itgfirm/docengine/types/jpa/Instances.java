/**TODO: License
 */
package com.itgfirm.docengine.types.jpa;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itgfirm.docengine.types.Instance;

/**
 * @author Justin Scott
 * TODO: Description
 */
public class Instances {

	@JsonDeserialize( contentAs = InstanceJpaImpl.class )
	private List<Instance> instances;
	
	public Instances() {
		
	}
	
	public Instances(List<Instance> instances) {
		this.setInstances(instances);
	}

	public List<Instance> getInstances() { return instances; }
	public void setInstances(List<Instance> instances) { this.instances = instances; }
}
