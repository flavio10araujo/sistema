package br.com.uol.pagseguro.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a PagSeguro authorization
 */
public class Authorization {
	
	/** Authorization code **/
	private String code;
	
	/** Date of the authorization creation */
	private Date date;
	
    /** A reference to associate the PagSeguro authorization to a authorization in your system. */
	private String reference;
	
	/** Permission list of this authorization **/
	private List<Permission> permissions;
	
	public Authorization() {
		
		permissions = new ArrayList<Permission>();
		
	}

	/**
	 * Get authorization code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Set authorization code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Get creation date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Set creation date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Get Reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * Set Reference
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * Get the list of permissions
	 */
	public List<Permission> getPermissions() {
		return this.permissions;
	}

	/**
	 * Add Permissions
	 */
	public void addPermission(Permission permission){
		if (permission != null) {
			this.permissions.add(permission);
		}
	}


	
}
