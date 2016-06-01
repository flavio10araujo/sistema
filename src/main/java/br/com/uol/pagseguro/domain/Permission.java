package br.com.uol.pagseguro.domain;

import java.util.Date;

import br.com.uol.pagseguro.enums.PermissionStatus;
import br.com.uol.pagseguro.enums.PermissionType;

public class Permission {
	
	/** Permission of an authorization **/
	private PermissionType permission;
	
	/** Status of this permission **/
	private PermissionStatus status;
	
	/** Last update date of this permission **/
	private Date lastUpdate;

	/**
	 * Get Permission
	 */
	public PermissionType getPermission() {
		return this.permission;
	}

	/**
	 * Set Permission
	 */
	public void setPermission(PermissionType permission) {
		this.permission = permission;
	}

	/**
	 * Get Status
	 */
	public PermissionStatus getStatus() {
		return status;
	}

	/**
	 * Set Status
	 */
	public void setStatus(PermissionStatus status) {
		this.status = status;
	}
	
	/**
	 * Get last update date
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * Set last update date
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	
}
