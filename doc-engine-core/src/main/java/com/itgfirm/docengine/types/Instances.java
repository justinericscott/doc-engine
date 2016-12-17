/**TODO: License
 */
package com.itgfirm.docengine.types;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Justin Scott
 * TODO: Description
 */
public class Instances {

	@JsonDeserialize( contentAs = InstanceJpaImpl.class )
	private List<InstanceJpaImpl> instances;
	
	public Instances() {
		
	}
	
	public Instances(List<InstanceJpaImpl> instances) {
		this.setInstances(instances);
	}

	public List<InstanceJpaImpl> getInstances() { return instances; }
	public void setInstances(List<InstanceJpaImpl> instances) { this.instances = instances; }
}
